package com.example.penny_juice.dto;

import com.example.penny_juice.model.DetallePedido;
import com.example.penny_juice.model.Pedido;

import java.util.Date;
import java.util.List;

public record PedidoDTO(
        Long idPedido,
        String usuario,
        Double total,
        Date fechaPedido,
        String estado,
        List<DetallesPedidoDTO> pedidoDTOList
) {
    public PedidoDTO(Pedido pedido, List<DetallePedido> detalles) {
        this(pedido.getIdPedido(), pedido.getIdUsuario().getNombre(), pedido.getTotal(), pedido.getFechaPedido(), pedido.getEstado(),
                detalles.stream()
                        .map(detalle -> new DetallesPedidoDTO(
                                detalle.getIdDetalle(),
                                detalle.getIdProducto(),
                                detalle.getCantidad(),
                                detalle.getSubtotal()
                        ))
                        .toList()
        );
    }
}
