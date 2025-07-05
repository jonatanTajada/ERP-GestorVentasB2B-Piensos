package com.gestorventasapp.service;

import com.gestorventasapp.model.DetalleCompra;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio para la gestión de detalles de compra. Aplica la lógica de negocio,
 * validaciones y consultas avanzadas sobre la entidad DetalleCompra.
 */
public interface DetalleCompraService {

	/**
	 * Da de alta un nuevo detalle de compra tras validar todos los datos y reglas
	 * de negocio.
	 *
	 * @param detalleCompra Detalle de compra a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos.
	 */
	void crearDetalleCompra(DetalleCompra detalleCompra);

	/**
	 * Modifica los datos de un detalle de compra existente tras validar todas las
	 * restricciones.
	 *
	 * @param detalleCompra Detalle modificado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o el
	 *                                                         detalle no existe.
	 */
	void modificarDetalleCompra(DetalleCompra detalleCompra);

	/**
	 * Elimina un detalle de compra (eliminación física o baja lógica según la
	 * política del negocio).
	 *
	 * @param idDetalleCompra Identificador del detalle.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	void eliminarDetalleCompra(int idDetalleCompra);

	/**
	 * Busca un detalle de compra por su ID.
	 *
	 * @param idDetalleCompra Identificador.
	 * @return Detalle encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	DetalleCompra buscarPorId(int idDetalleCompra);

	/**
	 * Lista todos los detalles de compra.
	 *
	 * @return Lista completa de detalles de compra.
	 */
	List<DetalleCompra> listarTodos();

	/**
	 * Lista los detalles de una compra concreta.
	 *
	 * @param idCompra ID de la compra.
	 * @return Lista de detalles de esa compra.
	 */
	List<DetalleCompra> listarPorCompra(int idCompra);

	/**
	 * Lista los detalles para un producto concreto (histórico de compras de ese
	 * producto).
	 *
	 * @param idProducto ID del producto.
	 * @return Lista de detalles de compra para ese producto.
	 */
	List<DetalleCompra> listarPorProducto(int idProducto);

	/**
	 * Busca un detalle concreto por producto y compra.
	 *
	 * @param idProducto ID del producto.
	 * @param idCompra   ID de la compra.
	 * @return Detalle de compra correspondiente, si existe.
	 */
	DetalleCompra buscarPorProductoYCompra(int idProducto, int idCompra);

	/**
	 * Lista los detalles de compra con cantidad superior a un valor.
	 *
	 * @param cantidad Cantidad mínima.
	 * @return Lista de detalles con cantidad mayor.
	 */
	List<DetalleCompra> listarPorCantidadMayorQue(int cantidad);

	/**
	 * Lista los detalles de compra cuyo subtotal sin IVA está en un rango.
	 *
	 * @param min Importe mínimo.
	 * @param max Importe máximo.
	 * @return Lista de detalles en ese rango.
	 */
	List<DetalleCompra> listarPorSubtotalSinIvaEntre(BigDecimal min, BigDecimal max);

	/**
	 * Lista los detalles de compra cuyo subtotal con IVA está en un rango.
	 *
	 * @param min Importe mínimo.
	 * @param max Importe máximo.
	 * @return Lista de detalles en ese rango.
	 */
	List<DetalleCompra> listarPorSubtotalConIvaEntre(BigDecimal min, BigDecimal max);
}
