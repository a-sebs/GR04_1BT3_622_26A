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
import java.util.regex.Pattern;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    public static final String ERR_PASSWORD_CORTA = "La contraseña tiene 8 caracteres mínimo";

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
        validarCredenciales(nombre, password, correo);
        nombre = nombre.trim();
        correo = correo.trim().toLowerCase();
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

        if (!hayNuevoNombre && !hayNuevoPassword && !hayNuevoCorreo) {
            throw new IllegalArgumentException("Debe enviar al menos un dato para actualizar.");
        }

        if (hayNuevoNombre) {
            this.nombre = nuevoNombre.trim();
        }
        if (hayNuevoPassword) {
            this.password = nuevoPassword;
        }
        if (hayNuevoCorreo) {
            this.correo = nuevoCorreo.trim().toLowerCase();
        }

        validarCredenciales(this.nombre, this.password, this.correo);
    }

    private void validarCredenciales(String nombre, String password, String correo) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (!USERNAME_PATTERN.matcher(nombre.trim()).matches()) {
            throw new IllegalArgumentException("Error al validar el nombre de usuario, no ingrese caracteres especiales");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("La contraseña tiene 8 caracteres mínimo");
        }
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        if (!EMAIL_PATTERN.matcher(correo.trim()).matches()) {
            throw new IllegalArgumentException("El correo debe seguir el formato: correo@dominio.com");
        }
    }
}
