package com.skillswap.controller;

import com.skillswap.model.Match;
import com.skillswap.model.Sesion;
import com.skillswap.model.Usuario;
import com.skillswap.repository.MatchRepository;
import com.skillswap.repository.SesionRepository;
import com.skillswap.repository.UsuarioRepository;
import com.skillswap.service.ValidadorDisponibilidad;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class SesionControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private SesionRepository sesionRepository;

    @Mock
    private ValidadorDisponibilidad validadorDisponibilidad;

    @Mock
    private HttpSession httpSession;

    @Mock
    private RedirectAttributes redirectAttributes;

    private SesionController sesionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sesionController = new SesionController(
                usuarioRepository,
                matchRepository,
                sesionRepository,
                validadorDisponibilidad
        );
    }

    @Test
    @DisplayName("Should accept session and return confirmation view with session ID")
    void should_accept_session_request_and_redirect() {
        Sesion sesion = crear_sesion("sesion-001", "123", new Date());
        Match match = crear_match(123L, crear_usuario(1L, "Juan"));

        configurar_aceptar_sesion("sesion-001", sesion, match);

        String resultado = sesionController.aceptarSesion("sesion-001", httpSession, redirectAttributes);

        assertThat(resultado).contains("sesion-001");
    }

    @Test
    @DisplayName("Should redirect to match list when sesion does not exist")
    void should_redirect_to_match_list_when_sesion_does_not_exist() {
        when(httpSession.getAttribute("usuarioId")).thenReturn(1L);
        when(sesionRepository.findById("sesion-invalid")).thenReturn(Optional.empty());

        String resultado = sesionController.aceptarSesion("sesion-invalid", httpSession, redirectAttributes);

        assertThat(resultado).isEqualTo("redirect:/match/lista");
    }

    @Test
    @DisplayName("Should accept session request and update status to ACEPTADA")
    void should_accept_session_request_when_decision_is_true() {
        procesarDecision("sesion-001", true, "ACEPTADA");
    }

    @Test
    @DisplayName("Should reject session request and update status to RECHAZADA")
    void should_reject_session_request_when_decision_is_false() {
        procesarDecision("sesion-002", false, "RECHAZADA");
    }



    // ========== HELPERS ==========

    private Usuario crear_usuario(Long id, String nombre) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre(nombre);
        return usuario;
    }

    private Sesion crear_sesion(String id, String idMatch, Date fecha) {
        Sesion sesion = new Sesion();
        sesion.setId(id);
        sesion.setIdMatch(idMatch);
        sesion.setFecha(fecha);
        return sesion;
    }

    private Match crear_match(Long id, Usuario solicitante) {
        Match match = new Match();
        match.setId(id);
        match.setUsuarioSolicitante(solicitante);
        return match;
    }

    private void configurar_aceptar_sesion(String sesionId, Sesion sesion, Match match) {
        when(httpSession.getAttribute("usuarioId")).thenReturn(1L);
        when(sesionRepository.findById(sesionId)).thenReturn(Optional.of(sesion));
        when(matchRepository.findById(match.getId())).thenReturn(Optional.of(match));
        when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);
    }

    private void procesarDecision(String sesionId, Boolean decision, String estadoEsperado) {
        Sesion sesion = crear_sesion(sesionId, "123", new Date());
        sesion.setEstado("PENDIENTE");

        when(sesionRepository.findById(sesionId)).thenReturn(Optional.of(sesion));
        when(sesionRepository.save(any(Sesion.class))).thenReturn(sesion);

        sesionController.procesarSolicitud(sesionId, decision);

        ArgumentCaptor<Sesion> captor = ArgumentCaptor.forClass(Sesion.class);
        verify(sesionRepository).save(captor.capture());
        assertEquals(estadoEsperado, captor.getValue().getEstado());
    }
}
