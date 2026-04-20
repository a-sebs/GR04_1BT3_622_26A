package com.skillswap.service;

import com.skillswap.model.Notificacion;
import com.skillswap.repository.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService() {
        this(null);
    }

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public List<Notificacion> solicitarLista() {
        return obtenerFuenteNotificaciones().stream()
                .filter(notificacion -> !notificacion.isLeida())
                .toList();
    }

    public void marcarLeida(Long idNotificacion) {
        if (idNotificacion == null) {
            return;
        }

        marcarLeidaEnRepositorio(idNotificacion);
        marcarLeidaEnMemoria(idNotificacion);
    }

    private List<Notificacion> obtenerFuenteNotificaciones() {
        if (notificacionRepository == null) {
            return Notificacion.listarPendientesEnMemoria();
        }
        return notificacionRepository.findAll();
    }

    private void marcarLeidaEnRepositorio(Long idNotificacion) {
        if (notificacionRepository == null) {
            return;
        }
        notificacionRepository.findById(idNotificacion).ifPresent(notificacion -> {
            notificacion.marcarLeida();
            notificacionRepository.save(notificacion);
        });
    }

    private void marcarLeidaEnMemoria(Long idNotificacion) {
        Notificacion notificacionEnMemoria = Notificacion.buscarEnMemoria(idNotificacion);
        if (notificacionEnMemoria == null) {
            return;
        }
        notificacionEnMemoria.marcarLeida();
    }
}


