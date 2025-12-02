package com.example.penny_juice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "DetallePedido")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDetalle")
    private Integer idDetalle;

    // Muchos detalles pertenecen a un solo pedido
    @ManyToOne
    @JoinColumn(name = "IdPedido", nullable = false)
    private Pedido idPedido;

    // Un detalle pertenece a un solo producto
    @ManyToOne
    @JoinColumn(name = "IdProducto", nullable = false)
    private Producto idProducto;

    @Column(name = "Cantidad")
    private Integer cantidad;

    @Column(name = "Subtotal")
    private Double subtotal;
}