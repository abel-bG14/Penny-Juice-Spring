package com.example.penny_juice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.penny_juice.model.Usuario;
import com.example.penny_juice.repository.UsuarioRepository;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  public List<Usuario> listarUsuario(){
    return usuarioRepository.findAll();
  }

  public Usuario obtenerPorId(Long id){
    return usuarioRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
  }

  public void eliminarPorId(Long id){
    usuarioRepository.deleteById(id);
  }

  public Usuario editUsuario(Long id, Usuario usuarioActualizado){
    Usuario usuarioExistente = usuarioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    usuarioExistente.editUsuario(usuarioActualizado);

    return usuarioRepository.save(usuarioExistente);
  }

  
}
