package com.example.penny_juice.service;

import com.example.penny_juice.model.Usuario;
import com.example.penny_juice.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        logger.info("Autenticación: buscando usuario por correo={}", correo);
        if (correo == null) {
            logger.warn("Correo null recibido en loadUserByUsername");
            throw new UsernameNotFoundException("Correo inválido");
        }

        String correoTrimmed = correo.trim();

        // Usamos búsqueda case-insensitive primero, luego fallback a la búsqueda exacta
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(correoTrimmed)
                .or(() -> usuarioRepository.findByCorreo(correoTrimmed))
                .orElseThrow(() -> {
                    logger.warn("Usuario no encontrado para correo={}", correoTrimmed);
                    return new UsernameNotFoundException("Usuario no encontrado: " + correoTrimmed);
                });

        String rol = usuario.getRol();
        if (rol == null || rol.isBlank()) {
            rol = "USER"; // rol por defecto si no está en la base
        } else {
            rol = rol.trim().toUpperCase();
        }

        if (usuario.getPassword() == null) {
            logger.error("Usuario encontrado pero sin contraseña (id={}, correo={})", usuario.getId(), usuario.getCorreo());
            throw new UsernameNotFoundException("Usuario sin contraseña: " + usuario.getCorreo());
        }

        logger.debug("Usuario encontrado: id={}, correo={}, rol={}", usuario.getId(), usuario.getCorreo(), rol);

        return new org.springframework.security.core.userdetails.User(
                usuario.getCorreo().trim(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol))
        );
    }
}
