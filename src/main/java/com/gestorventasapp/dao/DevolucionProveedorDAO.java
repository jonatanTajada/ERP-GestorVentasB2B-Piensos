package com.gestorventasapp.dao;

import com.gestorventasapp.model.DevolucionProveedor;
import com.gestorventasapp.enums.Estado;
import java.time.LocalDate;
import java.util.List;

public interface DevolucionProveedorDAO {

	void save(DevolucionProveedor devolucion); // Alta de nueva devolución

	void update(DevolucionProveedor devolucion); // Modificación de devolución existente

	void delete(int idDevolucion); // Baja lógica (estado -> inactivo)

	DevolucionProveedor findById(int idDevolucion); // Buscar devolución por ID

	List<DevolucionProveedor> findAll(); // Listar todas las devoluciones (activas e inactivas)

	List<DevolucionProveedor> findAllActivas(); // Listar solo devoluciones activas

	List<DevolucionProveedor> findAllInactivas(); // Listar solo devoluciones inactivas

	List<DevolucionProveedor> findByEstado(Estado estado); // Buscar devoluciones por estado

	List<DevolucionProveedor> findByProveedor(int idProveedor); // Buscar devoluciones por proveedor

	List<DevolucionProveedor> findByCompra(int idCompra); // Buscar devoluciones por compra asociada

	List<DevolucionProveedor> findByEmpleado(int idEmpleado); // Buscar devoluciones por empleado

	List<DevolucionProveedor> findByFecha(LocalDate fecha); // Buscar devoluciones por fecha exacta

	List<DevolucionProveedor> findByFechaRango(LocalDate fechaInicio, LocalDate fechaFin); // Buscar devoluciones en
																							// rango de fechas

	List<DevolucionProveedor> findByMotivo(String motivo); // Buscar devoluciones por motivo

	List<DevolucionProveedor> findByTotalBetween(double min, double max); // Buscar devoluciones por importe total

}
