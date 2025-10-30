package com.example.penny_juice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.penny_juice.model.Producto;
import com.example.penny_juice.repository.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public void guardar(Producto producto) { // es lo mismo que crear
        productoRepository.save(producto);
    }

    public Producto obtenerPorId(Long id) {
        Optional<Producto> opt = productoRepository.findById(id);
        return opt.orElse(null);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

}
