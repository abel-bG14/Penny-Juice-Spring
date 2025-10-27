package com.example.penny_juice.controller;

import com.example.penny_juice.model.Producto;
import com.example.penny_juice.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("productos")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.listarProducto());
        return "productos";
    }


    @GetMapping("/nuevo")
    public String crearProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto/crear";
    }

    @PostMapping
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoService.crearProducto(producto);
        return "redirect:productos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerPorId(id);
        model.addAttribute("producto", producto);
        return "editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Long id, @ModelAttribute Producto producto) {
        productoService.editarProducto(id, producto);
        return "redirect:/producto";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return "redirect:/producto";
    }
}
