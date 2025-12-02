// Variables globales
console.log("[pagosJS] script loaded");
let isFormValid = false;
let cartManager;

class CartManager {
  constructor() {
    this.cart = JSON.parse(localStorage.getItem("pennyjuice_cart") || "{}");
    this.floatingCart = document.getElementById("floating-cart");
    this.cartCount = document.getElementById("cart-count");
    this.cartItemsContainer = document.getElementById("cart-items-container");
    this.subtotalElement = document.getElementById("subtotal");
    this.totalElement = document.getElementById("total");
    this.checkoutBtn = document.getElementById("checkout-btn");

    this.init();
  }

  // initialize: migrate legacy keys (if any) then render and bind events
  async init() {
    try {
      await this.migrateCartKeysIfNeeded();
    } catch (e) {
      console.warn("[pagosJS] cart migration failed", e);
    }
    this.renderCart();
    this.updateFloatingCart();
    this.bindEvents();
  }

  // Migrate cart keys that are non-numeric (legacy slugs/names) to numeric IDs
  async migrateCartKeysIfNeeded() {
    const keys = Object.keys(this.cart);
    const nonNumericKeys = keys.filter((k) => Number.isNaN(Number(k)));
    if (nonNumericKeys.length === 0) return;

    console.log("[pagosJS] migrating cart keys", nonNumericKeys);

    for (const key of nonNumericKeys) {
      try {
        const item = this.cart[key];
        const nombre = item && item.name ? item.name : key;
        // Try resolving on the server by product name
        const resp = await fetch(
          `/api/productos/resolve?nombre=${encodeURIComponent(nombre)}`
        );
        if (!resp.ok) {
          console.warn(
            "[pagosJS] resolve failed for",
            nombre,
            "status",
            resp.status
          );
          continue;
        }
        const data = await resp.json();
        const newId = String(data.id);
        if (!newId || Number.isNaN(Number(newId))) {
          console.warn("[pagosJS] resolved id invalid for", nombre, data);
          continue;
        }

        // If numeric key already exists, merge quantities
        if (this.cart[newId]) {
          this.cart[newId].quantity =
            (this.cart[newId].quantity || 0) + (item.quantity || 0);
        } else {
          // move item under numeric id
          this.cart[newId] = Object.assign({}, item, { id: newId });
        }
        // remove old key
        delete this.cart[key];
        console.log("[pagosJS] migrated", key, "->", newId);
      } catch (e) {
        console.error("[pagosJS] error migrating key", key, e);
      }
    }

    // persist changes
    localStorage.setItem("pennyjuice_cart", JSON.stringify(this.cart));
  }

  bindEvents() {
    this.floatingCart.addEventListener("click", () => {
      window.location.reload();
    });
  }

  renderCart() {
    const cartItems = Object.values(this.cart);

    if (cartItems.length === 0) {
      this.showEmptyCart();
      return;
    }

    this.cartItemsContainer.innerHTML = cartItems
      .map(
        (item) => `
                    <div class="cart-item" data-id="${item.id}">
                        <img src="${item.image}" alt="${
          item.name
        }" class="item-image">
                        <div class="item-info">
                            <div class="item-name">${item.name}</div>
                            <div class="item-price">S/ ${item.price.toFixed(
                              2
                            )}</div>
                            <div class="quantity-controls">
                                <button class="quantity-btn decrease-btn" onclick="cartManager.updateQuantity('${
                                  item.id
                                }', -1)">
                                    <i class="fas fa-minus"></i>
                                </button>
                                <span class="quantity-display">${
                                  item.quantity
                                }</span>
                                <button class="quantity-btn increase-btn" onclick="cartManager.updateQuantity('${
                                  item.id
                                }', 1)">
                                    <i class="fas fa-plus"></i>
                                </button>
                            </div>
                            <button class="remove-btn" onclick="cartManager.removeItem('${
                              item.id
                            }')">
                                <i class="fas fa-trash"></i> Eliminar
                            </button>
                        </div>
                    </div>
                `
      )
      .join("");

    this.updateSummary();
  }

  showEmptyCart() {
    this.cartItemsContainer.innerHTML = `
                    <div class="empty-cart">
                        <i class="fas fa-shopping-cart"></i>
                        <h3>Tu carrito está vacío</h3>
                        <p>Agrega algunos productos increíbles para comenzar</p>
                        <a href="../PRODUCTOS/productos.html" class="continue-shopping-btn">
                            Continuar Comprando
                        </a>
                    </div>
                `;

    this.subtotalElement.textContent = "S/ 0.00";
    this.totalElement.textContent = "S/ 0.00";
    this.checkoutBtn.disabled = true;
  }

  updateQuantity(itemId, change) {
    if (this.cart[itemId]) {
      this.cart[itemId].quantity += change;

      if (this.cart[itemId].quantity <= 0) {
        delete this.cart[itemId];
      }

      this.saveCart();
      this.renderCart();
      this.updateFloatingCart();
    }
  }

  removeItem(itemId) {
    if (confirm("¿Estás seguro de que quieres eliminar este producto?")) {
      delete this.cart[itemId];
      this.saveCart();
      this.renderCart();
      this.updateFloatingCart();
    }
  }

  updateSummary() {
    const subtotal = Object.values(this.cart).reduce(
      (sum, item) => sum + item.price * item.quantity,
      0
    );

    this.subtotalElement.textContent = `S/ ${subtotal.toFixed(2)}`;
    this.totalElement.textContent = `S/ ${subtotal.toFixed(2)}`;

    // Actualizar estado del botón
    updateCheckoutButton();
  }

  updateFloatingCart() {
    const totalItems = Object.values(this.cart).reduce(
      (sum, item) => sum + item.quantity,
      0
    );

    if (totalItems > 0) {
      this.floatingCart.classList.remove("hidden");
      this.cartCount.textContent = totalItems;
    } else {
      this.floatingCart.classList.add("hidden");
    }
  }

  saveCart() {
    localStorage.setItem("pennyjuice_cart", JSON.stringify(this.cart));
  }
}

// Funciones de validación y pago
function selectPaymentMethod(element, type) {
  document
    .querySelectorAll(".payment-method")
    .forEach((method) => method.classList.remove("selected"));
  element.classList.add("selected");
  element.querySelector('input[type="radio"]').checked = true;

  const cardForm = document.getElementById("card-form");
  const qrSection = document.getElementById("qr-section");
  const selectedValue = element.querySelector('input[type="radio"]').value;

  if (type === "card") {
    cardForm.classList.remove("hidden");
    qrSection.classList.add("hidden");
    hideAllQRCodes();
    validateForm();
  } else if (type === "digital") {
    cardForm.classList.add("hidden");
    qrSection.classList.remove("hidden");
    showQRCode(selectedValue);
    isFormValid = true;
    updateCheckoutButton();
  }
}

function showQRCode(method) {
  hideAllQRCodes();
  const qrCode = document.getElementById(`${method}-qr`);
  if (qrCode) {
    qrCode.classList.remove("hidden");
  }
}

function hideAllQRCodes() {
  document.querySelectorAll(".qr-code").forEach((qr) => {
    qr.classList.add("hidden");
  });
}

function validateForm() {
  const cardNumber = document.getElementById("card-number").value;
  const cardExpiry = document.getElementById("card-expiry").value;
  const cardCVV = document.getElementById("card-cvv").value;
  const cardName = document.getElementById("card-name").value;

  const isNumberValid = validateCardNumber(cardNumber);
  const isExpiryValid = validateExpiry(cardExpiry);
  const isCVVValid = validateCVV(cardCVV);
  const isNameValid = cardName.trim().length >= 2;

  updateFieldValidation("card-number", isNumberValid);
  updateFieldValidation("card-expiry", isExpiryValid);
  updateFieldValidation("card-cvv", isCVVValid);
  updateFieldValidation("card-name", isNameValid);

  isFormValid = isNumberValid && isExpiryValid && isCVVValid && isNameValid;
  updateCheckoutButton();
}

function validateCardNumber(number) {
  const cleaned = number.replace(/\s/g, "");
  return cleaned.length >= 13 && cleaned.length <= 19 && /^\d+$/.test(cleaned);
}

function validateExpiry(expiry) {
  const regex = /^(0[1-9]|1[0-2])\/\d{2}$/;
  if (!regex.test(expiry)) return false;

  const [month, year] = expiry.split("/");
  const currentDate = new Date();
  const currentYear = currentDate.getFullYear() % 100;
  const currentMonth = currentDate.getMonth() + 1;

  const cardYear = parseInt(year);
  const cardMonth = parseInt(month);

  if (cardYear < currentYear) return false;
  if (cardYear === currentYear && cardMonth < currentMonth) return false;

  return true;
}

function validateCVV(cvv) {
  return cvv.length >= 3 && cvv.length <= 4 && /^\d+$/.test(cvv);
}

function updateFieldValidation(fieldId, isValid) {
  const field = document.getElementById(fieldId);
  field.classList.remove("error", "valid");

  if (field.value.trim() !== "") {
    field.classList.add(isValid ? "valid" : "error");
  }
}

function updateCheckoutButton() {
  const checkoutBtn = document.getElementById("checkout-btn");
  const cartHasItems = cartManager && Object.keys(cartManager.cart).length > 0;

  // We won't disable the button (disabled buttons don't receive clicks).
  // Instead change visual state and keep the click handler active so processPayment()
  // can show clear messages when submission is not possible.
  checkoutBtn.style.opacity = cartHasItems && isFormValid ? "1" : "0.6";
  // Ensure the button is clickable even if another code path previously disabled it
  try {
    checkoutBtn.disabled = false;
  } catch (e) {
    // ignore if element missing
  }
}

function getCurrentUserId() {
  try {
    return document.getElementById("current-user-id")?.value || null;
  } catch (e) {
    return null;
  }
}

function processPayment() {
  console.log("[pagosJS] processPayment invoked", {
    cartPresent: !!cartManager,
  });
  // Validar que haya artículos y que el formulario esté listo
  const cartHasItems = cartManager && Object.keys(cartManager.cart).length > 0;
  if (!cartHasItems) {
    showToast("El carrito está vacío", "error");
    return;
  }

  // Si el método de pago es tarjeta, asegurarnos de que el formulario es válido
  const selectedPayment = document.querySelector(
    '.payment-method.selected input[type="radio"]'
  )
    ? document.querySelector('.payment-method.selected input[type="radio"]')
        .value
    : "card";

  console.log(
    "[pagosJS] selectedPayment=",
    selectedPayment,
    "isFormValid=",
    isFormValid
  );

  if (selectedPayment === "card" && !isFormValid) {
    console.log("[pagosJS] card payment selected but form invalid - aborting");
    showToast(
      "Por favor, complete los datos de la tarjeta correctamente",
      "error"
    );
    return;
  }

  // Preparar el formulario Thymeleaf para envío al servidor
  const cartDataInput = document.getElementById("cart-data");
  const paymentMethodInput = document.getElementById("payment-method-input");
  const subtotalInput = document.getElementById("subtotal-input");
  const idUsuario = getCurrentUserId();

  // Convertir el carrito en una lista (array) para enviarlo
  const items = Object.values(cartManager.cart).map((it) => ({
    id: it.id,
    name: it.name,
    price: it.price,
    quantity: it.quantity,
  }));
  console.log("[pagosJS] cart items prepared for submit", items);
  // Poblar el formulario Thymeleaf (campos que Spring puede bindear)
  // IdUsuario
  const idUsuarioInput = document.getElementById("IdUsuario");
  idUsuarioInput.value = idUsuario || "";

  // DireccionEnvio (puedes cambiar para solicitar al usuario)
  const direccionInput = document.getElementById("DireccionEnvio");
  direccionInput.value = "";

  // Limpiar contenedor de inputs de detalles
  const detallesContainer = document.getElementById("detalle-inputs");
  detallesContainer.innerHTML = "";

  // Validate product ids are numeric and populate detalle inputs
  for (let index = 0; index < items.length; index++) {
    const it = items[index];
    const prodIdNum = Number(it.id);

    if (
      Number.isNaN(prodIdNum) ||
      !Number.isFinite(prodIdNum) ||
      !Number.isInteger(prodIdNum)
    ) {
      console.error(
        "[pagosJS] invalid product id",
        it,
        "converted=",
        prodIdNum
      );
      showToast(
        `ID de producto inválido para '${it.name}'. No se puede procesar la compra.`,
        "error"
      );
      return; // abort submit
    }

    const inputIdProd = document.createElement("input");
    inputIdProd.type = "hidden";
    inputIdProd.name = `detalles[${index}].idProducto`;
    inputIdProd.value = String(prodIdNum);
    detallesContainer.appendChild(inputIdProd);

    const inputCant = document.createElement("input");
    inputCant.type = "hidden";
    inputCant.name = `detalles[${index}].cantidad`;
    inputCant.value = it.quantity || 1;
    detallesContainer.appendChild(inputCant);
  }

  // Finalmente, enviar el formulario al endpoint /venta (Thymeleaf/Spring lo bindeará)
  const form = document.getElementById("checkout-form");
  console.log("[pagosJS] about to submit form", {
    formExists: !!form,
    action: form ? form.action : null,
  });
  if (!form) {
    showToast(
      "Formulario de checkout no encontrado. Imposible enviar.",
      "error"
    );
    return;
  }

  try {
    if (typeof form.requestSubmit === "function") {
      form.requestSubmit();
      console.log("[pagosJS] used requestSubmit()");
    } else {
      form.submit();
      console.log("[pagosJS] used submit()");
    }
  } catch (err) {
    console.error("[pagosJS] error submitting form", err);
    showToast(
      "Error al enviar el formulario de pago. Revisa la consola.",
      "error"
    );
  }
}

// Inicialización cuando la página carga
document.addEventListener("DOMContentLoaded", function () {
  try {
    // Inicializar CartManager
    cartManager = new CartManager();

    // Inicializar validación
    isFormValid = false;
    updateCheckoutButton();

    // Event listeners para el formulario
    const cardNumberInput = document.getElementById("card-number");
    const cardExpiryInput = document.getElementById("card-expiry");
    const cardCVVInput = document.getElementById("card-cvv");
    const cardNameInput = document.getElementById("card-name");

    // Formateo automático del número de tarjeta
    if (cardNumberInput) {
      cardNumberInput.addEventListener("input", function (e) {
        let value = e.target.value.replace(/\s/g, "");
        let formattedValue = value.replace(/(.{4})/g, "$1 ").trim();
        e.target.value = formattedValue;

        // Detectar tipo de tarjeta
        const visaIcon = document.querySelector(".fa-cc-visa");
        const mastercardIcon = document.querySelector(".fa-cc-mastercard");
        const amexIcon = document.querySelector(".fa-cc-amex");

        if (visaIcon) visaIcon.classList.remove("active");
        if (mastercardIcon) mastercardIcon.classList.remove("active");
        if (amexIcon) amexIcon.classList.remove("active");

        if (value.startsWith("4") && visaIcon) {
          visaIcon.classList.add("active");
        } else if (
          (value.startsWith("5") || value.startsWith("2")) &&
          mastercardIcon
        ) {
          mastercardIcon.classList.add("active");
        } else if (value.startsWith("3") && amexIcon) {
          amexIcon.classList.add("active");
        }

        validateForm();
      });
    }

    // Formateo de fecha de vencimiento
    if (cardExpiryInput) {
      cardExpiryInput.addEventListener("input", function (e) {
        let value = e.target.value.replace(/\D/g, "");
        if (value.length >= 2) {
          value = value.substring(0, 2) + "/" + value.substring(2, 4);
        }
        e.target.value = value;
        validateForm();
      });
    }

    // Validación de CVV
    if (cardCVVInput) {
      cardCVVInput.addEventListener("input", function (e) {
        e.target.value = e.target.value.replace(/\D/g, "");
        validateForm();
      });
    }

    // Validación de nombre
    if (cardNameInput) {
      cardNameInput.addEventListener("input", function (e) {
        e.target.value = e.target.value.replace(/[^a-zA-ZÀ-ÿ\s]/g, "");
        validateForm();
      });
    }

    // Avoid adding a duplicate listener if the button already has an inline onclick
    const checkoutBtnEl = document.getElementById("checkout-btn");
    if (checkoutBtnEl) {
      const hasInlineOnclick = !!checkoutBtnEl.getAttribute("onclick");
      if (!hasInlineOnclick) {
        checkoutBtnEl.addEventListener("click", function (e) {
          e.preventDefault();
          processPayment();
        });
      } else {
        // If the button has inline onclick, keep it but log to help debugging
        console.log(
          "[pagosJS] checkout button has inline onclick - not adding duplicate listener"
        );
      }
    }
  } catch (err) {
    console.error("[pagosJS] error during DOMContentLoaded handler", err);
  }
});

function showToast(message, type = "success") {
  const toast = document.getElementById("toast");
  const messageElement = document.getElementById("toast-message");

  toast.className = `toast ${type}`;
  messageElement.textContent = message;
  toast.classList.remove("hidden");

  setTimeout(() => {
    toast.classList.add("hidden");
  }, 3000);
}
