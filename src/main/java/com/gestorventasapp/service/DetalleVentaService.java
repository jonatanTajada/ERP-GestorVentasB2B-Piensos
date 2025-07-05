package com.gestorventasapp.service;

import com.gestorventasapp.model.DetalleVenta;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio para la gestión de detalles de venta. Aplica lógica de negocio,
 * validaciones y consultas avanzadas sobre la entidad DetalleVenta.
 */
public interface DetalleVentaService {

	/**
	 * Da de alta un nuevo detalle de venta tras validar todos los datos y reglas de
	 * negocio.
	 *
	 * @param detalleVenta Detalle de venta a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos.
	 */
	void crearDetalleVenta(DetalleVenta detalleVenta);

	/**
	 * Modifica los datos de un detalle de venta existente tras validar todas las
	 * restricciones.
	 *
	 * @param detalleVenta Detalle modificado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o el
	 *                                                         detalle no existe.
	 */
	void modificarDetalleVenta(DetalleVenta detalleVenta);

	/**
	 * Elimina un detalle de venta (baja lógica: estado -> inactivo).
	 *
	 * @param idDetalleVenta Identificador del detalle.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	void eliminarDetalleVenta(int idDetalleVenta);

	/**
	 * Busca un detalle de venta por su ID.
	 *
	 * @param idDetalleVenta Identificador.
	 * @return Detalle encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	DetalleVenta buscarPorId(int idDetalleVenta);

	/**
	 * Lista todos los detalles de venta (activos e inactivos).
	 *
	 * @return Lista completa de detalles de venta.
	 */
	List<DetalleVenta> listarTodos();

	/**
	 * Lista los detalles de una venta concreta.
	 *
	 * @param idVenta ID de la venta.
	 * @return Lista de detalles de esa venta.
	 */
	List<DetalleVenta> listarPorVenta(int idVenta);

	/**
	 * Lista los detalles para un producto concreto (histórico de ventas de ese
	 * producto).
	 *
	 * @param idProducto ID del producto.
	 * @return Lista de detalles de venta para ese producto.
	 */
	List<DetalleVenta> listarPorProducto(int idProducto);

	/**
	 * Busca un detalle concreto por producto y venta.
	 *
	 * @param idProducto ID del producto.
	 * @param idVenta    ID de la venta.
	 * @return Detalle de venta correspondiente, si existe.
	 */
	DetalleVenta buscarPorProductoYVenta(int idProducto, int idVenta);

	/**
	 * Lista los detalles de venta con cantidad superior a un valor.
	 *
	 * @param cantidad Cantidad mínima.
	 * @return Lista de detalles con cantidad mayor.
	 */
	List<DetalleVenta> listarPorCantidadMayorQue(int cantidad);

	/**
	 * Lista los detalles de venta cuyo subtotal sin IVA está en un rango.
	 *
	 * @param min Importe mínimo.
	 * @param max Importe máximo.
	 * @return Lista de detalles en ese rango.
	 */
	List<DetalleVenta> listarPorSubtotalSinIvaEntre(BigDecimal min, BigDecimal max);

	/**
	 * Lista los detalles de venta cuyo subtotal con IVA está en un rango.
	 *
	 * @param min Importe mínimo.
	 * @param max Importe máximo.
	 * @return Lista de detalles en ese rango.
	 */
	List<DetalleVenta> listarPorSubtotalConIvaEntre(BigDecimal min, BigDecimal max);
}
