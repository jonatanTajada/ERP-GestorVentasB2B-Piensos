package com.gestorventasapp.dao;

import com.gestorventasapp.model.Compra;
import com.gestorventasapp.model.DetalleCompra;
import com.gestorventasapp.enums.Estado;
import java.time.LocalDate;
import java.util.List;

public interface CompraDAO {

	void save(Compra compra); // Alta de nueva compra

	void update(Compra compra); // Modificación de compra existente

	void delete(int idCompra); // Baja lógica (estado -> inactivo)

	Compra findById(int idCompra); // Buscar compra por ID

	List<Compra> findAll(); // Listar todas las compras (activas e inactivas)

	List<Compra> findAllActivas(); // Listar solo compras activas

	List<Compra> findAllInactivas(); // Listar solo compras inactivas

	List<Compra> findByEstado(Estado estado); // Buscar compras por estado

	List<Compra> findByProveedor(int idProveedor); // Buscar compras por proveedor

	List<Compra> findByEmpleado(int idEmpleado); // Buscar compras por empleado

	List<Compra> findByFecha(LocalDate fecha); // Buscar compras por fecha exacta

	List<Compra> findByFechaRango(LocalDate fechaInicio, LocalDate fechaFin); // Buscar compras en rango de fechas

	List<Compra> findByTotalSinIvaBetween(double min, double max); // Buscar compras por rango de total sin IVA

	List<Compra> findByTotalConIvaBetween(double min, double max); // Buscar compras por rango de total con IVA

	void saveWithDetails(Compra compra, List<DetalleCompra> detallesCompra); // Guarda una compra y sus detalles en una única transacción.

	
}
