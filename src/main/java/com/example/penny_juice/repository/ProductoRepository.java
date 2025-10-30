package com.example.penny_juice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.penny_juice.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
