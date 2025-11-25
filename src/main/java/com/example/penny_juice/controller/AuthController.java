package com.example.penny_juice.controller;

import com.example.penny_juice.model.Usuario;
import com.example.penny_juice.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import java.util.Date;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public String login(@org.springframework.web.bind.annotation.RequestParam(value = "error", required = false) String error,
                        @org.springframework.web.bind.annotation.RequestParam(value = "logout", required = false) String logout,
                        Model model){
        if (error != null) {
            model.addAttribute("errorMessage", "Usuario o contraseña inválidos.");
        }
        if (logout != null) {
            model.addAttribute("infoMessage", "Sesión cerrada correctamente.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registroForm(Model model){
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/guardar")
    public String registrarUsuario(@ModelAttribute Usuario usuario, org.springframework.validation.BindingResult bindingResult, Model model) {
        logger.info("Intento de registro para correo={}", usuario != null ? usuario.getCorreo() : "(nulo)");

        if (bindingResult.hasErrors()) {
            logger.warn("Errores de binding en el formulario de registro: {}", bindingResult.getAllErrors());
            java.util.List<String> errors = new java.util.ArrayList<>();
            for (org.springframework.validation.FieldError fe : bindingResult.getFieldErrors()) {
                errors.add("Campo '" + fe.getField() + "' inválido: valor='" + fe.getRejectedValue() + "' - " + fe.getDefaultMessage());
            }
            for (org.springframework.validation.ObjectError oe : bindingResult.getGlobalErrors()) {
                errors.add("Error: " + oe.getDefaultMessage());
            }
            model.addAttribute("bindingErrors", errors);
            model.addAttribute("usuario", usuario);
            model.addAttribute("errorMessage", "Datos del formulario inválidos. Revise los campos.");
            return "register";
        }

        // Validaciones manuales básicas para detectar campos obligatorios antes de persistir
        if (usuario == null) {
            model.addAttribute("errorMessage", "Datos del formulario no recibidos.");
            return "register";
        }
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()
                || usuario.getApellidos() == null || usuario.getApellidos().isBlank()
                || usuario.getFechaNacimiento() == null
                || usuario.getTelefono() == null || usuario.getTelefono().isBlank()
                || usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("errorMessage", "Por favor complete todos los campos obligatorios.");
            return "register";
        }

        try {
            String masked = (usuario == null || usuario.getPassword() == null) ? "(nulo)" : "******";
            logger.debug("Password (masked)={}", masked);

            if (usuario == null || usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                model.addAttribute("usuario", usuario);
                model.addAttribute("errorMessage", "La contraseña es obligatoria.");
                return "register";
            }

            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            if (usuario.getFechaRegistro() == null) {
                usuario.setFechaRegistro(new Date());
            }
            if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
                usuario.setRol("USER");
            }

            Usuario saved = usuarioRepository.save(usuario);
            logger.info("Usuario guardado correctamente: IdUsuario={}, Correo={}", saved.getId(), saved.getCorreo());
            return "redirect:/login";
        } catch (org.springframework.dao.DataIntegrityViolationException dive) {
            String correo = usuario != null ? usuario.getCorreo() : "(nulo)";
            logger.warn("Violación de integridad al guardar usuario (posible correo duplicado): {}", correo, dive);
            model.addAttribute("usuario", usuario);
            model.addAttribute("errorMessage", "El correo ya está registrado.");
            return "register";
        } catch (Exception ex) {
            String correo = usuario != null ? usuario.getCorreo() : "(nulo)";
            logger.error("Error inesperado al guardar usuario (correo={}) - {}", correo, ex.getMessage());
            model.addAttribute("usuario", usuario);
            // Mostrar mensaje de excepción para ayudar a depurar localmente
            model.addAttribute("errorMessage", "Ocurrió un error al procesar el registro: " + ex.getMessage());
            return "register";
        }
    }

    @GetMapping({ "/", "/index" })
    public String home() {
        return "index";
    }
}
