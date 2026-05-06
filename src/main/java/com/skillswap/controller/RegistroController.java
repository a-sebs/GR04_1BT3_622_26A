package com.skillswap.controller;

import com.skillswap.model.PerfilHabilidades;
import com.skillswap.model.Usuario;
import com.skillswap.repository.PerfilHabilidadesRepository;
import com.skillswap.repository.UsuarioRepository;
import com.skillswap.service.UsuarioService;
import com.skillswap.service.ValidadorDatos;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/registro")
public class RegistroController {

	private static final List<String> CATALOGO_HABILIDADES = List.of(
			"Java", "Python", "JavaScript", "SQL", "Spring Boot", "HTML", "CSS", "Git"
	);

	private final UsuarioRepository usuarioRepository;
	private final PerfilHabilidadesRepository perfilHabilidadesRepository;
	private final ValidadorDatos validadorDatos;
	private final UsuarioService usuarioService;

	public RegistroController(UsuarioRepository usuarioRepository,
							  PerfilHabilidadesRepository perfilHabilidadesRepository,
							  ValidadorDatos validadorDatos,
							  UsuarioService usuarioService) {
		this.usuarioRepository = usuarioRepository;
		this.perfilHabilidadesRepository = perfilHabilidadesRepository;
		this.validadorDatos = validadorDatos;
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public String mostrarFormulario(Model model) {
		cargarAtributosBase(model);
		return "registro/formularioRegistro";
	}

	@PostMapping
	@Transactional
	public String registrarUsuario(@RequestParam("nombre") String nombre,
								   @RequestParam("password") String password,
								   @RequestParam("correo") String correo,
								   Model model,
								   RedirectAttributes redirectAttributes) {

		String nombreNormalizado = nombre.trim();
		String correoNormalizado = correo.trim().toLowerCase();

		Optional<String> errorValidacion = obtenerErrorValidacionRegistro(
				nombre,
				password,
				correo,
				nombreNormalizado,
				correoNormalizado
		);
		if (errorValidacion.isPresent()) {
			return regresarConError(model, errorValidacion.get(), nombre, correo);
		}

		Usuario usuario = crearUsuarioDesdeFormulario(nombreNormalizado, password, correoNormalizado);
		Usuario usuarioGuardado = usuarioRepository.save(usuario);

		redirectAttributes.addFlashAttribute("mensaje", "Usuario creado. Complete su perfil de habilidades.");
		return "redirect:/registro/perfil/" + usuarioGuardado.getId();
	}

	@GetMapping("/perfil/{usuarioId}")
	public String mostrarFormularioPerfil(@PathVariable Long usuarioId, Model model) {
		if (usuarioRepository.findById(usuarioId).isEmpty()) {
			model.addAttribute("error", "Usuario no encontrado.");
			cargarAtributosBase(model);
			return "registro/formularioRegistro";
		}

		PerfilHabilidades perfil = perfilHabilidadesRepository.findByUsuarioId(usuarioId).orElseGet(PerfilHabilidades::new);
		cargarAtributosPerfil(model, usuarioId, perfil);
		return "registro/formularioPerfilHabilidades";
	}

	@PostMapping("/perfil/{usuarioId}/agregar")
	@Transactional
	public String agregarPerfil(@PathVariable Long usuarioId,
							@RequestParam(value = "habilidadesOfrece", required = false) List<String> habilidadesOfrece,
							@RequestParam(value = "habilidadesBusca", required = false) List<String> habilidadesBusca,
							Model model,
							RedirectAttributes redirectAttributes) {
		return guardarPerfil(usuarioId, habilidadesOfrece, habilidadesBusca, model, redirectAttributes, true);
	}

	@PostMapping("/perfil/{usuarioId}/editar")
	@Transactional
	public String editarPerfil(@PathVariable Long usuarioId,
						   @RequestParam(value = "habilidadesOfrece", required = false) List<String> habilidadesOfrece,
						   @RequestParam(value = "habilidadesBusca", required = false) List<String> habilidadesBusca,
						   Model model,
						   RedirectAttributes redirectAttributes) {
		return guardarPerfil(usuarioId, habilidadesOfrece, habilidadesBusca, model, redirectAttributes, false);
	}
	@PostMapping("/actualizar-usuario/{usuarioId}")
	@Transactional
	public String actualizarInformacion(@PathVariable Long usuarioId,
	                                    @RequestParam("nombre") String nombre,
	                                    @RequestParam("correo") String correo,
	                                    Model model,
	                                    RedirectAttributes redirectAttributes) {

		// 3. validarInformacion(nombreUsuario, correoUsuario)
		Map<String, Object> datos = Map.of(
				"nombre", nombre,
				"correo", correo
		);
		String errorValidacion = validarDato(datos);

		if (errorValidacion != null) {
			// 4.b Información No Válida
			model.addAttribute("error", errorValidacion);
			return "registro/formularioActualizarUsuario";
		}

		try {
			// 4.a Información válida - usar servicio para actualizar
			usuarioService.actualizarDatos(usuarioId, nombre, correo);

			// 5. mostrarMensajeExito()
			redirectAttributes.addFlashAttribute("mensaje", "Información actualizada exitosamente");
			return "redirect:/registro/perfil/" + usuarioId;
		} catch (IllegalArgumentException e) {
			// Capturar excepción del servicio
			model.addAttribute("error", e.getMessage());
			return "registro/formularioActualizarUsuario";
		}
	}
	@PostMapping("/perfil/{usuarioId}/eliminar")
	@Transactional
	public String eliminarPerfil(@PathVariable Long usuarioId,
							RedirectAttributes redirectAttributes) {
		PerfilHabilidades perfil = perfilHabilidadesRepository.findByUsuarioId(usuarioId).orElse(null);
		if (perfil != null) {
			perfil.eliminar();
			perfilHabilidadesRepository.delete(perfil);
		}
		redirectAttributes.addFlashAttribute("mensaje", "Perfil de habilidades eliminado.");
		return "redirect:/registro/perfil/" + usuarioId;
	}

	public String validarDato(Map<String, Object> datos) {
		if (validadorDatos.validar(datos)) {
			return null;
		}
		List<String> errores = validadorDatos.getMensajeError();
		return errores.isEmpty() ? "Error de validación." : errores.getFirst();
	}

	public String confirmarRegistro(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("mensaje", "Bienvenido a SkillSwap, Inicie Sesión con sus credenciales");
		return "redirect:/login";
	}

	private String regresarConError(Model model,
									String error,
									String nombre,
									String correo) {
		cargarAtributosBase(model);
		model.addAttribute("error", error);
		model.addAttribute("nombre", nombre);
		model.addAttribute("correo", correo);
		return "registro/formularioRegistro";
	}

	private void cargarAtributosBase(Model model) {
		model.addAttribute("nombre", "");
		model.addAttribute("correo", "");
	}

	private String guardarPerfil(Long usuarioId,
							List<String> habilidadesOfrece,
							List<String> habilidadesBusca,
							Model model,
							RedirectAttributes redirectAttributes,
							boolean esNuevo) {
		Map<String, Object> datosHabilidades = Map.of(
				"habilidadesOfrece", habilidadesOfrece == null ? List.of() : habilidadesOfrece,
				"habilidadesBusca", habilidadesBusca == null ? List.of() : habilidadesBusca
		);
		String mensajeError = validarDato(datosHabilidades);
		if (mensajeError != null) {
			PerfilHabilidades perfilTemporal = new PerfilHabilidades();
			perfilTemporal.agregar(habilidadesOfrece, habilidadesBusca);
			cargarAtributosPerfil(model, usuarioId, perfilTemporal);
			model.addAttribute("error", mensajeError);
			return "registro/formularioPerfilHabilidades";
		}

		Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
		if (usuario == null) {
			model.addAttribute("error", "Usuario no encontrado.");
			cargarAtributosBase(model);
			return "registro/formularioRegistro";
		}

		PerfilHabilidades perfil = perfilHabilidadesRepository.findByUsuarioId(usuarioId).orElseGet(PerfilHabilidades::new);
		perfil.setUsuario(usuario);
		if (esNuevo || perfil.getId() == null) {
			perfil.agregar(habilidadesOfrece, habilidadesBusca);
		} else {
			perfil.editar(habilidadesOfrece, habilidadesBusca);
		}
		perfilHabilidadesRepository.save(perfil);

		return confirmarRegistro(redirectAttributes);
	}

	private void cargarAtributosPerfil(Model model, Long usuarioId, PerfilHabilidades perfil) {
		model.addAttribute("usuarioId", usuarioId);
		model.addAttribute("catalogoHabilidades", CATALOGO_HABILIDADES);
		model.addAttribute("habilidadesOfreceSeleccionadas", perfil.getHabilidadesOfrece());
		model.addAttribute("habilidadesBuscaSeleccionadas", perfil.getHabilidadesBusca());
	}

	private Optional<String> obtenerErrorValidacionRegistro(String nombre,
											String password,
											String correo,
											String nombreNormalizado,
											String correoNormalizado) {
		Optional<String> errorBasico = validarRegistroBasico(nombre, password, correo);
		if (errorBasico.isPresent()) {
			return errorBasico;
		}
		return validarUnicidad(nombreNormalizado, correoNormalizado);
	}

	private Optional<String> validarRegistroBasico(String nombre, String password, String correo) {
		String errorNombre = validarDato(Map.of("nombre", nombre));
		if (errorNombre != null) {
			return Optional.of(errorNombre);
		}

		String errorPassword = validarDato(Map.of("password", password));
		if (errorPassword != null) {
			return Optional.of(errorPassword);
		}

		String errorCorreo = validarDato(Map.of("correo", correo));
		return Optional.ofNullable(errorCorreo);
	}

	private Optional<String> validarUnicidad(String nombreNormalizado, String correoNormalizado) {
		if (usuarioRepository.existsByNombreIgnoreCase(nombreNormalizado)) {
			return Optional.of("El usuario ya existe en la base de datos");
		}
		if (usuarioRepository.existsByCorreoIgnoreCase(correoNormalizado)) {
			return Optional.of("El correo ya está registrado");
		}
		return Optional.empty();
	}

	private Usuario crearUsuarioDesdeFormulario(String nombre, String password, String correo) {
		Usuario usuario = new Usuario();
		usuario.setNombre(nombre);
		usuario.setPassword(password);
		usuario.setCorreo(correo);
		usuario.registrar();
		return usuario;
	}

}
