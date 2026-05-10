package com.skillswap.service;

import com.skillswap.model.Bloqueo;
import com.skillswap.repository.BloqueoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BloqueoService {

    private final BloqueoRepository bloqueoRepository;

    public BloqueoService(BloqueoRepository bloqueoRepository) {
        this.bloqueoRepository = bloqueoRepository;
    }

    @Transactional
    public Bloqueo bloquearUsuario(Long bloqueadorId, Long bloqueadoId) {
        validarIds(bloqueadorId, bloqueadoId);

        Bloqueo bloqueo = new Bloqueo(bloqueadorId, bloqueadoId);
        return bloqueoRepository.save(bloqueo);
    }

    private void validarIds(Long bloqueadorId, Long bloqueadoId) {
        if (bloqueadorId == null || bloqueadoId == null) {
            throw new IllegalArgumentException("Los ids de usuario son obligatorios.");
        }
        if (bloqueadorId.equals(bloqueadoId)) {
            throw new IllegalArgumentException("Un usuario no puede bloquearse a si mismo.");
        }
    }
}

