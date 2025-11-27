package com.example.penny_juice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPedido")
    private Integer idPedido;

    @Column(name = "IdUsuario", nullable = false)
    private Long idUsuario;

    @Column(name = "IdTarjeta")
    private Integer idTarjeta;

    @Column(name = "Total", nullable = false)
    private BigDecimal total;

    @Column(name = "DireccionEnvio")
    private String direccionEnvio;

    @Column(name = "FechaPedido")
    private LocalDateTime fechaPedido;

    @Column(name = "Estado")
    private String estado;

    public Integer getIdPedido() {
        return idPedido;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public String getEstado() {
        return estado;
    }
}
