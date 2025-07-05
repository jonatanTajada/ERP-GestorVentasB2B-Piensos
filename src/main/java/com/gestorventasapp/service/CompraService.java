package com.gestorventasapp.service;

import com.gestorventasapp.model.Compra;
import com.gestorventasapp.model.DetalleCompra;
import com.gestorventasapp.enums.Estado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de compras a proveedores. Aplica lógica de negocio,
 * validaciones y control profesional sobre la entidad Compra.
 */
public interface CompraService {

	/**
	 * Da de alta una nueva compra tras validar los datos y reglas de negocio.
	 *
	 * @param compra Compra a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos.
	 */
	void crearCompra(Compra compra);

	/**
	 * Modifica los datos de una compra existente tras validar todas las
	 * restricciones.
	 *
	 * @param compra Compra modificada.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o la compra
	 *                                                         no existe.
	 */
	void modificarCompra(Compra compra);

	/**
	 * Realiza la baja lógica (estado -> inactivo) de una compra.
	 *
	 * @param idCompra Identificador de la compra.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	void darBajaLogicaCompra(int idCompra);

	/**
	 * Busca una compra por su ID.
	 *
	 * @param idCompra Identificador.
	 * @return Compra encontrada.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	Compra buscarPorId(int idCompra);

	/**
	 * Lista todas las compras (activas e inactivas).
	 *
	 * @return Lista completa de compras.
	 */
	List<Compra> listarTodas();

	/**
	 * Lista solo las compras activas.
	 *
	 * @return Lista de compras activas.
	 */
	List<Compra> listarActivas();

	/**
	 * Lista solo las compras inactivas.
	 *
	 * @return Lista de compras inactivas.
	 */
	List<Compra> listarInactivas();

	/**
	 * Lista compras por estado (activo/inactivo).
	 *
	 * @param estado Estado a filtrar.
	 * @return Lista de compras filtradas por estado.
	 */
	List<Compra> listarPorEstado(Estado estado);

	/**
	 * Busca compras por proveedor.
	 *
	 * @param idProveedor ID del proveedor.
	 * @return Lista de compras de ese proveedor.
	 */
	List<Compra> buscarPorProveedor(int idProveedor);

	/**
	 * Busca compras por empleado.
	 *
	 * @param idEmpleado ID del empleado.
	 * @return Lista de compras hechas por ese empleado.
	 */
	List<Compra> buscarPorEmpleado(int idEmpleado);

	/**
	 * Busca compras por fecha exacta.
	 *
	 * @param fecha Fecha concreta.
	 * @return Lista de compras en esa fecha.
	 */
	List<Compra> buscarPorFecha(LocalDate fecha);

	/**
	 * Busca compras entre dos fechas (ambas inclusive).
	 *
	 * @param fechaInicio Fecha de inicio.
	 * @param fechaFin    Fecha de fin.
	 * @return Lista de compras en ese rango.
	 */
	List<Compra> buscarPorFechaRango(LocalDate fechaInicio, LocalDate fechaFin);

	/**
	 * Busca compras por rango de total sin IVA.
	 *
	 * @param min Total sin IVA mínimo (incluido).
	 * @param max Total sin IVA máximo (incluido).
	 * @return Lista de compras en ese rango.
	 */
	List<Compra> buscarPorTotalSinIvaEntre(BigDecimal min, BigDecimal max);

	/**
	 * Busca compras por rango de total con IVA.
	 *
	 * @param min Total con IVA mínimo (incluido).
	 * @param max Total con IVA máximo (incluido).
	 * @return Lista de compras en ese rango.
	 */
	List<Compra> buscarPorTotalConIvaEntre(BigDecimal min, BigDecimal max);
	
	/**
	 * Crea una compra con todos sus detalles asociados (transacción completa).
	 */
	void crearCompraConDetalles(Compra compra, List<DetalleCompra> detallesCompra);
	
    /**
     * Valida el objeto Compra según las reglas de negocio y la estructura de la BBDD.
     * @param compra  Objeto a validar.
     * @param esNuevo True si es alta.
     */
    void validarCompra(Compra compra, boolean esNuevo);




}
