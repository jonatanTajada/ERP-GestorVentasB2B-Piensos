package com.gestorventasapp.controller;

import com.gestorventasapp.model.Auditoria;
import com.gestorventasapp.service.AuditoriaService;
import com.gestorventasapp.exceptions.ControllerException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para el registro y consulta de auditorías (logs). Valida la
 * entrada y canaliza las operaciones a la capa de servicio.
 */
public class AuditoriaController {

	private final AuditoriaService auditoriaService;

	public AuditoriaController(AuditoriaService auditoriaService) {
		this.auditoriaService = auditoriaService;
	}

	public void registrarAuditoria(Auditoria auditoria) {
		if (auditoria == null)
			throw new ControllerException("El objeto auditoria no puede ser nulo.");
		try {
			auditoriaService.registrarAuditoria(auditoria);
		} catch (Exception e) {
			throw new ControllerException("Error al registrar auditoría: " + e.getMessage(), e);
		}
	}

	public Auditoria buscarPorId(int idAuditoria) {
		if (idAuditoria <= 0)
			throw new ControllerException("El ID debe ser mayor que cero.");
		try {
			return auditoriaService.buscarPorId(idAuditoria);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar auditoría: " + e.getMessage(), e);
		}
	}

	public List<Auditoria> listarTodas() {
		try {
			return auditoriaService.listarTodas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar auditorías: " + e.getMessage(), e);
		}
	}

	public List<Auditoria> buscarPorUsuario(String usuario) {
		if (usuario == null || usuario.trim().isEmpty())
			throw new ControllerException("El usuario no puede estar vacío.");
		try {
			return auditoriaService.buscarPorUsuario(usuario.trim());
		} catch (Exception e) {
			throw new ControllerException("Error al buscar auditorías por usuario: " + e.getMessage(), e);
		}
	}

	public List<Auditoria> buscarPorAccion(String accion) {
		if (accion == null || accion.trim().isEmpty())
			throw new ControllerException("La acción no puede estar vacía.");
		try {
			return auditoriaService.buscarPorAccion(accion.trim());
		} catch (Exception e) {
			throw new ControllerException("Error al buscar auditorías por acción: " + e.getMessage(), e);
		}
	}

	public List<Auditoria> buscarPorEntidad(String entidad) {
		if (entidad == null || entidad.trim().isEmpty())
			throw new ControllerException("La entidad no puede estar vacía.");
		try {
			return auditoriaService.buscarPorEntidad(entidad.trim());
		} catch (Exception e) {
			throw new ControllerException("Error al buscar auditorías por entidad: " + e.getMessage(), e);
		}
	}

	public List<Auditoria> buscarPorFechaHoraRango(LocalDateTime desde, LocalDateTime hasta) {
		if (desde == null || hasta == null)
			throw new ControllerException("Debes indicar el rango de fechas.");
		if (desde.isAfter(hasta))
			throw new ControllerException("La fecha de inicio no puede ser posterior a la de fin.");
		try {
			return auditoriaService.buscarPorFechaHoraRango(desde, hasta);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar auditorías por rango de fechas: " + e.getMessage(), e);
		}
	}

	public List<Auditoria> buscarPorDescripcionLike(String descripcion) {
		if (descripcion == null || descripcion.trim().isEmpty())
			throw new ControllerException("La descripción no puede estar vacía.");
		try {
			return auditoriaService.buscarPorDescripcionLike(descripcion.trim());
		} catch (Exception e) {
			throw new ControllerException("Error al buscar auditorías por descripción: " + e.getMessage(), e);
		}
	}
}
