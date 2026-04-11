package com.skillswap.service;

import com.skillswap.model.PerfilHabilidades;
import com.skillswap.model.Usuario;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlgoritmoMatchingTest {

    private final AlgoritmoMatching algoritmoMatching = new AlgoritmoMatching();

    @Test
    void debeCalcularCompatibilidadConCoincidenciasReciprocas() {
        PerfilHabilidades actual = crearPerfil(1L, Set.of("Java", "SQL"), Set.of("Python", "Git"));
        PerfilHabilidades candidato = crearPerfil(2L, Set.of("Python", "Docker"), Set.of("SQL"));

        float compatibilidad = algoritmoMatching.calcularCompatibilidad(actual, candidato);

        assertTrue(compatibilidad > 0.0f);
    }

    @Test
    void debeOrdenarResultadosYAplicarFiltro() {
        PerfilHabilidades actual = crearPerfil(1L, Set.of("Java"), Set.of("Python"));
        PerfilHabilidades candidatoA = crearPerfil(2L, Set.of("Python"), Set.of("Java"));
        PerfilHabilidades candidatoB = crearPerfil(3L, Set.of("Excel"), Set.of("PowerBI"));

        List<AlgoritmoMatching.ResultadoCompatibilidad> resultados = algoritmoMatching
                .calcularCompatibilidades(actual, List.of(candidatoA, candidatoB), "Python");

        assertEquals(1, resultados.size());
        assertEquals(2L, resultados.getFirst().perfil().getUsuario().getId());
        assertFalse(resultados.isEmpty());
    }

    private PerfilHabilidades crearPerfil(Long idUsuario, Set<String> ofrece, Set<String> busca) {
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        usuario.setNombre("u" + idUsuario);
        usuario.setPassword("password123");
        usuario.setCorreo("u" + idUsuario + "@mail.com");

        PerfilHabilidades perfil = new PerfilHabilidades();
        perfil.setUsuario(usuario);
        perfil.agregar(ofrece, busca);
        return perfil;
    }
}

