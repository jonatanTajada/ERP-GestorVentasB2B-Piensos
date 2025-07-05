package com.gestorventasapp.service;

import com.gestorventasapp.dao.ProductoDAO;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.enums.TipoAnimal;
import com.gestorventasapp.model.Iva;
import com.gestorventasapp.model.Producto;
import com.gestorventasapp.model.Proveedor;
import com.gestorventasapp.exceptions.ServiceException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Implementación de la lógica de negocio y validaciones para la entidad
 * Producto. Controla creación, modificación, baja lógica, reactivación y
 * control profesional del stock.
 */
public class ProductoServiceImpl implements ProductoService {

	private final ProductoDAO productoDAO;

	/**
	 * Constructor por inyección de dependencia.
	 *
	 * @param productoDAO DAO de productos.
	 */
	public ProductoServiceImpl(ProductoDAO productoDAO) {
		this.productoDAO = productoDAO;
	}

	@Override
	public void crearProducto(Producto producto) {
		validarProducto(producto, true);

		// Combinación única: marca + formato + proveedor
		boolean existeCombinacion = productoDAO.existsByMarcaAndFormatoAndProveedor(producto.getMarca(),
				producto.getFormato(), producto.getProveedor().getIdProveedor());

		// Si existe como inactivo → reactivar y actualizar
		if (existeCombinacion) {
			List<Producto> candidatos = productoDAO.findByProveedor(producto.getProveedor().getIdProveedor());
			for (Producto p : candidatos) {
				if (p.getMarca().equalsIgnoreCase(producto.getMarca())
						&& p.getFormato().equalsIgnoreCase(producto.getFormato()) && p.getEstado() == Estado.inactivo) {
					actualizarYReactivar(producto, p.getIdProducto());
					return;
				}
			}
			// Si existe y está activo, error
			for (Producto p : candidatos) {
				if (p.getMarca().equalsIgnoreCase(producto.getMarca())
						&& p.getFormato().equalsIgnoreCase(producto.getFormato()) && p.getEstado() == Estado.activo) {
					throw new ServiceException("Ya existe un producto activo con esa marca, formato y proveedor.");
				}
			}
		}

		// Nombre único (opcional, si tu negocio lo requiere)
		if (productoDAO.existsNombre(producto.getNombre())) {
			throw new ServiceException("Ya existe un producto con ese nombre.");
		}

		producto.setEstado(Estado.activo);
		if (producto.getStock() == null)
			producto.setStock(0);
		if (producto.getStockMinimo() == null)
			producto.setStockMinimo(0);

		productoDAO.save(producto);
	}

	@Override
	public void modificarProducto(Producto producto) {
		if (producto == null || producto.getIdProducto() == null) {
			throw new ServiceException("El producto y su ID no pueden ser nulos.");
		}

		Producto existente = productoDAO.findById(producto.getIdProducto());
		if (existente == null) {
			throw new ServiceException("No existe el producto a modificar.");
		}

		validarProducto(producto, false);

		// -- Comprobación de combinación única --
		List<Producto> conMismaCombinacion = productoDAO.findByProveedor(producto.getProveedor().getIdProveedor());
		for (Producto p : conMismaCombinacion) {
			// Solo comprobamos si el ID es distinto (no se compara consigo mismo)
			if (p.getIdProducto() != null && !p.getIdProducto().equals(producto.getIdProducto())
					&& p.getMarca().equalsIgnoreCase(producto.getMarca())
					&& p.getFormato().equalsIgnoreCase(producto.getFormato())) {
				throw new ServiceException("Ya existe otro producto con esa marca, formato y proveedor.");
			}
		}

		// -- Comprobación de nombre único --
		List<Producto> conMismoNombre = productoDAO.findByNombre(producto.getNombre());
		for (Producto p : conMismoNombre) {
			if (p.getIdProducto() != null && !p.getIdProducto().equals(producto.getIdProducto())) {
				throw new ServiceException("Ya existe otro producto con ese nombre.");
			}
		}

		productoDAO.update(producto);
	}

	@Override
	public void darBajaLogicaProducto(int idProducto) {
		Producto producto = productoDAO.findById(idProducto);
		if (producto == null) {
			throw new ServiceException("No existe el producto a dar de baja.");
		}
		if (producto.getEstado() == Estado.inactivo) {
			throw new ServiceException("El producto ya está inactivo.");
		}
		producto.setEstado(Estado.inactivo);
		productoDAO.update(producto);
	}

	@Override
	public Producto buscarPorId(int idProducto) {
		Producto producto = productoDAO.findById(idProducto);
		if (producto == null) {
			throw new ServiceException("No existe un producto con ese ID.");
		}
		return producto;
	}

	@Override
	public List<Producto> listarTodos() {
		return productoDAO.findAll();
	}

	@Override
	public List<Producto> listarActivos() {
		return productoDAO.findAllActivos();
	}

	@Override
	public List<Producto> listarInactivos() {
		return productoDAO.findAllInactivos();
	}

	@Override
	public List<Producto> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ServiceException("El estado no puede ser nulo.");
		return productoDAO.findByEstado(estado);
	}

	@Override
	public List<Producto> buscarPorNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty())
			throw new ServiceException("El nombre no puede estar vacío.");
		return productoDAO.findByNombre(nombre.trim());
	}

	@Override
	public List<Producto> buscarPorTipoAnimal(TipoAnimal tipoAnimal) {
		if (tipoAnimal == null)
			throw new ServiceException("El tipo de animal no puede ser nulo.");
		return productoDAO.findByTipoAnimal(tipoAnimal.name());
	}

	@Override
	public List<Producto> buscarPorMarca(String marca) {
		if (marca == null || marca.trim().isEmpty())
			throw new ServiceException("La marca no puede estar vacía.");
		return productoDAO.findByMarca(marca.trim());
	}

	@Override
	public List<Producto> buscarPorProveedor(int idProveedor) {
		if (idProveedor <= 0)
			throw new ServiceException("El ID de proveedor no es válido.");
		return productoDAO.findByProveedor(idProveedor);
	}

	@Override
	public List<Producto> buscarPorFormato(String formato) {
		if (formato == null || formato.trim().isEmpty())
			throw new ServiceException("El formato no puede estar vacío.");
		return productoDAO.findByFormato(formato.trim());
	}

	@Override
	public List<Producto> buscarPorStockMinimo() {
		return productoDAO.findByStockMinimo();
	}

	@Override
	public List<Producto> buscarPorStockMenorQue(int cantidad) {
		if (cantidad < 0)
			throw new ServiceException("La cantidad no puede ser negativa.");
		return productoDAO.findByStockMenorQue(cantidad);
	}

	@Override
	public List<Producto> buscarPorStockMayorQue(int cantidad) {
		if (cantidad < 0)
			throw new ServiceException("La cantidad no puede ser negativa.");
		return productoDAO.findByStockMayorQue(cantidad);
	}

	@Override
	public boolean existeNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty())
			throw new ServiceException("El nombre no puede estar vacío.");
		return productoDAO.existsNombre(nombre.trim());
	}

	@Override
	public boolean existeMarcaFormatoProveedor(String marca, String formato, int idProveedor) {
		if (marca == null || marca.trim().isEmpty() || formato == null || formato.trim().isEmpty() || idProveedor <= 0)
			throw new ServiceException("Marca, formato y proveedor son obligatorios.");
		return productoDAO.existsByMarcaAndFormatoAndProveedor(marca.trim(), formato.trim(), idProveedor);
	}

	@Override
	public List<Producto> buscarPorPrecioVentaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los precios no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los precios no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El precio máximo no puede ser menor que el mínimo.");
		return productoDAO.findByPrecioVentaBetween(min.doubleValue(), max.doubleValue());
	}

	@Override
	public List<Producto> buscarPorPrecioCompraEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los precios no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los precios no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El precio máximo no puede ser menor que el mínimo.");
		return productoDAO.findByPrecioCompraBetween(min.doubleValue(), max.doubleValue());
	}

	/**
	 * Suma al stock de un producto y lo reactiva si estaba inactivo.
	 * 
	 * @param idProducto    ID del producto.
	 * @param cantidadSumar Cantidad a añadir al stock.
	 * @throws ServiceException si el producto no existe.
	 */
	@Override
	public void actualizarYReactivarStock(int idProducto, int cantidadSumar) {
		Producto prod = productoDAO.findById(idProducto);
		if (prod == null) {
			throw new ServiceException("No existe el producto para actualizar stock.");
		}
		int stockActual = prod.getStock() != null ? prod.getStock() : 0;
		prod.setStock(stockActual + cantidadSumar);
		prod.setEstado(Estado.activo); // Reactiva si estaba inactivo
		productoDAO.update(prod);
	}

	/**
	 * Valida el objeto Producto según reglas de negocio y estructura de BBDD.
	 *
	 * @param producto Objeto a validar.
	 * @param esNuevo  True si es alta.
	 * @throws ServiceException si algún dato no cumple las restricciones.
	 */
	private void validarProducto(Producto producto, boolean esNuevo) {
		if (producto == null)
			throw new ServiceException("El producto no puede ser nulo.");
		// Nombre
		if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()
				|| producto.getNombre().length() > 100) {
			throw new ServiceException("El nombre es obligatorio y no puede superar 100 caracteres.");
		}
		// Descripción (opcional)
		if (producto.getDescripcion() != null && producto.getDescripcion().length() > 255) {
			throw new ServiceException("La descripción no puede superar 255 caracteres.");
		}
		// Tipo animal
		if (producto.getTipoAnimal() == null) {
			throw new ServiceException("El tipo de animal es obligatorio.");
		}
		// Marca
		if (producto.getMarca() == null || producto.getMarca().trim().isEmpty() || producto.getMarca().length() > 50) {
			throw new ServiceException("La marca es obligatoria y no puede superar 50 caracteres.");
		}
		// Formato
		if (producto.getFormato() == null || producto.getFormato().trim().isEmpty()
				|| producto.getFormato().length() > 50) {
			throw new ServiceException("El formato es obligatorio y no puede superar 50 caracteres.");
		}
		// Precio venta
		if (producto.getPrecioVenta() == null || producto.getPrecioVenta().compareTo(BigDecimal.ZERO) < 0) {
			throw new ServiceException("El precio de venta es obligatorio y no puede ser negativo.");
		}
		// Precio compra
		if (producto.getPrecioCompra() == null || producto.getPrecioCompra().compareTo(BigDecimal.ZERO) < 0) {
			throw new ServiceException("El precio de compra es obligatorio y no puede ser negativo.");
		}
		// Proveedor
		Proveedor proveedor = producto.getProveedor();
		if (proveedor == null || proveedor.getIdProveedor() == null) {
			throw new ServiceException("El proveedor es obligatorio.");
		}
		// IVA
		Iva iva = producto.getIva();
		if (iva == null || iva.getIdIva() == null) {
			throw new ServiceException("El tipo de IVA es obligatorio.");
		}
		// Stock
		if (producto.getStock() != null && producto.getStock() < 0) {
			throw new ServiceException("El stock no puede ser negativo.");
		}
		// Stock mínimo
		if (producto.getStockMinimo() != null && producto.getStockMinimo() < 0) {
			throw new ServiceException("El stock mínimo no puede ser negativo.");
		}
		// Estado
		if (producto.getEstado() == null) {
			throw new ServiceException("El estado es obligatorio.");
		}
	}

	/**
	 * Actualiza todos los datos y reactiva un producto previamente inactivo.
	 *
	 * @param producto   Datos nuevos del producto.
	 * @param idProducto ID del producto a reactivar.
	 */
	private void actualizarYReactivar(Producto producto, Integer idProducto) {
		producto.setIdProducto(idProducto);
		producto.setEstado(Estado.activo);
		productoDAO.update(producto);
	}

}
