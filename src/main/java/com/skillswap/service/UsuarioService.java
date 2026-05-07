package com.skillswap.service;

import com.skillswap.model.Usuario;
import com.skillswap.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void actualizarDatos(Long id, String nuevoNombre, String nuevoCorreo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.actualizarDatos(nuevoNombre, null, nuevoCorreo);
        usuarioRepository.save(usuario);
    }
}

