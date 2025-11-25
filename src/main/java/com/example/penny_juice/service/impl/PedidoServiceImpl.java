package com.example.penny_juice.service.impl;

import com.example.penny_juice.dto.VentaView;
import com.example.penny_juice.model.DetallePedido;
import com.example.penny_juice.model.Pedido;
import com.example.penny_juice.model.Producto;
import com.example.penny_juice.model.Usuario;
import com.example.penny_juice.repository.DetallePedidoRepository;
import com.example.penny_juice.repository.PedidoRepository;
import com.example.penny_juice.repository.ProductoRepository;
import com.example.penny_juice.repository.UsuarioRepository;
import com.example.penny_juice.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Pedido> listarTodasEntidades() {
        return pedidoRepository.findAll();
    }

    @Override
    public List<VentaView> listarTodas() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        return pedidos.stream().map(p -> {
            String cliente = usuarioRepository.findById(p.getIdUsuario())
                    .map(u -> u.getNombre() + " " + u.getApellidos())
                    .orElse("Usuario #" + p.getIdUsuario());

            List<DetallePedido> detalles = detallePedidoRepository.findByIdPedido(p.getIdPedido());

            String productos = detalles.stream().map(d -> {
                Producto prod = productoRepository.findById(d.getIdProducto().longValue()).orElse(null);
                String nombre = prod != null ? prod.getNombre() : "Producto #" + d.getIdProducto();
                return (d.getCantidad() != null ? d.getCantidad() : 1) + " x " + nombre;
            }).collect(Collectors.joining(", "));

            return new VentaView(p.getIdPedido(), cliente, productos, p.getTotal(), p.getFechaPedido(), p.getEstado());
        }).collect(Collectors.toList());
    }
}
