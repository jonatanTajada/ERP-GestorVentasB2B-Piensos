package com.gestorventasapp.service;

import com.gestorventasapp.model.DetalleDevolucionProveedor;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio para la gestión de detalles de devoluciones a proveedor. Aplica
 * lógica de negocio, validaciones y consultas avanzadas sobre la entidad
 * DetalleDevolucionProveedor.
 */
public interface DetalleDevolucionProveedorService {

	/**
	 * Da de alta un nuevo detalle de devolución a proveedor tras validar los datos
	 * y reglas de negocio.
	 *
	 * @param detalle Detalle a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos.
	 */
	void crearDetalleDevolucionProveedor(DetalleDevolucionProveedor detalle);

	/**
	 * Modifica los datos de un detalle existente tras validar todas las
	 * restricciones.
	 *
	 * @param detalle Detalle modificado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o el
	 *                                                         detalle no existe.
	 */
	void modificarDetalleDevolucionProveedor(DetalleDevolucionProveedor detalle);

	/**
	 * Elimina (baja lógica: estado -> inactivo) un detalle de devolución a
	 * proveedor.
	 *
	 * @param idDetalle ID del detalle.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	void eliminarDetalleDevolucionProveedor(int idDetalle);

	/**
	 * Busca un detalle de devolución a proveedor por su ID.
	 *
	 * @param idDetalle Identificador.
	 * @return Detalle encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	DetalleDevolucionProveedor buscarPorId(int idDetalle);

	/**
	 * Lista todos los detalles de devoluciones (activos e inactivos).
	 *
	 * @return Lista completa de detalles.
	 */
	List<DetalleDevolucionProveedor> listarTodos();

	/**
	 * Lista los detalles de una devolución concreta.
	 *
	 * @param idDevolucion ID de la devolución.
	 * @return Lista de detalles de esa devolución.
	 */
	List<DetalleDevolucionProveedor> listarPorDevolucion(int idDevolucion);

	/**
	 * Lista los detalles para un producto concreto (histórico de devoluciones de
	 * ese producto).
	 *
	 * @param idProducto ID del producto.
	 * @return Lista de detalles para ese producto.
	 */
	List<DetalleDevolucionProveedor> listarPorProducto(int idProducto);

	/**
	 * Busca un detalle concreto por producto y devolución.
	 *
	 * @param idProducto   ID del producto.
	 * @param idDevolucion ID de la devolución.
	 * @return Detalle encontrado, si existe.
	 */
	DetalleDevolucionProveedor buscarPorProductoYDevolucion(int idProducto, int idDevolucion);

	/**
	 * Lista los detalles con cantidad superior a un valor.
	 *
	 * @param cantidad Cantidad mínima.
	 * @return Lista de detalles con cantidad mayor.
	 */
	List<DetalleDevolucionProveedor> listarPorCantidadMayorQue(int cantidad);

	/**
	 * Lista los detalles cuyo subtotal sin IVA está en un rango.
	 *
	 * @param min Importe mínimo.
	 * @param max Importe máximo.
	 * @return Lista de detalles en ese rango.
	 */
	List<DetalleDevolucionProveedor> listarPorSubtotalSinIvaEntre(BigDecimal min, BigDecimal max);

	/**
	 * Lista los detalles cuyo subtotal con IVA está en un rango.
	 *
	 * @param min Importe mínimo.
	 * @param max Importe máximo.
	 * @return Lista de detalles en ese rango.
	 */
	List<DetalleDevolucionProveedor> listarPorSubtotalConIvaEntre(BigDecimal min, BigDecimal max);
}
