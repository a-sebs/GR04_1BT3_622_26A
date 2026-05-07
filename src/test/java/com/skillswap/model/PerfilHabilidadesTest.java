package com.skillswap.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PerfilHabilidadesTest {

    @Test
    @DisplayName("Escenario 3: Debería lanzar excepción si se intenta editar dejando el catálogo de búsqueda vacío")
    public void editar_ConCatalogoBuscaVacio_LanzaExcepcion() {
        // Arrange
        PerfilHabilidades perfil = new PerfilHabilidades();
        Set<String> ofreceOriginal = Set.of("Java");
        Set<String> buscaOriginal = Set.of("Python");
        perfil.agregar(ofreceOriginal, buscaOriginal);

        Set<String> ofreceNuevo = Set.of("Java", "Spring");
        Set<String> buscaNuevo = Collections.emptySet(); // Catálogo de busca vacío

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            perfil.editar(ofreceNuevo, buscaNuevo);
        });

        assertEquals("Debe seleccionar al menos una habilidad en cada área", exception.getMessage());
    }

    @Test
    @DisplayName("Escenario 3: Debería lanzar excepción si se intenta editar dejando el catálogo de oferta vacío")
    public void editar_ConCatalogoOfreceVacio_LanzaExcepcion() {
        // Arrange
        PerfilHabilidades perfil = new PerfilHabilidades();
        Set<String> ofreceOriginal = Set.of("Java");
        Set<String> buscaOriginal = Set.of("Python");
        perfil.agregar(ofreceOriginal, buscaOriginal);

        Set<String> ofreceNuevo = Collections.emptySet(); // Catálogo de ofrece vacío
        Set<String> buscaNuevo = Set.of("Python", "Rust");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            perfil.editar(ofreceNuevo, buscaNuevo);
        });

        assertEquals("Debe seleccionar al menos una habilidad en cada área", exception.getMessage());
    }
}

