package com.skillswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bloqueos")
public class Bloqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBloqueo;

    @Column(nullable = false)
    private Long bloqueadorId;

    @Column(nullable = false)
    private Long bloqueadoId;

    public Bloqueo() {
    }

    public Long getIdBloqueo() {
        return idBloqueo;
    }

    public void setIdBloqueo(Long idBloqueo) {
        this.idBloqueo = idBloqueo;
    }

    public Long getBloqueadorId() {
        return bloqueadorId;
    }

    public void setBloqueadorId(Long bloqueadorId) {
        this.bloqueadorId = bloqueadorId;
    }

    public Long getBloqueadoId() {
        return bloqueadoId;
    }

    public void setBloqueadoId(Long bloqueadoId) {
        this.bloqueadoId = bloqueadoId;
    }
}

