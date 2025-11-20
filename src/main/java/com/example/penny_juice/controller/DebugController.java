package com.example.penny_juice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import java.util.stream.Collectors;

@RestController
public class DebugController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/debug/ping-db")
    public ResponseEntity<String> pingDb() {
        try (Connection c = dataSource.getConnection()) {
            if (c != null && !c.isClosed()) {
                return ResponseEntity.ok("DB OK: " + c.getMetaData().getURL());
            } else {
                return ResponseEntity.status(500).body("No se pudo obtener conexión a la base de datos.");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Error DB: " + ex.getMessage());
        }
    }

    @GetMapping("/debug/whoami")
    public ResponseEntity<String> whoami() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        String username = auth.getName();
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return ResponseEntity.ok("usuario=" + username + "; authorities=" + authorities);
    }
}
