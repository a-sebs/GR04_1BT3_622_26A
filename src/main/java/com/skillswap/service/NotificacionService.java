package com.skillswap.service;

import com.skillswap.model.Notificacion;
import com.skillswap.repository.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public List<Notificacion> solicitarLista() {
        if (notificacionRepository == null) {
            return Collections.emptyList();
        }

        return obtenerFuenteNotificaciones().stream()
                .filter(notificacion -> !notificacion.isEstadoLectura())
                .toList();
    }

    public void marcarLeida(Long idNotificacion) {
        if (idNotificacion == null) {
            return;
        }

        if (idNotificacion > Integer.MAX_VALUE || idNotificacion < Integer.MIN_VALUE) {
            return;
        }

        marcarLeidaEnRepositorio(idNotificacion.intValue());
    }

    private List<Notificacion> obtenerFuenteNotificaciones() {
        return notificacionRepository.findAll();
    }

    private void marcarLeidaEnRepositorio(Integer idNotificacion) {
        if (notificacionRepository == null) {
            return;
        }
        notificacionRepository.findById(idNotificacion).ifPresent(notificacion -> {
            notificacion.setEstadoLectura(true);
            notificacionRepository.save(notificacion);
        });
    }
}


