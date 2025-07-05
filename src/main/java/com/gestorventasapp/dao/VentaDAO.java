package com.gestorventasapp.dao;

import com.gestorventasapp.model.DetalleVenta;
import com.gestorventasapp.model.Venta;
import com.gestorventasapp.enums.Estado;
import java.time.LocalDate;
import java.util.List;

public interface VentaDAO {

	void save(Venta venta); // Alta de nueva venta

	void update(Venta venta); // Modificación de venta existente

	void delete(int idVenta); // Baja lógica (estado -> inactivo)

	Venta findById(int idVenta); // Buscar venta por ID

	List<Venta> findAll(); // Listar todas las ventas (activas e inactivas)

	List<Venta> findAllActivas(); // Listar solo ventas activas

	List<Venta> findAllInactivas(); // Listar solo ventas inactivas

	List<Venta> findByEstado(Estado estado); // Buscar ventas por estado

	List<Venta> findByCliente(int idCliente); // Buscar ventas por cliente

	List<Venta> findByEmpleado(int idEmpleado); // Buscar ventas por empleado

	List<Venta> findByFecha(LocalDate fecha); // Buscar ventas por fecha exacta

	List<Venta> findByFechaRango(LocalDate fechaInicio, LocalDate fechaFin); // Buscar ventas en rango de fechas

	List<Venta> findByTotalSinIvaBetween(double min, double max); // Buscar ventas por total sin IVA

	List<Venta> findByTotalConIvaBetween(double min, double max); // Buscar ventas por total con IVA

	void saveWithDetails(Venta venta, List<DetalleVenta> detallesVenta); // Guarda una venta junto con todos sus detalles en una sola transacción.



}
