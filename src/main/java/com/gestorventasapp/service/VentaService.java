package com.gestorventasapp.service;

import com.gestorventasapp.model.DetalleVenta;
import com.gestorventasapp.model.Venta;
import com.gestorventasapp.enums.Estado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de ventas. Aplica la lógica de negocio, validaciones
 * y control profesional sobre la entidad Venta.
 */
public interface VentaService {

	/**
	 * Da de alta una nueva venta tras validar los datos y reglas de negocio.
	 *
	 * @param venta Venta a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos.
	 */
	void crearVenta(Venta venta);

	/**
	 * Modifica los datos de una venta existente tras validar todas las
	 * restricciones.
	 *
	 * @param venta Venta modificada.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o la venta
	 *                                                         no existe.
	 */
	void modificarVenta(Venta venta);

	/**
	 * Realiza la baja lógica (estado -> inactivo) de una venta.
	 *
	 * @param idVenta Identificador de la venta.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	void darBajaLogicaVenta(int idVenta);

	/**
	 * Busca una venta por su ID.
	 *
	 * @param idVenta Identificador.
	 * @return Venta encontrada.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	Venta buscarPorId(int idVenta);

	/**
	 * Lista todas las ventas (activas e inactivas).
	 *
	 * @return Lista completa de ventas.
	 */
	List<Venta> listarTodas();

	/**
	 * Lista solo las ventas activas.
	 *
	 * @return Lista de ventas activas.
	 */
	List<Venta> listarActivas();

	/**
	 * Lista solo las ventas inactivas.
	 *
	 * @return Lista de ventas inactivas.
	 */
	List<Venta> listarInactivas();

	/**
	 * Lista ventas por estado (activo/inactivo).
	 *
	 * @param estado Estado a filtrar.
	 * @return Lista de ventas filtradas por estado.
	 */
	List<Venta> listarPorEstado(Estado estado);

	/**
	 * Busca ventas por cliente.
	 *
	 * @param idCliente ID del cliente.
	 * @return Lista de ventas de ese cliente.
	 */
	List<Venta> buscarPorCliente(int idCliente);

	/**
	 * Busca ventas por empleado.
	 *
	 * @param idEmpleado ID del empleado.
	 * @return Lista de ventas hechas por ese empleado.
	 */
	List<Venta> buscarPorEmpleado(int idEmpleado);

	/**
	 * Busca ventas por fecha exacta.
	 *
	 * @param fecha Fecha concreta.
	 * @return Lista de ventas en esa fecha.
	 */
	List<Venta> buscarPorFecha(LocalDate fecha);

	/**
	 * Busca ventas entre dos fechas (ambas inclusive).
	 *
	 * @param fechaInicio Fecha de inicio.
	 * @param fechaFin    Fecha de fin.
	 * @return Lista de ventas en ese rango.
	 */
	List<Venta> buscarPorFechaRango(LocalDate fechaInicio, LocalDate fechaFin);

	/**
	 * Busca ventas por rango de total sin IVA.
	 *
	 * @param min Total sin IVA mínimo (incluido).
	 * @param max Total sin IVA máximo (incluido).
	 * @return Lista de ventas en ese rango.
	 */
	List<Venta> buscarPorTotalSinIvaEntre(BigDecimal min, BigDecimal max);

	/**
	 * Busca ventas por rango de total con IVA.
	 *
	 * @param min Total con IVA mínimo (incluido).
	 * @param max Total con IVA máximo (incluido).
	 * @return Lista de ventas en ese rango.
	 */
	List<Venta> buscarPorTotalConIvaEntre(BigDecimal min, BigDecimal max);

	/**
	 * Crea una venta junto a todos sus detalles en una única transacción.
	 *
	 * @param venta         Objeto Venta a guardar.
	 * @param detallesVenta Lista de detalles asociados a la venta.
	 * @throws com.gestorventasapp.exceptions.ServiceException si ocurre error o
	 *                                                         validación.
	 */
	void crearVentaConDetalles(Venta venta, List<DetalleVenta> detallesVenta);

}
