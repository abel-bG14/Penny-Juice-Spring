package com.example.penny_juice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.penny_juice.model.Usuario;
import com.example.penny_juice.service.UsuarioService;

@Controller
@RequestMapping("/usuarios") //CUALQUIER RUTA EMPEZARÁ CON /usuarios
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    //MUESTRA LA LISTA DE USUARIOS
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("listaUsuarios", usuarioService.listar());
        return "usuarios/lista"; //TEMPLATES: PÁGINA lista.html (PRONTA INCORPORACIÓN)
    }
    //MUESTRA EL FORMULARIO DE NUEVO USUARIO
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form"; //TEMPLATES: PÁGINA form.html (PRONTA INCORPORACIÓN)
    }
    //GUARDAR NUEVO USUARIO
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/usuarios"; //REDIRIGE A lista.html (SE PUEDE CAMBIAR LA REDIRECCIÓN)
    }
    //MOSTRAR FORMULARIO PARA EDITAR
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.obtenerPorId(id));
        return "usuarios/form";
    }
    //ELIMINAR USUARIO
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios";
    }
}
