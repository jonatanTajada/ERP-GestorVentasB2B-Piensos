package com.gestorventasapp.service;

import com.gestorventasapp.dao.DetalleDevolucionProveedorDAO;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.DetalleDevolucionProveedor;
import com.gestorventasapp.model.DevolucionProveedor;
import com.gestorventasapp.model.Producto;
import com.gestorventasapp.exceptions.ServiceException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementación de la lógica de negocio y validaciones para la entidad
 * DetalleDevolucionProveedor. Aplica bajas lógicas, validaciones y consultas
 * avanzadas.
 */
public class DetalleDevolucionProveedorServiceImpl implements DetalleDevolucionProveedorService {

	private final DetalleDevolucionProveedorDAO detalleDAO;

	/**
	 * Constructor con inyección de dependencia.
	 *
	 * @param detalleDAO DAO de detalles de devolución a proveedor.
	 */
	public DetalleDevolucionProveedorServiceImpl(DetalleDevolucionProveedorDAO detalleDAO) {
		this.detalleDAO = detalleDAO;
	}

	@Override
	public void crearDetalleDevolucionProveedor(DetalleDevolucionProveedor detalle) {
		validarDetalle(detalle, true);
		detalle.setEstado(Estado.activo);
		detalleDAO.save(detalle);
	}

	@Override
	public void modificarDetalleDevolucionProveedor(DetalleDevolucionProveedor detalle) {
		if (detalle == null || detalle.getIdDetalleDevolucionProveedor() == null) {
			throw new ServiceException("El detalle de devolución y su ID no pueden ser nulos.");
		}
		DetalleDevolucionProveedor existente = detalleDAO.findById(detalle.getIdDetalleDevolucionProveedor());
		if (existente == null) {
			throw new ServiceException("No existe el detalle de devolución a modificar.");
		}
		validarDetalle(detalle, false);
		detalleDAO.update(detalle);
	}

	@Override
	public void eliminarDetalleDevolucionProveedor(int idDetalle) {
		DetalleDevolucionProveedor detalle = detalleDAO.findById(idDetalle);
		if (detalle == null) {
			throw new ServiceException("No existe el detalle de devolución a eliminar.");
		}
		if (detalle.getEstado() == Estado.inactivo) {
			throw new ServiceException("El detalle de devolución ya está inactivo.");
		}
		detalleDAO.delete(idDetalle); // Baja lógica (update estado)
	}

	@Override
	public DetalleDevolucionProveedor buscarPorId(int idDetalle) {
		DetalleDevolucionProveedor detalle = detalleDAO.findById(idDetalle);
		if (detalle == null) {
			throw new ServiceException("No existe un detalle de devolución con ese ID.");
		}
		return detalle;
	}

	@Override
	public List<DetalleDevolucionProveedor> listarTodos() {
		return detalleDAO.findAll();
	}

	@Override
	public List<DetalleDevolucionProveedor> listarPorDevolucion(int idDevolucion) {
		if (idDevolucion <= 0)
			throw new ServiceException("El ID de devolución no es válido.");
		return detalleDAO.findByDevolucion(idDevolucion);
	}

	@Override
	public List<DetalleDevolucionProveedor> listarPorProducto(int idProducto) {
		if (idProducto <= 0)
			throw new ServiceException("El ID de producto no es válido.");
		return detalleDAO.findByProducto(idProducto);
	}

	@Override
	public DetalleDevolucionProveedor buscarPorProductoYDevolucion(int idProducto, int idDevolucion) {
		if (idProducto <= 0 || idDevolucion <= 0)
			throw new ServiceException("ID de producto o devolución no válidos.");
		return detalleDAO.findByProductoAndDevolucion(idProducto, idDevolucion);
	}

	@Override
	public List<DetalleDevolucionProveedor> listarPorCantidadMayorQue(int cantidad) {
		if (cantidad < 0)
			throw new ServiceException("La cantidad debe ser positiva.");
		return detalleDAO.findByCantidadGreaterThan(cantidad);
	}

	@Override
	public List<DetalleDevolucionProveedor> listarPorSubtotalSinIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los importes no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return detalleDAO.findBySubtotalSinIvaBetween(min.doubleValue(), max.doubleValue());
	}

	@Override
	public List<DetalleDevolucionProveedor> listarPorSubtotalConIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los importes no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return detalleDAO.findBySubtotalConIvaBetween(min.doubleValue(), max.doubleValue());
	}

	/**
	 * Valida el objeto DetalleDevolucionProveedor según reglas de negocio y
	 * estructura de BBDD.
	 *
	 * @param detalle Objeto a validar.
	 * @param esNuevo True si es alta.
	 * @throws ServiceException si algún dato no cumple las restricciones.
	 */
	private void validarDetalle(DetalleDevolucionProveedor detalle, boolean esNuevo) {
		if (detalle == null)
			throw new ServiceException("El detalle de devolución no puede ser nulo.");

		DevolucionProveedor devolucion = detalle.getDevolucionProveedor();
		if (devolucion == null || devolucion.getIdDevolucionProveedor() == null)
			throw new ServiceException("La devolución asociada es obligatoria.");

		Producto producto = detalle.getProducto();
		if (producto == null || producto.getIdProducto() == null)
			throw new ServiceException("El producto es obligatorio.");

		if (detalle.getCantidad() == null || detalle.getCantidad() < 1)
			throw new ServiceException("La cantidad debe ser mayor que cero.");

		if (detalle.getPorcentajeIva() == null || detalle.getPorcentajeIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El porcentaje de IVA no puede ser negativo.");

		if (detalle.getSubtotalSinIva() == null || detalle.getSubtotalSinIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El subtotal sin IVA no puede ser negativo.");

		if (detalle.getSubtotalConIva() == null || detalle.getSubtotalConIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El subtotal con IVA no puede ser negativo.");

		if (detalle.getEstado() == null)
			throw new ServiceException("El estado es obligatorio.");
	}
}
