package com.gestorventasapp.dao;

import com.gestorventasapp.model.DetalleVenta;
import com.gestorventasapp.enums.Estado;
import java.util.List;

public interface DetalleVentaDAO {

	void save(DetalleVenta detalleVenta); // Alta de nuevo detalle de venta

	void update(DetalleVenta detalleVenta); // Modificación de detalle existente

	void delete(int idDetalleVenta); // Baja lógica (estado -> inactivo)

	DetalleVenta findById(int idDetalleVenta); // Buscar detalle por ID

	List<DetalleVenta> findAll(); // Listar todos (activos e inactivos)

	List<DetalleVenta> findAllActivos(); // Listar solo detalles activos

	List<DetalleVenta> findAllInactivos(); // Listar solo detalles inactivos

	List<DetalleVenta> findByEstado(Estado estado); // Listar por estado

	List<DetalleVenta> findByVenta(int idVenta); // Listar detalles por venta (solo activos)

	List<DetalleVenta> findByProducto(int idProducto); // Listar detalles por producto (solo activos)

	DetalleVenta findByProductoAndVenta(int idProducto, int idVenta); // Buscar detalle concreto

	List<DetalleVenta> findByCantidadGreaterThan(int cantidad); // Detectar ventas grandes (solo activos)

	List<DetalleVenta> findBySubtotalSinIvaBetween(double min, double max); // Filtro importe (solo activos)

	List<DetalleVenta> findBySubtotalConIvaBetween(double min, double max); // Filtro importe (solo activos)
}
