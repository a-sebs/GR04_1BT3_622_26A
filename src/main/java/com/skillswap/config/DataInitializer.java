package com.skillswap.config;

import com.skillswap.model.Match;
import com.skillswap.model.PerfilHabilidades;
import com.skillswap.model.Usuario;
import com.skillswap.repository.MatchRepository;
import com.skillswap.repository.PerfilHabilidadesRepository;
import com.skillswap.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PerfilHabilidadesRepository perfilHabilidadesRepository;
    private final MatchRepository matchRepository;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           PerfilHabilidadesRepository perfilHabilidadesRepository,
                           MatchRepository matchRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilHabilidadesRepository = perfilHabilidadesRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Limpiar datos anteriores para garantizar estado limpio
        matchRepository.deleteAll();
        perfilHabilidadesRepository.deleteAll();
        usuarioRepository.deleteAll();

        Usuario usuarioBase = crearUsuario("demoUser", "demoPass1", "demo@skillswap.com");
        Usuario ana = crearUsuario("anaDev", "demoPass2", "ana@skillswap.com");
        Usuario luis = crearUsuario("luisData", "demoPass3", "luis@skillswap.com");
        Usuario maria = crearUsuario("mariaWeb", "demoPass4", "maria@skillswap.com");

        usuarioRepository.saveAll(List.of(usuarioBase, ana, luis, maria));

        perfilHabilidadesRepository.save(crearPerfil(usuarioBase,
                List.of("Java", "Spring Boot"),
                List.of("Python", "SQL"),
                4.4f));
        perfilHabilidadesRepository.save(crearPerfil(ana,
                List.of("Python", "SQL"),
                List.of("Java", "Git"),
                4.8f));
        perfilHabilidadesRepository.save(crearPerfil(luis,
                List.of("JavaScript", "HTML", "CSS"),
                List.of("Spring Boot", "Java"),
                4.2f));
        perfilHabilidadesRepository.save(crearPerfil(maria,
                List.of("Git", "Java"),
                List.of("CSS", "JavaScript"),
                4.6f));

        matchRepository.save(crearMatch(usuarioBase, ana, 0.90f));
        matchRepository.save(crearMatch(usuarioBase, luis, 0.65f));
        matchRepository.save(crearMatch(usuarioBase, maria, 0.75f));
    }

    private Usuario crearUsuario(String nombre, String password, String correo) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setPassword(password);
        usuario.setCorreo(correo);
        usuario.registrar();
        return usuario;
    }

    private PerfilHabilidades crearPerfil(Usuario usuario,
                                          List<String> habilidadesOfrece,
                                          List<String> habilidadesBusca,
                                          float reputacion) {
        PerfilHabilidades perfil = new PerfilHabilidades();
        perfil.setUsuario(usuario);
        perfil.agregar(habilidadesOfrece, habilidadesBusca);
        perfil.setReputacion(reputacion);
        return perfil;
    }

    private Match crearMatch(Usuario solicitante, Usuario usuarioMatch, float compatibilidad) {
        Match match = new Match();
        match.setUsuarioSolicitante(solicitante);
        match.setUsuarioMatch(usuarioMatch);
        match.setCompatibilidad(compatibilidad);
        match.setEstado("GENERADO");
        return match;
    }
}

