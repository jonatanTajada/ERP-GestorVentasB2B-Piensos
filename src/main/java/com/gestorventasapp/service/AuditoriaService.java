package com.gestorventasapp.service;

import com.gestorventasapp.model.Auditoria;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para el registro y consulta de auditorías (logs). Permite registrar,
 * consultar y filtrar eventos relevantes del sistema.
 */
public interface AuditoriaService {

	/**
	 * Registra una nueva acción de auditoría.
	 *
	 * @param auditoria Objeto Auditoria a registrar.
	 */
	void registrarAuditoria(Auditoria auditoria);

	/**
	 * Busca un registro de auditoría por su ID.
	 *
	 * @param idAuditoria Identificador único.
	 * @return Registro encontrado o null si no existe.
	 */
	Auditoria buscarPorId(int idAuditoria);

	/**
	 * Lista todos los registros de auditoría.
	 *
	 * @return Lista completa de logs.
	 */
	List<Auditoria> listarTodas();

	/**
	 * Busca registros de auditoría por usuario responsable (si se registra este
	 * campo en descripción).
	 *
	 * @param usuario Nombre o identificador de usuario.
	 * @return Lista de registros para ese usuario.
	 */
	List<Auditoria> buscarPorUsuario(String usuario);

	/**
	 * Busca registros por tipo de acción (INSERT, UPDATE, DELETE, LOGIN...).
	 *
	 * @param accion Acción realizada.
	 * @return Lista de registros para esa acción.
	 */
	List<Auditoria> buscarPorAccion(String accion);

	/**
	 * Busca registros por entidad (tabla) modificada.
	 *
	 * @param entidad Nombre de la tabla o entidad.
	 * @return Lista de registros para esa entidad.
	 */
	List<Auditoria> buscarPorEntidad(String entidad);

	/**
	 * Busca registros por rango de fecha/hora.
	 *
	 * @param desde Fecha/hora de inicio (inclusive).
	 * @param hasta Fecha/hora de fin (inclusive).
	 * @return Lista de registros en ese rango.
	 */
	List<Auditoria> buscarPorFechaHoraRango(LocalDateTime desde, LocalDateTime hasta);

	/**
	 * Busca registros por texto parcial en la descripción.
	 *
	 * @param descripcion Texto o fragmento a buscar.
	 * @return Lista de logs que contienen ese texto.
	 */
	List<Auditoria> buscarPorDescripcionLike(String descripcion);
}
