package com.example.penny_juice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentaView {
    private Integer idPedido;
    private String cliente;
    private String productos;
    private BigDecimal total;
    private LocalDateTime fechaPedido;
    private String estado;

    public VentaView(Integer idPedido, String cliente, String productos, BigDecimal total, LocalDateTime fechaPedido, String estado) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.productos = productos;
        this.total = total;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
    }

    public Integer getIdPedido() { return idPedido; }
    public String getCliente() { return cliente; }
    public String getProductos() { return productos; }
    public BigDecimal getTotal() { return total; }
    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public String getEstado() { return estado; }
}
