package com.example.penny_juice.repository;

import com.example.penny_juice.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
	java.util.Optional<Producto> findByNombreIgnoreCase(String nombre);

}
