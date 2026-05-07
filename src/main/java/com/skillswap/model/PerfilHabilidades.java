package com.skillswap.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "perfiles_habilidades")
@Getter
@Setter
@NoArgsConstructor
public class PerfilHabilidades {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(optional = false)
	@JoinColumn(name = "usuario_id", nullable = false, unique = true)
	private Usuario usuario;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "perfil_habilidades_ofrece", joinColumns = @JoinColumn(name = "perfil_id"))
	@Column(name = "habilidad", nullable = false, length = 100)
	private Set<String> habilidadesOfrece = new LinkedHashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "perfil_habilidades_busca", joinColumns = @JoinColumn(name = "perfil_id"))
	@Column(name = "habilidad", nullable = false, length = 100)
	private Set<String> habilidadesBusca = new LinkedHashSet<>();

public boolean coincideConNombreUsuario(String filtroNombreUsuario) {
    // Validación: si no hay filtro, todos coinciden
    if (filtroNombreUsuario == null || filtroNombreUsuario.isBlank()) {
        return true;
    }

    // Validación: si el perfil o usuario es nulo, no coincide
    if (this.usuario == null || this.usuario.getNombre() == null) {
        return false;
    }

    // Comparación normalizada (case-insensitive y trim)
    String filtroNormalizado = filtroNombreUsuario.trim().toLowerCase();
    return this.usuario.getNombre().toLowerCase().contains(filtroNormalizado);
}	@Column(nullable = false)
	private Float reputacion = 0.0f;

	public void agregar(Collection<String> ofrece, Collection<String> busca) {
		Set<String> copiaOfrece = new LinkedHashSet<>();
		Set<String> copiaBusca = new LinkedHashSet<>();
		agregarHabilidades(copiaOfrece, ofrece);
		agregarHabilidades(copiaBusca, busca);

		habilidadesOfrece.clear();
		habilidadesBusca.clear();
		habilidadesOfrece.addAll(copiaOfrece);
		habilidadesBusca.addAll(copiaBusca);
	}

	public void agregar() {
		agregar(habilidadesOfrece, habilidadesBusca);
	}

	public void editar(Collection<String> ofrece, Collection<String> busca) {
		validarHabilidadesRequeridas(ofrece, busca);
		agregar(ofrece, busca);
	}

	public void editar() {
		agregar(habilidadesOfrece, habilidadesBusca);
	}

	public void eliminar() {
		habilidadesOfrece.clear();
		habilidadesBusca.clear();
	}

	private void agregarHabilidades(Set<String> destino, Collection<String> origen) {
		if (origen == null) {
			return;
		}
		for (String habilidad : origen) {
			if (habilidad != null && !habilidad.isBlank()) {
				destino.add(habilidad.trim());
			}
		}
	}

	private void validarHabilidadesRequeridas(Collection<String> ofrece, Collection<String> busca) {
		if (ofrece == null || ofrece.isEmpty() || busca == null || busca.isEmpty()) {
			throw new IllegalArgumentException("Debe seleccionar al menos una habilidad en cada área");
		}
	}

	public boolean coincideConFiltro(String filtro) {
		if (filtro == null || filtro.isBlank()) {
			return true;
		}
		String filtroNormalizado = filtro.trim().toLowerCase();
		return habilidadesOfrece.stream().anyMatch(h -> h.toLowerCase().contains(filtroNormalizado))
				|| habilidadesBusca.stream().anyMatch(h -> h.toLowerCase().contains(filtroNormalizado));
	}
}
