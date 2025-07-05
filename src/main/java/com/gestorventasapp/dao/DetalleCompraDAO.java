package com.gestorventasapp.dao;

import com.gestorventasapp.model.DetalleCompra;
import com.gestorventasapp.enums.Estado;
import java.util.List;

public interface DetalleCompraDAO {

	void save(DetalleCompra detalleCompra); // Alta de nuevo detalle de compra

	void update(DetalleCompra detalleCompra); // Modificación de detalle existente

	void delete(int idDetalleCompra); // Baja lógica (estado -> inactivo)

	DetalleCompra findById(int idDetalleCompra); // Buscar detalle por ID

	List<DetalleCompra> findAll(); // Listar todos (activos e inactivos)

	List<DetalleCompra> findAllActivos(); // Listar solo detalles activos

	List<DetalleCompra> findAllInactivos(); // Listar solo detalles inactivos

	List<DetalleCompra> findByEstado(Estado estado); // Listar por estado (activo/inactivo)

	List<DetalleCompra> findByCompra(int idCompra); // Listar detalles de una compra (solo activos)

	List<DetalleCompra> findByProducto(int idProducto); // Listar detalles por producto (solo activos)

	DetalleCompra findByProductoAndCompra(int idProducto, int idCompra); // Por si quieres detalle concreto

	List<DetalleCompra> findByCantidadGreaterThan(int cantidad); // Detectar compras grandes (solo activos)

	List<DetalleCompra> findBySubtotalSinIvaBetween(double min, double max); // Filtro importe (solo activos)

	List<DetalleCompra> findBySubtotalConIvaBetween(double min, double max); // Filtro importe (solo activos)
}
