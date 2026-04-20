package com.skillswap.service;

import com.skillswap.model.Notificacion;
import com.skillswap.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Transactional
    public Notificacion crearNotificacionSolicitud(Long usuarioDestinoId,
                                                   Long usuarioEmisorId,
                                                   Long referenciaSolicitudId,
                                                   String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioDestinoId(usuarioDestinoId);
        notificacion.setUsuarioEmisorId(usuarioEmisorId);
        notificacion.setReferenciaSolicitudId(referenciaSolicitudId);
        notificacion.setTipo("SOLICITUD_INTERCAMBIO");
        notificacion.setMensaje(mensaje == null ? "" : mensaje);
        notificacion.setLeida(false);
        return notificacionRepository.save(notificacion);
    }

    @Transactional(readOnly = true)
    public Long contarNotificacionesNoLeidas(Long usuarioDestinoId) {
        return notificacionRepository.countByUsuarioDestinoIdAndLeidaFalse(usuarioDestinoId);
    }

    @Transactional
    public void marcarTodasComoLeidas(Long usuarioDestinoId) {

    List<Notificacion> noLeidas = notificacionRepository.findByUsuarioDestinoIdAndLeidaFalse(usuarioDestinoId);

    // Marca todas como leídas
    noLeidas.forEach(n -> n.setLeida(true));

    // SaveAll en vez de loop individual (test 6 lo verifica)
    notificacionRepository.saveAll(noLeidas);
}
}

