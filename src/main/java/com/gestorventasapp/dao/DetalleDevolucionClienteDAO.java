package com.gestorventasapp.dao;

import com.gestorventasapp.model.DetalleDevolucionCliente;
import com.gestorventasapp.enums.Estado;
import java.util.List;

public interface DetalleDevolucionClienteDAO {

	void save(DetalleDevolucionCliente detalle); // Alta de nuevo detalle

	void update(DetalleDevolucionCliente detalle); // Modificación de detalle

	void delete(int idDetalle); // Baja lógica (estado -> inactivo)

	DetalleDevolucionCliente findById(int idDetalle); // Buscar detalle por ID

	List<DetalleDevolucionCliente> findAll(); // Listar todos (activos e inactivos)

	List<DetalleDevolucionCliente> findAllActivos(); // Listar solo detalles activos

	List<DetalleDevolucionCliente> findAllInactivos(); // Listar solo detalles inactivos

	List<DetalleDevolucionCliente> findByEstado(Estado estado); // Buscar por estado

	List<DetalleDevolucionCliente> findByDevolucion(int idDevolucion); // Buscar detalles por devolución (solo activos)

	List<DetalleDevolucionCliente> findByProducto(int idProducto); // Buscar detalles por producto (solo activos)

	DetalleDevolucionCliente findByProductoAndDevolucion(int idProducto, int idDevolucion); // Detalle concreto (activo)

	List<DetalleDevolucionCliente> findByCantidadGreaterThan(int cantidad); // Buscar devoluciones grandes (solo
																			// activos)

	List<DetalleDevolucionCliente> findBySubtotalSinIvaBetween(double min, double max); // Filtro por subtotal sin IVA
																						// (solo activos)

	List<DetalleDevolucionCliente> findBySubtotalConIvaBetween(double min, double max); // Filtro por subtotal con IVA
																						// (solo activos)

}
