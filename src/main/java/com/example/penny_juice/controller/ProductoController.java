package com.example.penny_juice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.penny_juice.model.Producto;
import com.example.penny_juice.service.ProductoService;

@Controller
@RequestMapping("/productos")

public class ProductoController {
    @Autowired

    private ProductoService productoService;
    @GetMapping
    
    public String listarProductos(Model model) {
        model.addAttribute("listaProductos", productoService.listar());
        return "lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("producto", new Producto());
        return "formulario";
    }

   @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto) {
    productoService.guardar(producto);
    return "redirect:/productos";
}


    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return "error";
        }

        model.addAttribute("producto", producto);
        return "formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
        return "redirect:/productos";
    }

}
