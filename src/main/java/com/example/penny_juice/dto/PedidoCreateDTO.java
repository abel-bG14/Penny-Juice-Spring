package com.example.penny_juice.dto;

import java.util.List;

public record PedidoCreateDTO(Long IdUsuario, String DireccionEnvio, List<DetallePedidoCreateDTO>detalles){
}