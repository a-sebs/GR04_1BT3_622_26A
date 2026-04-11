package com.skillswap.controller;

import com.skillswap.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SesionController {

	private final UsuarioRepository usuarioRepository;

	public SesionController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@GetMapping("/")
	public String inicio() {
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String mostrarLogin(Model model) {
		if (!model.containsAttribute("mensaje")) {
			model.addAttribute("mensaje", null);
		}
		if (!model.containsAttribute("error")) {
			model.addAttribute("error", null);
		}
		return "sesion/pantallaMatch";
	}

	@PostMapping("/login")
	public String iniciarSesion(@RequestParam("nombre") String nombre,
							 @RequestParam("password") String password,
							 HttpSession session,
							 RedirectAttributes redirectAttributes) {
		var usuario = usuarioRepository.findByNombreIgnoreCaseAndPassword(nombre.trim(), password).orElse(null);
		if (usuario == null) {
			redirectAttributes.addFlashAttribute("error", "Credenciales inválidas.");
			return "redirect:/login";
		}
		session.setAttribute("usuarioId", usuario.getId());
		session.setAttribute("usuarioNombre", usuario.getNombre());
		redirectAttributes.addFlashAttribute("mensaje", "Bienvenido " + nombre.trim() + ".");
		return "redirect:/sesion/confirmada";
	}

	@GetMapping("/sesion/confirmada")
	public String sesionConfirmada() {
		return "sesion/sesionConfirmada";
	}
}
