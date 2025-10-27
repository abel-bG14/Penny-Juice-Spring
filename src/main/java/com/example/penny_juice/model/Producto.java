package com.example.penny_juice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Productos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproducto")
    private Long idProducto;

    @Column(name = "categoria", nullable = false, length = 100)
    private String categoria;

    @Column(name = "nombreProducto", nullable = false, length = 100)
    private String nombreProducto;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "precio",nullable = false)
    private Double precio;

    @Column(name = "imagenURL", nullable = false, length = 100)
    private String imagenUrl;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    public void update(Producto producto) {
        if (producto.getNombreProducto() != null) setNombreProducto(producto.getNombreProducto());
        if (producto.getCategoria() != null) setCategoria(producto.getCategoria());
        if (producto.getDescripcion() != null) setDescripcion(producto.getDescripcion());
        if (producto.getPrecio() != null) setPrecio(producto.getPrecio());
        if (producto.getImagenUrl() != null) setImagenUrl(producto.getImagenUrl());
        if (producto.getStock() != null) setStock(producto.getStock());
    }
}
