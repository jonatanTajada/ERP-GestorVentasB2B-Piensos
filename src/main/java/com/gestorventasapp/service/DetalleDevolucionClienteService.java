package com.gestorventasapp.service;

import com.gestorventasapp.model.DetalleDevolucionCliente;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio para la gestión de detalles de devoluciones de cliente. Aplica
 * lógica de negocio, validaciones y consultas avanzadas sobre la entidad
 * DetalleDevolucionCliente.
 */
public interface DetalleDevolucionClienteService {

	/**
	 * Da de alta un nuevo detalle de devolución de cliente tras validar los datos y
	 * reglas de negocio.
	 *
	 * @param detalle Detalle a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos.
	 */
	void crearDetalleDevolucionCliente(DetalleDevolucionCliente detalle);

	/**
	 * Modifica los datos de un detalle existente tras validar todas las
	 * restricciones.
	 *
	 * @param detalle Detalle modificado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o el
	 *                                                         detalle no existe.
	 */
	void modificarDetalleDevolucionCliente(DetalleDevolucionCliente detalle);

	/**
	 * Elimina (baja lógica: estado -> inactivo) un detalle de devolución de
	 * cliente.
	 *
	 * @param idDetalle ID del detalle.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	void eliminarDetalleDevolucionCliente(int idDetalle);

	/**
	 * Busca un detalle de devolución por su ID.
	 *
	 * @param idDetalle Identificador.
	 * @return Detalle encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	DetalleDevolucionCliente buscarPorId(int idDetalle);

	/**
	 * Lista todos los detalles de devoluciones (activos e inactivos).
	 *
	 * @return Lista completa de detalles.
	 */
	List<DetalleDevolucionCliente> listarTodos();

	/**
	 * Lista los detalles de una devolución concreta.
	 *
	 * @param idDevolucion ID de la devolución.
	 * @return Lista de detalles de esa devolución.
	 */
	List<DetalleDevolucionCliente> listarPorDevolucion(int idDevolucion);

	/**
	 * Lista los detalles para un producto concreto (histórico de devoluciones de
	 * ese producto).
	 *
	 * @param idProducto ID del producto.
	 * @return Lista de detalles para ese producto.
	 */
	List<DetalleDevolucionCliente> listarPorProducto(int idProducto);

	/**
	 * Busca un detalle concreto por producto y devolución.
	 *
	 * @param idProducto   ID del producto.
	 * @param idDevolucion ID de la devolución.
	 * @return Detalle encontrado, si existe.
	 */
	DetalleDevolucionCliente buscarPorProductoYDevolucion(int idProducto, int idDevolucion);

	/**
	 * Lista los detalles con cantidad superior a un valor.
	 *
	 * @param cantidad Cantidad mínima.
	 * @return Lista de detalles con cantidad mayor.
	 */
	List<DetalleDevolucionCliente> listarPorCantidadMayorQue(int cantidad);

	/**
	 * Lista los detalles cuyo subtotal sin IVA está en un rango.
	 *
	 * @param min Importe mínimo.
	 * @param max Importe máximo.
	 * @return Lista de detalles en ese rango.
	 */
	List<DetalleDevolucionCliente> listarPorSubtotalSinIvaEntre(BigDecimal min, BigDecimal max);

	/**
	 * Lista los detalles cuyo subtotal con IVA está en un rango.
	 *
	 * @param min Importe mínimo.
	 * @param max Importe máximo.
	 * @return Lista de detalles en ese rango.
	 */
	List<DetalleDevolucionCliente> listarPorSubtotalConIvaEntre(BigDecimal min, BigDecimal max);
}
