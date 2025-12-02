package com.example.penny_juice.controller;

import com.example.penny_juice.model.Producto;
import com.example.penny_juice.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoApiController {

    @Autowired
    private ProductoRepository productoRepository;

    // Resolve product id by name (case-insensitive). Returns 404 if not found.
    @GetMapping("/resolve")
    public ResponseEntity<?> resolveByName(@RequestParam("nombre") String nombre) {
        Optional<Producto> p = productoRepository.findByNombreIgnoreCase(nombre);
        if (p.isPresent()) {
            return ResponseEntity.ok(java.util.Map.of("id", p.get().getId(), "nombre", p.get().getNombre()));
        }
        return ResponseEntity.notFound().build();
    }
}
