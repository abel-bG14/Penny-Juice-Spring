package com.example.penny_juice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.penny_juice.model.Usuario;
import com.example.penny_juice.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    public Usuario obtenerPorId(Long idUsuario) {
        Optional<Usuario> opt = usuarioRepository.findById(idUsuario);
        return opt.orElse(null);
    }
    public void eliminar(Long idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }
    //POR MIENTRAS UN MÉTODO QUE BUSQUE POR CORREO
    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}
