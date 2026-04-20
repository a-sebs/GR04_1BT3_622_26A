package com.skillswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

    private static final Map<Long, Notificacion> REGISTRO_MEMORIA = new ConcurrentHashMap<>();

    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 300)
    private String mensaje;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fechaPropuesta;

    @Column(nullable = false)
    private boolean leida;

    public Notificacion() {
        // Constructor requerido por JPA
    }

    public Notificacion(Long id, String mensaje, Date fechaPropuesta, boolean leida) {
        this.id = id;
        this.mensaje = mensaje;
        this.fechaPropuesta = fechaPropuesta;
        this.leida = leida;
        registrarEnMemoria();
    }

    @PrePersist
    void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        }
        if (fechaPropuesta == null) {
            fechaPropuesta = new Date();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        registrarEnMemoria();
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaPropuesta() {
        return fechaPropuesta;
    }

    public void setFechaPropuesta(Date fechaPropuesta) {
        this.fechaPropuesta = fechaPropuesta;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public void marcarLeida() {
        this.leida = true;
    }

    public static Notificacion buscarEnMemoria(Long id) {
        return REGISTRO_MEMORIA.get(id);
    }

    public static List<Notificacion> listarPendientesEnMemoria() {
        List<Notificacion> pendientes = new ArrayList<>();
        for (Notificacion notificacion : REGISTRO_MEMORIA.values()) {
            if (!notificacion.isLeida()) {
                pendientes.add(notificacion);
            }
        }
        return pendientes;
    }

    private void registrarEnMemoria() {
        if (id != null) {
            REGISTRO_MEMORIA.put(id, this);
        }
    }
}

