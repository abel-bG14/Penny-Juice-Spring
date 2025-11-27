package com.example.penny_juice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.penny_juice.model.Producto;
import com.example.penny_juice.service.ProductoService;


@Controller
public class HomeController {
    
    private final ProductoService productoService;

    // Inyección por constructor
    public HomeController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/galeria")
    public String galeria(Model model) {
        model.addAttribute("listaProductos", productoService.listar());
        return "galeria";
    }

    @GetMapping("/carrito")
    public String carrito() {
        return "carrito";
    }

    @GetMapping("/galeria/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerPorId(id); // devuelve Producto o null
        if (producto == null) {
            return "redirect:/galeria"; // o una página de error personalizada
        }
        model.addAttribute("producto", producto);
        return "detalle-producto"; // nombre del HTML Thymeleaf
    }

    @GetMapping("/panel")
    public String panelAdministrativo() {
        return "panel-administrativo";
    }

    @GetMapping("/us")
    public String us() {
        return "us";
    }

    @GetMapping("/learn-more")
    public String learnMore() {
        return "learn-more";
    }
}