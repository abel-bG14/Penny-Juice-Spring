package com.example.penny_juice.service;

import com.example.penny_juice.dto.PedidoCreateDTO;
import com.example.penny_juice.dto.PedidoDTO;
import com.example.penny_juice.model.DetallePedido;
import com.example.penny_juice.model.Pedido;
import com.example.penny_juice.model.Producto;
import com.example.penny_juice.repository.DetallePedidoRepository;
import com.example.penny_juice.repository.PedidoRepository;
import com.example.penny_juice.repository.ProductoRepository;
import com.example.penny_juice.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Pedido> listarTodasEntidades() {
        return pedidoRepository.findAll();
    }


    // crear pedido
    public PedidoDTO crearPedido(PedidoCreateDTO dto) {
        if (dto == null) throw new IllegalArgumentException("PedidoCreateDTO es nulo");
        if (dto.IdUsuario() == null) throw new IllegalArgumentException("IdUsuario no puede ser nulo");
    
        Pedido pedido = Pedido.builder()
                .estado("pendiente")
                .idUsuario(usuarioRepository.findById(dto.IdUsuario())
                        .orElseThrow(() -> new RuntimeException("No existe un usuario con ese id")))
                .fechaPedido(new Date())
                .total(0.0)
                .build();
    
        pedido = pedidoRepository.save(pedido);
    
        List<DetallePedido> detalles = new ArrayList<>();
        double total = 0.0;
    
        for (var det : dto.detalles()) {
            System.out.println("ID PRODUCTO LLEGÓ: " + det.idProducto());
            Producto producto = productoRepository.findById(det.idProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    
            double subtotal = producto.getPrecio() * det.cantidad();
    
            DetallePedido detalle = DetallePedido.builder()
                    .idPedido(pedido)
                    .idProducto(producto)
                    .cantidad(det.cantidad())
                    .subtotal(subtotal)
                    .build();
    
            detallePedidoRepository.save(detalle);
            detalles.add(detalle);
    
            total += subtotal;
    
            producto.setStock(producto.getStock() - det.cantidad());
            productoRepository.save(producto);
        }
    
        pedido.setTotal(total);
        pedidoRepository.save(pedido);
    
        return new PedidoDTO(pedido, detalles);
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public java.util.List<DetallePedido> obtenerDetallesPorPedidoId(Long idPedido) {
        java.util.List<DetallePedido> result = new java.util.ArrayList<>();
        for (DetallePedido d : detallePedidoRepository.findAll()) {
            if (d.getIdPedido() != null && d.getIdPedido().getIdPedido() != null && d.getIdPedido().getIdPedido().equals(idPedido)) {
                result.add(d);
            }
        }
        return result;
    }

    public void eliminarPedido(Long idPedido) {
        Pedido pedido = obtenerPorId(idPedido);

        // Restaurar stock y borrar detalles asociados
        java.util.List<DetallePedido> detalles = obtenerDetallesPorPedidoId(idPedido);
        for (DetallePedido d : detalles) {
            Producto p = d.getIdProducto();
            if (p != null) {
                Integer current = p.getStock() == null ? 0 : p.getStock();
                p.setStock(current + (d.getCantidad() == null ? 0 : d.getCantidad()));
                productoRepository.save(p);
            }
            detallePedidoRepository.delete(d);
        }

        pedidoRepository.delete(pedido);
    }

    public void actualizarEstado(Long idPedido, String nuevoEstado) {
        Pedido pedido = obtenerPorId(idPedido);
        pedido.setEstado(nuevoEstado);
        pedidoRepository.save(pedido);
    }

}