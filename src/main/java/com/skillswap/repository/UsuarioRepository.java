package com.skillswap.repository;

import com.skillswap.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	boolean existsByNombreIgnoreCase(String nombre);
	boolean existsByCorreoIgnoreCase(String correo);
	Optional<Usuario> findByNombreIgnoreCaseAndPassword(String nombre, String password);
}
