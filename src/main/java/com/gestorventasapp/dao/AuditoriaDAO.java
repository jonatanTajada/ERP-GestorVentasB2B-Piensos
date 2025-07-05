package com.gestorventasapp.dao;

import com.gestorventasapp.model.Auditoria;
import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaDAO {

	void save(Auditoria auditoria); // Registrar nueva auditoría (log)

	Auditoria findById(int idAuditoria); // Buscar registro por ID

	List<Auditoria> findAll(); // Listar todos los logs

	List<Auditoria> findByUsuario(String usuario); // Buscar por usuario

	List<Auditoria> findByAccion(String accion); // Buscar por tipo de acción (INSERT, UPDATE, DELETE, LOGIN, etc.)

	List<Auditoria> findByEntidad(String entidad); // Buscar por tabla o entidad afectada

	List<Auditoria> findByFechaHoraRango(LocalDateTime desde, LocalDateTime hasta); // Buscar por rango de fecha/hora

	List<Auditoria> findByDescripcionLike(String descripcion); // Buscar por texto parcial en descripción

}
