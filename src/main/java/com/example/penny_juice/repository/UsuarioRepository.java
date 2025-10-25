package com.example.penny_juice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.penny_juice.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByCorreo(String correo);
}
