package com.example.penny_juice.service;

import com.example.penny_juice.dto.VentaView;
import com.example.penny_juice.model.Pedido;

import java.util.List;

public interface PedidoService {
    List<Pedido> listarTodasEntidades();
    List<VentaView> listarTodas();
}
