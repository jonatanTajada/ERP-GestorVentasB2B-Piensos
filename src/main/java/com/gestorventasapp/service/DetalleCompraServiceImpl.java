package com.gestorventasapp.service;

import com.gestorventasapp.dao.DetalleCompraDAO;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.Compra;
import com.gestorventasapp.model.DetalleCompra;
import com.gestorventasapp.model.Producto;
import com.gestorventasapp.exceptions.ServiceException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementación de la lógica de negocio y validaciones para la entidad
 * DetalleCompra. Aplica bajas lógicas, validaciones y consultas avanzadas.
 */
public class DetalleCompraServiceImpl implements DetalleCompraService {

	private final DetalleCompraDAO detalleCompraDAO;

	/**
	 * Constructor con inyección de dependencia.
	 *
	 * @param detalleCompraDAO DAO de detalles de compra.
	 */
	public DetalleCompraServiceImpl(DetalleCompraDAO detalleCompraDAO) {
		this.detalleCompraDAO = detalleCompraDAO;
	}

	@Override
	public void crearDetalleCompra(DetalleCompra detalleCompra) {
		validarDetalleCompra(detalleCompra, true);
		detalleCompra.setEstado(Estado.activo);
		detalleCompraDAO.save(detalleCompra);
	}

	@Override
	public void modificarDetalleCompra(DetalleCompra detalleCompra) {
		if (detalleCompra == null || detalleCompra.getIdDetalleCompra() == null) {
			throw new ServiceException("El detalle de compra y su ID no pueden ser nulos.");
		}
		DetalleCompra existente = detalleCompraDAO.findById(detalleCompra.getIdDetalleCompra());
		if (existente == null) {
			throw new ServiceException("No existe el detalle de compra a modificar.");
		}
		validarDetalleCompra(detalleCompra, false);
		detalleCompraDAO.update(detalleCompra);
	}

	@Override
	public void eliminarDetalleCompra(int idDetalleCompra) {
		DetalleCompra detalle = detalleCompraDAO.findById(idDetalleCompra);
		if (detalle == null) {
			throw new ServiceException("No existe el detalle de compra a eliminar.");
		}
		if (detalle.getEstado() == Estado.inactivo) {
			throw new ServiceException("El detalle de compra ya está inactivo.");
		}
		detalleCompraDAO.delete(idDetalleCompra); // Baja lógica (update estado)
	}

	@Override
	public DetalleCompra buscarPorId(int idDetalleCompra) {
		DetalleCompra detalle = detalleCompraDAO.findById(idDetalleCompra);
		if (detalle == null) {
			throw new ServiceException("No existe un detalle de compra con ese ID.");
		}
		return detalle;
	}

	@Override
	public List<DetalleCompra> listarTodos() {
		return detalleCompraDAO.findAll();
	}

	@Override
	public List<DetalleCompra> listarPorCompra(int idCompra) {
		if (idCompra <= 0)
			throw new ServiceException("El ID de compra no es válido.");
		return detalleCompraDAO.findByCompra(idCompra);
	}

	@Override
	public List<DetalleCompra> listarPorProducto(int idProducto) {
		if (idProducto <= 0)
			throw new ServiceException("El ID de producto no es válido.");
		return detalleCompraDAO.findByProducto(idProducto);
	}

	@Override
	public DetalleCompra buscarPorProductoYCompra(int idProducto, int idCompra) {
		if (idProducto <= 0 || idCompra <= 0)
			throw new ServiceException("ID de producto o compra no válidos.");
		return detalleCompraDAO.findByProductoAndCompra(idProducto, idCompra);
	}

	@Override
	public List<DetalleCompra> listarPorCantidadMayorQue(int cantidad) {
		if (cantidad < 0)
			throw new ServiceException("La cantidad debe ser positiva.");
		return detalleCompraDAO.findByCantidadGreaterThan(cantidad);
	}

	@Override
	public List<DetalleCompra> listarPorSubtotalSinIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los importes no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return detalleCompraDAO.findBySubtotalSinIvaBetween(min.doubleValue(), max.doubleValue());
	}

	@Override
	public List<DetalleCompra> listarPorSubtotalConIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los importes no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return detalleCompraDAO.findBySubtotalConIvaBetween(min.doubleValue(), max.doubleValue());
	}

	/**
	 * Valida el objeto DetalleCompra según reglas de negocio y estructura de BBDD.
	 *
	 * @param detalleCompra Objeto a validar.
	 * @param esNuevo       True si es alta.
	 * @throws ServiceException si algún dato no cumple las restricciones.
	 */
	private void validarDetalleCompra(DetalleCompra detalleCompra, boolean esNuevo) {
		if (detalleCompra == null)
			throw new ServiceException("El detalle de compra no puede ser nulo.");

		Compra compra = detalleCompra.getCompra();
		if (compra == null || compra.getIdCompra() == null)
			throw new ServiceException("La compra asociada es obligatoria.");

		Producto producto = detalleCompra.getProducto();
		if (producto == null || producto.getIdProducto() == null)
			throw new ServiceException("El producto es obligatorio.");

		if (detalleCompra.getCantidad() == null || detalleCompra.getCantidad() < 1)
			throw new ServiceException("La cantidad debe ser mayor que cero.");

		if (detalleCompra.getPrecioUnitario() == null
				|| detalleCompra.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El precio unitario es obligatorio y no puede ser negativo.");

		if (detalleCompra.getPorcentajeIva() == null || detalleCompra.getPorcentajeIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El porcentaje de IVA es obligatorio y no puede ser negativo.");

		if (detalleCompra.getSubtotalSinIva() == null
				|| detalleCompra.getSubtotalSinIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El subtotal sin IVA es obligatorio y no puede ser negativo.");

		if (detalleCompra.getSubtotalConIva() == null
				|| detalleCompra.getSubtotalConIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El subtotal con IVA es obligatorio y no puede ser negativo.");

		if (detalleCompra.getEstado() == null)
			throw new ServiceException("El estado es obligatorio.");
	}
}
