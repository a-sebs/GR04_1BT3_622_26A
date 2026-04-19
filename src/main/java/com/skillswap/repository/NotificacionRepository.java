package com.skillswap.repository;

import com.skillswap.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    long countByUsuarioDestinoIdAndLeidaFalse(Long usuarioDestinoId);

    List<Notificacion> findByUsuarioDestinoIdAndLeidaFalse(Long usuarioDestinoId);

}

