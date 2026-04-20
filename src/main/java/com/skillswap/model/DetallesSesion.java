package com.skillswap.model;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
public class DetallesSesion {

	private List<String> detalles = List.of();
	private boolean opcionAceptarSeleccionada;
	private boolean accionConfirmada;
	private boolean decisionConfirmada;

	public void desplegarDetallesSesion(Sesion detalles) {
		if (detalles == null) {
			this.detalles = List.of();
			return;
		}

		List<String> valores = new ArrayList<>();
		valores.add("ID Match: " + detalles.getIdMatch());
		if (detalles.getFecha() != null) {
			String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(detalles.getFecha());
			valores.add("Fecha: " + fecha);
		}
		valores.add("Hora: " + (detalles.getHora() == null ? "" : detalles.getHora()));
		valores.add("Estado: " + (detalles.getEstado() == null ? "" : detalles.getEstado()));
		this.detalles = List.copyOf(valores);
	}

	public void desplegarDetallesSesion(List<String> detalles) {
		this.detalles = detalles == null ? List.of() : List.copyOf(detalles);
	}

	public void seleccionarOpcionAceptar() {
		opcionAceptarSeleccionada = true;
	}

	public void confirmarAccion() {
		accionConfirmada = opcionAceptarSeleccionada;
	}

	public void confirmarDecision() {
		decisionConfirmada = accionConfirmada;
	}

	public String mostrarMensajeExito() {
		if (decisionConfirmada) {
			return "Sesion finalizada correctamente.";
		}
		return "No se pudo confirmar la accion para la sesion.";
	}
}

