package com.gestorventasapp.dao;

import com.gestorventasapp.model.DevolucionCliente;
import com.gestorventasapp.enums.Estado;
import java.time.LocalDate;
import java.util.List;

public interface DevolucionClienteDAO {

	void save(DevolucionCliente devolucion); // Alta de nueva devolución

	void update(DevolucionCliente devolucion); // Modificación de devolución existente

	void delete(int idDevolucion); // Baja lógica (estado -> inactivo)

	DevolucionCliente findById(int idDevolucion); // Buscar devolución por ID

	List<DevolucionCliente> findAll(); // Listar todas las devoluciones (activas e inactivas)

	List<DevolucionCliente> findAllActivas(); // Listar solo devoluciones activas

	List<DevolucionCliente> findAllInactivas(); // Listar solo devoluciones inactivas

	List<DevolucionCliente> findByEstado(Estado estado); // Buscar devoluciones por estado

	List<DevolucionCliente> findByCliente(int idCliente); // Buscar devoluciones por cliente

	List<DevolucionCliente> findByVenta(int idVenta); // Buscar devoluciones por venta asociada

	List<DevolucionCliente> findByEmpleado(int idEmpleado); // Buscar devoluciones por empleado que la gestionó

	List<DevolucionCliente> findByFecha(LocalDate fecha); // Buscar devoluciones por fecha exacta

	List<DevolucionCliente> findByFechaRango(LocalDate fechaInicio, LocalDate fechaFin); // Buscar devoluciones en rango
																							// de fechas

	List<DevolucionCliente> findByMotivo(String motivo); // Buscar devoluciones por motivo (error, defecto, cliente,
															// etc.)

	List<DevolucionCliente> findByTotalBetween(double min, double max); // Buscar devoluciones por importe total

}
