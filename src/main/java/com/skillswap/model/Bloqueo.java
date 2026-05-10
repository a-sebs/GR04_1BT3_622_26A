package com.skillswap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bloqueos")
@Getter
@Setter
@NoArgsConstructor
public class Bloqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_bloqueador", nullable = false)
    private Long idBloqueador;

    @Column(name = "id_bloqueado", nullable = false)
    private Long idBloqueado;

    public Bloqueo(Long idBloqueador, Long idBloqueado) {
        this.idBloqueador = idBloqueador;
        this.idBloqueado = idBloqueado;
    }
}
