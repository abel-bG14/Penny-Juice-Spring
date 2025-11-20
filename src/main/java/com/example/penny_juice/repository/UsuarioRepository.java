package com.example.penny_juice.repository;

import com.example.penny_juice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);

    // Búsqueda case-insensitive para evitar problemas de mayúsculas/minúsculas
    Optional<Usuario> findByCorreoIgnoreCase(String correo);
}
