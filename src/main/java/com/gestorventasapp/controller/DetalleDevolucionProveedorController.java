package com.gestorventasapp.controller;

import com.gestorventasapp.model.DetalleDevolucionProveedor;
import com.gestorventasapp.service.DetalleDevolucionProveedorService;
import com.gestorventasapp.exceptions.ControllerException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador para la gestión de detalles de devoluciones a proveedor. Orquesta
 * la interacción entre la vista y la capa de servicios, validando la entrada y
 * gestionando los resultados y errores.
 */
public class DetalleDevolucionProveedorController {

	private final DetalleDevolucionProveedorService detalleService;

	public DetalleDevolucionProveedorController(DetalleDevolucionProveedorService detalleService) {
		this.detalleService = detalleService;
	}

	public void crearDetalleDevolucionProveedor(DetalleDevolucionProveedor detalle) {
		if (detalle == null)
			throw new ControllerException("El detalle no puede ser nulo.");
		if (detalle.getDevolucionProveedor() == null)
			throw new ControllerException("La devolución asociada es obligatoria.");
		if (detalle.getProducto() == null)
			throw new ControllerException("El producto es obligatorio.");
		if (detalle.getCantidad() == null || detalle.getCantidad() <= 0)
			throw new ControllerException("La cantidad debe ser mayor que 0.");
		if (detalle.getPorcentajeIva() == null)
			throw new ControllerException("El porcentaje de IVA es obligatorio.");
		if (detalle.getSubtotalSinIva() == null)
			throw new ControllerException("El subtotal sin IVA es obligatorio.");
		if (detalle.getSubtotalConIva() == null)
			throw new ControllerException("El subtotal con IVA es obligatorio.");
		if (detalle.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			detalleService.crearDetalleDevolucionProveedor(detalle);
		} catch (Exception e) {
			throw new ControllerException("Error al crear el detalle de devolución a proveedor: " + e.getMessage(), e);
		}
	}

	public void modificarDetalleDevolucionProveedor(DetalleDevolucionProveedor detalle) {
		if (detalle == null || detalle.getIdDetalleDevolucionProveedor() == null)
			throw new ControllerException("El detalle y su ID no pueden ser nulos.");
		if (detalle.getDevolucionProveedor() == null)
			throw new ControllerException("La devolución asociada es obligatoria.");
		if (detalle.getProducto() == null)
			throw new ControllerException("El producto es obligatorio.");
		if (detalle.getCantidad() == null || detalle.getCantidad() <= 0)
			throw new ControllerException("La cantidad debe ser mayor que 0.");
		if (detalle.getPorcentajeIva() == null)
			throw new ControllerException("El porcentaje de IVA es obligatorio.");
		if (detalle.getSubtotalSinIva() == null)
			throw new ControllerException("El subtotal sin IVA es obligatorio.");
		if (detalle.getSubtotalConIva() == null)
			throw new ControllerException("El subtotal con IVA es obligatorio.");
		if (detalle.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			detalleService.modificarDetalleDevolucionProveedor(detalle);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar el detalle de devolución a proveedor: " + e.getMessage(),
					e);
		}
	}

	public void eliminarDetalleDevolucionProveedor(int idDetalle) {
		if (idDetalle <= 0)
			throw new ControllerException("El ID del detalle debe ser válido.");
		try {
			detalleService.eliminarDetalleDevolucionProveedor(idDetalle);
		} catch (Exception e) {
			throw new ControllerException("Error al eliminar el detalle de devolución a proveedor: " + e.getMessage(),
					e);
		}
	}

	public DetalleDevolucionProveedor buscarPorId(int idDetalle) {
		if (idDetalle <= 0)
			throw new ControllerException("El ID del detalle debe ser válido.");
		try {
			return detalleService.buscarPorId(idDetalle);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el detalle: " + e.getMessage(), e);
		}
	}

	public List<DetalleDevolucionProveedor> listarTodos() {
		try {
			return detalleService.listarTodos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los detalles: " + e.getMessage(), e);
		}
	}

	public List<DetalleDevolucionProveedor> listarPorDevolucion(int idDevolucion) {
		if (idDevolucion <= 0)
			throw new ControllerException("El ID de la devolución debe ser válido.");
		try {
			return detalleService.listarPorDevolucion(idDevolucion);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por devolución: " + e.getMessage(), e);
		}
	}

	public List<DetalleDevolucionProveedor> listarPorProducto(int idProducto) {
		if (idProducto <= 0)
			throw new ControllerException("El ID del producto debe ser válido.");
		try {
			return detalleService.listarPorProducto(idProducto);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por producto: " + e.getMessage(), e);
		}
	}

	public DetalleDevolucionProveedor buscarPorProductoYDevolucion(int idProducto, int idDevolucion) {
		if (idProducto <= 0 || idDevolucion <= 0)
			throw new ControllerException("El ID de producto y devolución deben ser válidos.");
		try {
			return detalleService.buscarPorProductoYDevolucion(idProducto, idDevolucion);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar detalle por producto y devolución: " + e.getMessage(), e);
		}
	}

	public List<DetalleDevolucionProveedor> listarPorCantidadMayorQue(int cantidad) {
		if (cantidad <= 0)
			throw new ControllerException("La cantidad debe ser mayor que 0.");
		try {
			return detalleService.listarPorCantidadMayorQue(cantidad);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por cantidad: " + e.getMessage(), e);
		}
	}

	public List<DetalleDevolucionProveedor> listarPorSubtotalSinIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de subtotales sin IVA.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los importes no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El importe mínimo no puede ser mayor que el máximo.");
		try {
			return detalleService.listarPorSubtotalSinIvaEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por subtotal sin IVA: " + e.getMessage(), e);
		}
	}

	public List<DetalleDevolucionProveedor> listarPorSubtotalConIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de subtotales con IVA.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los importes no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El importe mínimo no puede ser mayor que el máximo.");
		try {
			return detalleService.listarPorSubtotalConIvaEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por subtotal con IVA: " + e.getMessage(), e);
		}
	}
}
