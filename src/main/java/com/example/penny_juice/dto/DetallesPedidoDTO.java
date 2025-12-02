package com.example.penny_juice.dto;

import com.example.penny_juice.model.Producto;

public record DetallesPedidoDTO(
        Integer idDetalle,
        Producto idProducto,
        Integer cantidad,
        Double subtotal
) {
}
