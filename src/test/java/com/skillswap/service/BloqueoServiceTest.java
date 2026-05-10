package com.skillswap.service;

import com.skillswap.model.Bloqueo;
import com.skillswap.repository.BloqueoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BloqueoServiceTest {

    private static final Long BLOQUEADOR_ID = 10L;
    private static final Long BLOQUEADO_ID = 20L;

    @Mock
    private BloqueoRepository bloqueoRepository;

    @InjectMocks
    private BloqueoService bloqueoService;

    @Test
    @DisplayName("deberiaCrearRegistroCuandoIdsSonDistintos")
    void deberiaCrearRegistroCuandoIdsSonDistintos() {
        // Arrange: simulamos persistencia para inspeccionar el objeto guardado
        when(bloqueoRepository.save(any(Bloqueo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        ArgumentCaptor<Bloqueo> captor = ArgumentCaptor.forClass(Bloqueo.class);

        // Act
        bloqueoService.bloquearUsuario(BLOQUEADOR_ID, BLOQUEADO_ID);

        // Assert
        verify(bloqueoRepository, times(1)).save(captor.capture());
        Bloqueo bloqueo = captor.getValue();
        assertEquals(BLOQUEADOR_ID, bloqueo.getIdBloqueador());
        assertEquals(BLOQUEADO_ID, bloqueo.getIdBloqueado());
    }

    @Test
    @DisplayName("deberiaLanzarExcepcionCuandoIdsSonIguales")
    void deberiaLanzarExcepcionCuandoIdsSonIguales() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> bloqueoService.bloquearUsuario(BLOQUEADOR_ID, BLOQUEADOR_ID));
    }

    @Test
    @DisplayName("deberiaInvocarSaveCuandoBloqueoEsValido")
    void deberiaInvocarSaveCuandoBloqueoEsValido() {
        // Arrange
        when(bloqueoRepository.save(any(Bloqueo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act + Assert
        assertDoesNotThrow(() -> bloqueoService.bloquearUsuario(BLOQUEADOR_ID, BLOQUEADO_ID));
        verify(bloqueoRepository, times(1)).save(any(Bloqueo.class));
    }

    @Test
    @DisplayName("deberiaNoInvocarSaveCuandoBloqueoEsInvalido")
    void deberiaNoInvocarSaveCuandoBloqueoEsInvalido() {
        // Act
        assertThrows(IllegalArgumentException.class,
                () -> bloqueoService.bloquearUsuario(BLOQUEADOR_ID, BLOQUEADOR_ID));

        // Assert
        verify(bloqueoRepository, never()).save(any(Bloqueo.class));
    }
}

