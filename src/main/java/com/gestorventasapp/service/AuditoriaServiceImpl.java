package com.gestorventasapp.service;

import com.gestorventasapp.dao.AuditoriaDAO;
import com.gestorventasapp.model.Auditoria;
import com.gestorventasapp.exceptions.ServiceException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación de la lógica de negocio para la entidad Auditoria. Permite
 * registrar y consultar eventos relevantes del sistema.
 */
public class AuditoriaServiceImpl implements AuditoriaService {

	private final AuditoriaDAO auditoriaDAO;

	/**
	 * Constructor con inyección de dependencia.
	 * 
	 * @param auditoriaDAO DAO de auditorías.
	 */
	public AuditoriaServiceImpl(AuditoriaDAO auditoriaDAO) {
		this.auditoriaDAO = auditoriaDAO;
	}

	@Override
	public void registrarAuditoria(Auditoria auditoria) {
		if (auditoria == null)
			throw new ServiceException("El registro de auditoría no puede ser nulo.");
		auditoriaDAO.save(auditoria);
	}

	@Override
	public Auditoria buscarPorId(int idAuditoria) {
		Auditoria registro = auditoriaDAO.findById(idAuditoria);
		if (registro == null)
			throw new ServiceException("No existe registro de auditoría con ese ID.");
		return registro;
	}

	@Override
	public List<Auditoria> listarTodas() {
		return auditoriaDAO.findAll();
	}

	@Override
	public List<Auditoria> buscarPorUsuario(String usuario) {
		if (usuario == null || usuario.trim().isEmpty())
			throw new ServiceException("El usuario no puede estar vacío.");
		return auditoriaDAO.findByUsuario(usuario.trim());
	}

	@Override
	public List<Auditoria> buscarPorAccion(String accion) {
		if (accion == null || accion.trim().isEmpty())
			throw new ServiceException("La acción no puede estar vacía.");
		return auditoriaDAO.findByAccion(accion.trim());
	}

	@Override
	public List<Auditoria> buscarPorEntidad(String entidad) {
		if (entidad == null || entidad.trim().isEmpty())
			throw new ServiceException("La entidad no puede estar vacía.");
		return auditoriaDAO.findByEntidad(entidad.trim());
	}

	@Override
	public List<Auditoria> buscarPorFechaHoraRango(LocalDateTime desde, LocalDateTime hasta) {
		if (desde == null || hasta == null)
			throw new ServiceException("Las fechas no pueden ser nulas.");
		if (hasta.isBefore(desde))
			throw new ServiceException("La fecha/hora de fin no puede ser anterior a la de inicio.");
		return auditoriaDAO.findByFechaHoraRango(desde, hasta);
	}

	@Override
	public List<Auditoria> buscarPorDescripcionLike(String descripcion) {
		if (descripcion == null || descripcion.trim().isEmpty())
			throw new ServiceException("La descripción a buscar no puede estar vacía.");
		return auditoriaDAO.findByDescripcionLike(descripcion.trim());
	}
}
