package com.example.penny_juice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "DetallePedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDetalle")
    private Integer idDetalle;

    @Column(name = "IdPedido", nullable = false)
    private Integer idPedido;

    @Column(name = "IdProducto", nullable = false)
    private Integer idProducto;

    @Column(name = "Cantidad")
    private Integer cantidad;

    @Column(name = "Subtotal")
    private BigDecimal subtotal;

    public Integer getIdDetalle() { return idDetalle; }
    public Integer getIdPedido() { return idPedido; }
    public Integer getIdProducto() { return idProducto; }
    public Integer getCantidad() { return cantidad; }
    public BigDecimal getSubtotal() { return subtotal; }
}
