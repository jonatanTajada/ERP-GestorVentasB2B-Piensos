package com.gestorventasapp.service;

import com.gestorventasapp.model.DevolucionProveedor;
import com.gestorventasapp.enums.Estado;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de devoluciones a proveedor. Aplica lógica de
 * negocio, validaciones y consultas avanzadas sobre la entidad
 * DevolucionProveedor.
 */
public interface DevolucionProveedorService {

	/**
	 * Da de alta una nueva devolución a proveedor tras validar todos los datos y
	 * reglas de negocio.
	 *
	 * @param devolucion Devolución a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos.
	 */
	void crearDevolucion(DevolucionProveedor devolucion);

	/**
	 * Modifica los datos de una devolución existente tras validar todas las
	 * restricciones.
	 *
	 * @param devolucion Devolución modificada.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o la
	 *                                                         devolución no existe.
	 */
	void modificarDevolucion(DevolucionProveedor devolucion);

	/**
	 * Realiza la baja lógica (estado -> inactivo) de una devolución.
	 *
	 * @param idDevolucion ID de la devolución.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	void darBajaLogicaDevolucion(int idDevolucion);

	/**
	 * Busca una devolución por su ID.
	 *
	 * @param idDevolucion Identificador.
	 * @return Devolución encontrada.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	DevolucionProveedor buscarPorId(int idDevolucion);

	/**
	 * Lista todas las devoluciones (activas e inactivas).
	 *
	 * @return Lista completa de devoluciones.
	 */
	List<DevolucionProveedor> listarTodas();

	/**
	 * Lista solo las devoluciones activas.
	 *
	 * @return Lista de devoluciones activas.
	 */
	List<DevolucionProveedor> listarActivas();

	/**
	 * Lista solo las devoluciones inactivas.
	 *
	 * @return Lista de devoluciones inactivas.
	 */
	List<DevolucionProveedor> listarInactivas();

	/**
	 * Lista devoluciones por estado (activo/inactivo).
	 *
	 * @param estado Estado a filtrar.
	 * @return Lista de devoluciones filtradas por estado.
	 */
	List<DevolucionProveedor> listarPorEstado(Estado estado);

	/**
	 * Busca devoluciones por proveedor.
	 *
	 * @param idProveedor ID del proveedor.
	 * @return Lista de devoluciones de ese proveedor.
	 */
	List<DevolucionProveedor> buscarPorProveedor(int idProveedor);

	/**
	 * Busca devoluciones por compra asociada.
	 *
	 * @param idCompra ID de la compra.
	 * @return Lista de devoluciones para esa compra.
	 */
	List<DevolucionProveedor> buscarPorCompra(int idCompra);

	/**
	 * Busca devoluciones por empleado que la gestionó.
	 *
	 * @param idEmpleado ID del empleado.
	 * @return Lista de devoluciones gestionadas por ese empleado.
	 */
	List<DevolucionProveedor> buscarPorEmpleado(int idEmpleado);

	/**
	 * Busca devoluciones por fecha exacta.
	 *
	 * @param fecha Fecha concreta.
	 * @return Lista de devoluciones en esa fecha.
	 */
	List<DevolucionProveedor> buscarPorFecha(LocalDate fecha);

	/**
	 * Busca devoluciones entre dos fechas (ambas inclusive).
	 *
	 * @param fechaInicio Fecha de inicio.
	 * @param fechaFin    Fecha de fin.
	 * @return Lista de devoluciones en ese rango.
	 */
	List<DevolucionProveedor> buscarPorFechaRango(LocalDate fechaInicio, LocalDate fechaFin);

	/**
	 * Busca devoluciones por motivo.
	 *
	 * @param motivo Motivo parcial o exacto.
	 * @return Lista de devoluciones que contienen ese motivo.
	 */
	List<DevolucionProveedor> buscarPorMotivo(String motivo);

	/**
	 * Busca devoluciones por importe total (rango).
	 *
	 * @param min Importe mínimo.
	 * @param max Importe máximo.
	 * @return Lista de devoluciones en ese rango.
	 */
	List<DevolucionProveedor> buscarPorTotalEntre(double min, double max);
}
