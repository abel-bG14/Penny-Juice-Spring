package com.example.penny_juice.service;

import com.example.penny_juice.model.Producto;
import com.example.penny_juice.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> listarProducto() {
        return productoRepository.findAll();
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un producto con ese id"));
    }

    public Producto editarProducto(Long id, Producto producto) {
        Producto productoExistente = obtenerPorId(id);
        productoExistente.update(producto);
        return productoRepository.save(productoExistente);
    }

    public void eliminarProducto(Long id) {
        obtenerPorId(id);
        productoRepository.deleteById(id);
    }
}
