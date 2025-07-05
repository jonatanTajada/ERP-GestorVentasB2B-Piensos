package com.gestorventasapp.dao;

import com.gestorventasapp.model.DetalleDevolucionProveedor;
import com.gestorventasapp.enums.Estado;
import java.util.List;

public interface DetalleDevolucionProveedorDAO {

	void save(DetalleDevolucionProveedor detalle); // Alta de nuevo detalle

	void update(DetalleDevolucionProveedor detalle); // Modificación de detalle

	void delete(int idDetalle); // Baja lógica (estado -> inactivo)

	DetalleDevolucionProveedor findById(int idDetalle); // Buscar detalle por ID

	List<DetalleDevolucionProveedor> findAll(); // Listar todos (activos e inactivos)

	List<DetalleDevolucionProveedor> findAllActivos(); // Listar solo detalles activos

	List<DetalleDevolucionProveedor> findAllInactivos(); // Listar solo detalles inactivos

	List<DetalleDevolucionProveedor> findByEstado(Estado estado); // Buscar por estado

	List<DetalleDevolucionProveedor> findByDevolucion(int idDevolucion); // Buscar detalles por devolución (solo
																			// activos)

	List<DetalleDevolucionProveedor> findByProducto(int idProducto); // Buscar detalles por producto (solo activos)

	DetalleDevolucionProveedor findByProductoAndDevolucion(int idProducto, int idDevolucion); // Detalle concreto
																								// (activo)

	List<DetalleDevolucionProveedor> findByCantidadGreaterThan(int cantidad); // Buscar devoluciones grandes (solo
																				// activos)

	List<DetalleDevolucionProveedor> findBySubtotalSinIvaBetween(double min, double max); // Filtro subtotal sin IVA
																							// (solo activos)

	List<DetalleDevolucionProveedor> findBySubtotalConIvaBetween(double min, double max); // Filtro subtotal con IVA
																							// (solo activos)

}
