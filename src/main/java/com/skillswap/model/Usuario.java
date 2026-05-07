package com.skillswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 120)
    private String correo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

    public void registrar() {
        this.nombre = (nombre != null) ? nombre.trim() : null;
        this.correo = (correo != null) ? correo.trim().toLowerCase() : null;

        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

    public boolean iniciarSesion(String passwordIngresada) {
        if (passwordIngresada == null || passwordIngresada.isBlank()) {
            return false;
        }
        return Objects.equals(this.password, passwordIngresada);
    }

    public void actualizarDatos(String nuevoNombre, String nuevoPassword, String nuevoCorreo) {
        boolean hayNuevoNombre = nuevoNombre != null && !nuevoNombre.isBlank();
        boolean hayNuevoPassword = nuevoPassword != null && !nuevoPassword.isBlank();
        boolean hayNuevoCorreo = nuevoCorreo != null && !nuevoCorreo.isBlank();

        if (hayNuevoNombre) {
            this.nombre = nuevoNombre.trim();
        }
        if (hayNuevoPassword) {
            this.password = nuevoPassword;
        }
        if (hayNuevoCorreo) {
            this.correo = nuevoCorreo.trim().toLowerCase();
        }
    }
}
