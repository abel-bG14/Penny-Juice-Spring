package com.example.penny_juice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdPedido")
    private Long idPedido;

    @ManyToOne
    @JoinColumn(name = "IdUsuario", nullable = false)
    private Usuario idUsuario;

    @Column(name = "Total", nullable = false)
    private Double total;

    @Column(name = "DireccionEnvio")
    private String direccionEnvio;

    @Column(name = "FechaPedido")
    private Date fechaPedido;

    @Column(name = "Estado")
    private String estado;

}