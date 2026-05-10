package com.skillswap.controller;

import com.skillswap.service.BloqueoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.servlet.http.HttpSession;

@Controller
public class BloqueoController {

    private final BloqueoService bloqueoService;

    public BloqueoController(BloqueoService bloqueoService) {
        this.bloqueoService = bloqueoService;
    }

    @PostMapping("/bloqueo/aplicar")
    public String aplicarBloqueo(@RequestParam("idBloqueado") Long idBloqueado, 
                                 HttpSession session, 
                                 RedirectAttributes redirectAttributes) {
        Object sessionUser = session.getAttribute("usuarioId");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        
        Long idBloqueador;
        if (sessionUser instanceof Number valorNumerico) {
            idBloqueador = valorNumerico.longValue();
        } else {
            return "redirect:/login";
        }

        try {
            bloqueoService.bloquearUsuario(idBloqueador, idBloqueado);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario bloqueado exitosamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            redirectAttributes.addFlashAttribute("error", "No se pudo registrar el bloqueo.");
        }

        return "redirect:/match/explorar";
    }
}
