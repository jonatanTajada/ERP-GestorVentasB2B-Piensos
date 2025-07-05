package com.gestorventasapp.service;

import com.gestorventasapp.dao.DetalleVentaDAO;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.DetalleVenta;
import com.gestorventasapp.model.Producto;
import com.gestorventasapp.model.Venta;
import com.gestorventasapp.exceptions.ServiceException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementación de la lógica de negocio y validaciones para la entidad
 * DetalleVenta. Aplica bajas lógicas, validaciones y consultas avanzadas.
 */
public class DetalleVentaServiceImpl implements DetalleVentaService {

	private final DetalleVentaDAO detalleVentaDAO;

	/**
	 * Constructor con inyección de dependencia.
	 *
	 * @param detalleVentaDAO DAO de detalles de venta.
	 */
	public DetalleVentaServiceImpl(DetalleVentaDAO detalleVentaDAO) {
		this.detalleVentaDAO = detalleVentaDAO;
	}

	@Override
	public void crearDetalleVenta(DetalleVenta detalleVenta) {
		validarDetalleVenta(detalleVenta, true);
		detalleVenta.setEstado(Estado.activo);
		detalleVentaDAO.save(detalleVenta);
	}

	@Override
	public void modificarDetalleVenta(DetalleVenta detalleVenta) {
		if (detalleVenta == null || detalleVenta.getIdDetalleVenta() == null) {
			throw new ServiceException("El detalle de venta y su ID no pueden ser nulos.");
		}
		DetalleVenta existente = detalleVentaDAO.findById(detalleVenta.getIdDetalleVenta());
		if (existente == null) {
			throw new ServiceException("No existe el detalle de venta a modificar.");
		}
		validarDetalleVenta(detalleVenta, false);
		detalleVentaDAO.update(detalleVenta);
	}

	@Override
	public void eliminarDetalleVenta(int idDetalleVenta) {
		DetalleVenta detalle = detalleVentaDAO.findById(idDetalleVenta);
		if (detalle == null) {
			throw new ServiceException("No existe el detalle de venta a eliminar.");
		}
		if (detalle.getEstado() == Estado.inactivo) {
			throw new ServiceException("El detalle de venta ya está inactivo.");
		}
		detalleVentaDAO.delete(idDetalleVenta); // Baja lógica (update estado)
	}

	@Override
	public DetalleVenta buscarPorId(int idDetalleVenta) {
		DetalleVenta detalle = detalleVentaDAO.findById(idDetalleVenta);
		if (detalle == null) {
			throw new ServiceException("No existe un detalle de venta con ese ID.");
		}
		return detalle;
	}

	@Override
	public List<DetalleVenta> listarTodos() {
		return detalleVentaDAO.findAll();
	}

	@Override
	public List<DetalleVenta> listarPorVenta(int idVenta) {
		if (idVenta <= 0)
			throw new ServiceException("El ID de venta no es válido.");
		return detalleVentaDAO.findByVenta(idVenta);
	}

	@Override
	public List<DetalleVenta> listarPorProducto(int idProducto) {
		if (idProducto <= 0)
			throw new ServiceException("El ID de producto no es válido.");
		return detalleVentaDAO.findByProducto(idProducto);
	}

	@Override
	public DetalleVenta buscarPorProductoYVenta(int idProducto, int idVenta) {
		if (idProducto <= 0 || idVenta <= 0)
			throw new ServiceException("ID de producto o venta no válidos.");
		return detalleVentaDAO.findByProductoAndVenta(idProducto, idVenta);
	}

	@Override
	public List<DetalleVenta> listarPorCantidadMayorQue(int cantidad) {
		if (cantidad < 0)
			throw new ServiceException("La cantidad debe ser positiva.");
		return detalleVentaDAO.findByCantidadGreaterThan(cantidad);
	}

	@Override
	public List<DetalleVenta> listarPorSubtotalSinIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los importes no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return detalleVentaDAO.findBySubtotalSinIvaBetween(min.doubleValue(), max.doubleValue());
	}

	@Override
	public List<DetalleVenta> listarPorSubtotalConIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los importes no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return detalleVentaDAO.findBySubtotalConIvaBetween(min.doubleValue(), max.doubleValue());
	}

	/**
	 * Valida el objeto DetalleVenta según reglas de negocio y estructura de BBDD.
	 *
	 * @param detalleVenta Objeto a validar.
	 * @param esNuevo      True si es alta.
	 * @throws ServiceException si algún dato no cumple las restricciones.
	 */
	private void validarDetalleVenta(DetalleVenta detalleVenta, boolean esNuevo) {
		if (detalleVenta == null)
			throw new ServiceException("El detalle de venta no puede ser nulo.");

		Venta venta = detalleVenta.getVenta();
		if (venta == null || venta.getIdVenta() == null)
			throw new ServiceException("La venta asociada es obligatoria.");

		Producto producto = detalleVenta.getProducto();
		if (producto == null || producto.getIdProducto() == null)
			throw new ServiceException("El producto es obligatorio.");

		if (detalleVenta.getCantidad() == null || detalleVenta.getCantidad() < 1)
			throw new ServiceException("La cantidad debe ser mayor que cero.");

		if (detalleVenta.getPrecioUnitario() == null || detalleVenta.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El precio unitario es obligatorio y no puede ser negativo.");

		if (detalleVenta.getPorcentajeIva() == null || detalleVenta.getPorcentajeIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El porcentaje de IVA es obligatorio y no puede ser negativo.");

		if (detalleVenta.getSubtotalSinIva() == null || detalleVenta.getSubtotalSinIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El subtotal sin IVA es obligatorio y no puede ser negativo.");

		if (detalleVenta.getSubtotalConIva() == null || detalleVenta.getSubtotalConIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El subtotal con IVA es obligatorio y no puede ser negativo.");

		if (detalleVenta.getEstado() == null)
			throw new ServiceException("El estado es obligatorio.");
	}
}
