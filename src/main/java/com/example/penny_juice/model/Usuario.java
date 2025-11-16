package com.example.penny_juice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellidos;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;
    private String telefono;
    private String correo;
    private String password;
    private Date fechaRegistro;
    private String rol;
}
