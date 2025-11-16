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

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String registroForm(Model model){
        model.addAttribute("usuario", new Usuario());
        return "register";
    }

    @PostMapping("/guardar")
    public String registrarUsuario(@ModelAttribute Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(new Date());
        }
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("USER");
        }
        usuarioRepository.save(usuario);
        return "redirect:/login";
    }

    @GetMapping({ "/", "/index" })
    public String home() {
        return "index";
    }
}
