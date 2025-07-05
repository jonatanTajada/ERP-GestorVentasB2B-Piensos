package com.gestorventasapp.service;

import com.gestorventasapp.model.Producto;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.enums.TipoAnimal;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio para la gestión de productos (piensos). Aplica lógica de negocio,
 * validaciones y control de inventario sobre la entidad Producto.
 */
public interface ProductoService {

	/**
	 * Da de alta un nuevo producto tras validar todos los datos y reglas de
	 * negocio. Si existe un producto igual (nombre/marca/formato/proveedor)
	 * inactivo, lo reactiva.
	 *
	 * @param producto Producto a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o ya
	 *                                                         existe.
	 */
	void crearProducto(Producto producto);

	/**
	 * Modifica los datos de un producto existente tras validar todas las
	 * restricciones.
	 *
	 * @param producto Producto modificado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o el
	 *                                                         producto no existe.
	 */
	void modificarProducto(Producto producto);

	/**
	 * Realiza la baja lógica (estado -> inactivo) de un producto.
	 *
	 * @param idProducto Identificador del producto.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	void darBajaLogicaProducto(int idProducto);

	/**
	 * Busca un producto por su ID.
	 *
	 * @param idProducto Identificador.
	 * @return Producto encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	Producto buscarPorId(int idProducto);

	/**
	 * Lista todos los productos (activos e inactivos).
	 *
	 * @return Lista completa de productos.
	 */
	List<Producto> listarTodos();

	/**
	 * Lista solo los productos activos.
	 *
	 * @return Lista de productos activos.
	 */
	List<Producto> listarActivos();

	/**
	 * Lista solo los productos inactivos.
	 *
	 * @return Lista de productos inactivos.
	 */
	List<Producto> listarInactivos();

	/**
	 * Lista productos por estado.
	 *
	 * @param estado Estado a filtrar.
	 * @return Lista de productos filtrados por estado.
	 */
	List<Producto> listarPorEstado(Estado estado);

	/**
	 * Busca productos por nombre (parcial o exacto).
	 *
	 * @param nombre Nombre o parte.
	 * @return Lista de productos que coinciden.
	 */
	List<Producto> buscarPorNombre(String nombre);

	/**
	 * Busca productos por tipo de animal.
	 *
	 * @param tipoAnimal Tipo de animal.
	 * @return Lista de productos para ese tipo.
	 */
	List<Producto> buscarPorTipoAnimal(TipoAnimal tipoAnimal);

	/**
	 * Busca productos por marca.
	 *
	 * @param marca Marca.
	 * @return Lista de productos de esa marca.
	 */
	List<Producto> buscarPorMarca(String marca);

	/**
	 * Busca productos por proveedor.
	 *
	 * @param idProveedor ID del proveedor.
	 * @return Lista de productos de ese proveedor.
	 */
	List<Producto> buscarPorProveedor(int idProveedor);

	/**
	 * Busca productos por formato (saco, caja, etc.).
	 *
	 * @param formato Formato.
	 * @return Lista de productos de ese formato.
	 */
	List<Producto> buscarPorFormato(String formato);

	/**
	 * Busca productos cuyo stock es igual o menor al mínimo.
	 *
	 * @return Lista de productos en situación de stock crítico.
	 */
	List<Producto> buscarPorStockMinimo();

	/**
	 * Busca productos cuyo stock es menor que una cantidad.
	 *
	 * @param cantidad Cantidad máxima de stock.
	 * @return Lista de productos con stock menor.
	 */
	List<Producto> buscarPorStockMenorQue(int cantidad);

	/**
	 * Busca productos cuyo stock es mayor que una cantidad.
	 *
	 * @param cantidad Cantidad mínima de stock.
	 * @return Lista de productos con stock mayor.
	 */
	List<Producto> buscarPorStockMayorQue(int cantidad);

	/**
	 * Comprueba si existe un producto con ese nombre.
	 *
	 * @param nombre Nombre de producto.
	 * @return true si existe.
	 */
	boolean existeNombre(String nombre);

	/**
	 * Comprueba si existe un producto con la combinación única marca + formato +
	 * proveedor.
	 *
	 * @param marca       Marca.
	 * @param formato     Formato.
	 * @param idProveedor ID proveedor.
	 * @return true si existe esa combinación.
	 */
	boolean existeMarcaFormatoProveedor(String marca, String formato, int idProveedor);

	/**
	 * Busca productos por rango de precio de venta.
	 *
	 * @param min Precio mínimo (incluido).
	 * @param max Precio máximo (incluido).
	 * @return Lista de productos en ese rango.
	 */
	List<Producto> buscarPorPrecioVentaEntre(BigDecimal min, BigDecimal max);

	/**
	 * Busca productos por rango de precio de compra.
	 *
	 * @param min Precio mínimo (incluido).
	 * @param max Precio máximo (incluido).
	 * @return Lista de productos en ese rango.
	 */
	List<Producto> buscarPorPrecioCompraEntre(BigDecimal min, BigDecimal max);

	/**
	 * Actualiza (incrementa) el stock de un producto sumando la cantidad indicada.
	 * Si el producto estaba inactivo y se repone stock, se reactiva
	 * automáticamente.
	 *
	 * @param idProducto    ID del producto a actualizar.
	 * @param cantidadSumar Cantidad de unidades a sumar al stock.
	 * @throws com.gestorventasapp.exceptions.ServiceException si el producto no
	 *                                                         existe o ocurre un
	 *                                                         error de
	 *                                                         persistencia.
	 */
	void actualizarYReactivarStock(int idProducto, int cantidadSumar);

}
