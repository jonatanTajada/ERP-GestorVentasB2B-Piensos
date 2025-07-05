package com.gestorventasapp.controller;

import com.gestorventasapp.model.Producto;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.enums.TipoAnimal;
import com.gestorventasapp.service.ProductoService;
import com.gestorventasapp.exceptions.ControllerException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador para la gestión de productos. Orquesta la interacción entre la
 * vista y la capa de servicios, validando la entrada y gestionando los
 * resultados y errores.
 */
public class ProductoController {

	private final ProductoService productoService;

	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

	public void crearProducto(Producto producto) {
		if (producto == null)
			throw new ControllerException("El producto no puede ser nulo.");
		if (producto.getNombre() == null || producto.getNombre().trim().isEmpty())
			throw new ControllerException("El nombre del producto es obligatorio.");
		if (producto.getTipoAnimal() == null)
			throw new ControllerException("El tipo de animal es obligatorio.");
		if (producto.getMarca() == null || producto.getMarca().trim().isEmpty())
			throw new ControllerException("La marca es obligatoria.");
		if (producto.getFormato() == null || producto.getFormato().trim().isEmpty())
			throw new ControllerException("El formato es obligatorio.");
		if (producto.getPrecioVenta() == null)
			throw new ControllerException("El precio de venta es obligatorio.");
		if (producto.getPrecioCompra() == null)
			throw new ControllerException("El precio de compra es obligatorio.");
		if (producto.getProveedor() == null)
			throw new ControllerException("El proveedor es obligatorio.");
		if (producto.getIva() == null)
			throw new ControllerException("El tipo de IVA es obligatorio.");
		if (producto.getStock() == null)
			throw new ControllerException("El stock es obligatorio.");
		if (producto.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			productoService.crearProducto(producto);
		} catch (Exception e) {
			throw new ControllerException("Error al crear el producto: " + e.getMessage(), e);
		}
	}

	public void modificarProducto(Producto producto) {
		if (producto == null || producto.getIdProducto() == null)
			throw new ControllerException("El producto y su ID no pueden ser nulos.");
		if (producto.getNombre() == null || producto.getNombre().trim().isEmpty())
			throw new ControllerException("El nombre del producto es obligatorio.");
		if (producto.getTipoAnimal() == null)
			throw new ControllerException("El tipo de animal es obligatorio.");
		if (producto.getMarca() == null || producto.getMarca().trim().isEmpty())
			throw new ControllerException("La marca es obligatoria.");
		if (producto.getFormato() == null || producto.getFormato().trim().isEmpty())
			throw new ControllerException("El formato es obligatorio.");
		if (producto.getPrecioVenta() == null)
			throw new ControllerException("El precio de venta es obligatorio.");
		if (producto.getPrecioCompra() == null)
			throw new ControllerException("El precio de compra es obligatorio.");
		if (producto.getProveedor() == null)
			throw new ControllerException("El proveedor es obligatorio.");
		if (producto.getIva() == null)
			throw new ControllerException("El tipo de IVA es obligatorio.");
		if (producto.getStock() == null)
			throw new ControllerException("El stock es obligatorio.");
		if (producto.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			productoService.modificarProducto(producto);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar el producto: " + e.getMessage(), e);
		}
	}

	public void darBajaLogicaProducto(int idProducto) {
		if (idProducto <= 0)
			throw new ControllerException("El ID del producto debe ser válido.");
		try {
			productoService.darBajaLogicaProducto(idProducto);
		} catch (Exception e) {
			throw new ControllerException("Error al dar de baja el producto: " + e.getMessage(), e);
		}
	}

	public Producto buscarPorId(int idProducto) {
		if (idProducto <= 0)
			throw new ControllerException("El ID del producto debe ser válido.");
		try {
			return productoService.buscarPorId(idProducto);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el producto: " + e.getMessage(), e);
		}
	}

	public List<Producto> listarTodos() {
		try {
			return productoService.listarTodos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los productos: " + e.getMessage(), e);
		}
	}

	public List<Producto> listarActivos() {
		try {
			return productoService.listarActivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los productos activos: " + e.getMessage(), e);
		}
	}

	public List<Producto> listarInactivos() {
		try {
			return productoService.listarInactivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los productos inactivos: " + e.getMessage(), e);
		}
	}

	public List<Producto> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ControllerException("El estado no puede ser nulo.");
		try {
			return productoService.listarPorEstado(estado);
		} catch (Exception e) {
			throw new ControllerException("Error al listar productos por estado: " + e.getMessage(), e);
		}
	}

	public List<Producto> buscarPorNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty())
			throw new ControllerException("El nombre es obligatorio para la búsqueda.");
		try {
			return productoService.buscarPorNombre(nombre);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar productos por nombre: " + e.getMessage(), e);
		}
	}

	public List<Producto> buscarPorTipoAnimal(TipoAnimal tipoAnimal) {
		if (tipoAnimal == null)
			throw new ControllerException("El tipo de animal es obligatorio para la búsqueda.");
		try {
			return productoService.buscarPorTipoAnimal(tipoAnimal);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar productos por tipo de animal: " + e.getMessage(), e);
		}
	}

	public List<Producto> buscarPorMarca(String marca) {
		if (marca == null || marca.trim().isEmpty())
			throw new ControllerException("La marca es obligatoria para la búsqueda.");
		try {
			return productoService.buscarPorMarca(marca);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar productos por marca: " + e.getMessage(), e);
		}
	}

	public List<Producto> buscarPorProveedor(int idProveedor) {
		if (idProveedor <= 0)
			throw new ControllerException("El ID del proveedor debe ser válido.");
		try {
			return productoService.buscarPorProveedor(idProveedor);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar productos por proveedor: " + e.getMessage(), e);
		}
	}

	public List<Producto> buscarPorFormato(String formato) {
		if (formato == null || formato.trim().isEmpty())
			throw new ControllerException("El formato es obligatorio para la búsqueda.");
		try {
			return productoService.buscarPorFormato(formato);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar productos por formato: " + e.getMessage(), e);
		}
	}

	public List<Producto> buscarPorStockMinimo() {
		try {
			return productoService.buscarPorStockMinimo();
		} catch (Exception e) {
			throw new ControllerException("Error al buscar productos con stock mínimo: " + e.getMessage(), e);
		}
	}

	public List<Producto> buscarPorStockMenorQue(int cantidad) {
		if (cantidad < 0)
			throw new ControllerException("La cantidad debe ser positiva.");
		try {
			return productoService.buscarPorStockMenorQue(cantidad);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar productos con stock menor: " + e.getMessage(), e);
		}
	}

	public List<Producto> buscarPorStockMayorQue(int cantidad) {
		if (cantidad < 0)
			throw new ControllerException("La cantidad debe ser positiva.");
		try {
			return productoService.buscarPorStockMayorQue(cantidad);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar productos con stock mayor: " + e.getMessage(), e);
		}
	}

	public boolean existeNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty())
			throw new ControllerException("El nombre es obligatorio.");
		try {
			return productoService.existeNombre(nombre);
		} catch (Exception e) {
			throw new ControllerException("Error al comprobar existencia de nombre de producto: " + e.getMessage(), e);
		}
	}

	public boolean existeMarcaFormatoProveedor(String marca, String formato, int idProveedor) {
		if (marca == null || marca.trim().isEmpty())
			throw new ControllerException("La marca es obligatoria.");
		if (formato == null || formato.trim().isEmpty())
			throw new ControllerException("El formato es obligatorio.");
		if (idProveedor <= 0)
			throw new ControllerException("El ID del proveedor debe ser válido.");
		try {
			return productoService.existeMarcaFormatoProveedor(marca, formato, idProveedor);
		} catch (Exception e) {
			throw new ControllerException(
					"Error al comprobar existencia de combinación marca/formato/proveedor: " + e.getMessage(), e);
		}
	}

	public List<Producto> buscarPorPrecioVentaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de precios.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los precios no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El precio mínimo no puede ser mayor que el máximo.");
		try {
			return productoService.buscarPorPrecioVentaEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar productos por precio de venta: " + e.getMessage(), e);
		}
	}

	public List<Producto> buscarPorPrecioCompraEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de precios.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los precios no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El precio mínimo no puede ser mayor que el máximo.");
		try {
			return productoService.buscarPorPrecioCompraEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar productos por precio de compra: " + e.getMessage(), e);
		}
	}
}
