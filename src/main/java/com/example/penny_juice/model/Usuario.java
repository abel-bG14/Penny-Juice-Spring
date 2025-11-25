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
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUsuario")
    private Long id;

    @Column(name = "Nombres")
    private String nombre;

    @Column(name = "Apellidos")
    private String apellidos;

    @Column(name = "FechaNacimiento")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;

    @Column(name = "Telefono")
    private String telefono;

    @Column(name = "Correo")
    private String correo;

    @Column(name = "ContrasenaHash")
    private String password;

    @Column(name = "FechaRegistro")
    private Date fechaRegistro;

    @Column(name = "Rol")
    private String rol;

    // Métodos explícitos para asegurar disponibilidad en tiempo de compilación
    public String getCorreo() {
        return this.correo;
    }

    public Long getId() { return this.id; }

    public Date getFechaNacimiento() { return this.fechaNacimiento; }

    public String getTelefono() { return this.telefono; }

    public String getPassword() {
        return this.password;
    }

    public Date getFechaRegistro() {
        return this.fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getRol() {
        return this.rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombre() { return this.nombre; }
    public String getApellidos() { return this.apellidos; }

    // Setters explícitos para asegurar el data binding en los formularios
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setPassword(String password) { this.password = password; }
}
