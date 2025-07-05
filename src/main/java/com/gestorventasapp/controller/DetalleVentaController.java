package com.gestorventasapp.controller;

import com.gestorventasapp.model.DetalleVenta;
import com.gestorventasapp.service.DetalleVentaService;
import com.gestorventasapp.exceptions.ControllerException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador para la gestión de detalles de venta. Orquesta la interacción
 * entre la vista y la capa de servicios, validando la entrada y gestionando los
 * resultados y errores.
 */
public class DetalleVentaController {

	private final DetalleVentaService detalleVentaService;

	public DetalleVentaController(DetalleVentaService detalleVentaService) {
		this.detalleVentaService = detalleVentaService;
	}

	public void crearDetalleVenta(DetalleVenta detalleVenta) {
		if (detalleVenta == null)
			throw new ControllerException("El detalle de venta no puede ser nulo.");
		if (detalleVenta.getVenta() == null)
			throw new ControllerException("La venta asociada es obligatoria.");
		if (detalleVenta.getProducto() == null)
			throw new ControllerException("El producto es obligatorio.");
		if (detalleVenta.getCantidad() == null || detalleVenta.getCantidad() <= 0)
			throw new ControllerException("La cantidad debe ser mayor que 0.");
		if (detalleVenta.getPrecioUnitario() == null)
			throw new ControllerException("El precio unitario es obligatorio.");
		if (detalleVenta.getPorcentajeIva() == null)
			throw new ControllerException("El porcentaje de IVA es obligatorio.");
		if (detalleVenta.getSubtotalSinIva() == null)
			throw new ControllerException("El subtotal sin IVA es obligatorio.");
		if (detalleVenta.getSubtotalConIva() == null)
			throw new ControllerException("El subtotal con IVA es obligatorio.");
		if (detalleVenta.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			detalleVentaService.crearDetalleVenta(detalleVenta);
		} catch (Exception e) {
			throw new ControllerException("Error al crear el detalle de venta: " + e.getMessage(), e);
		}
	}

	public void modificarDetalleVenta(DetalleVenta detalleVenta) {
		if (detalleVenta == null || detalleVenta.getIdDetalleVenta() == null)
			throw new ControllerException("El detalle y su ID no pueden ser nulos.");
		if (detalleVenta.getVenta() == null)
			throw new ControllerException("La venta asociada es obligatoria.");
		if (detalleVenta.getProducto() == null)
			throw new ControllerException("El producto es obligatorio.");
		if (detalleVenta.getCantidad() == null || detalleVenta.getCantidad() <= 0)
			throw new ControllerException("La cantidad debe ser mayor que 0.");
		if (detalleVenta.getPrecioUnitario() == null)
			throw new ControllerException("El precio unitario es obligatorio.");
		if (detalleVenta.getPorcentajeIva() == null)
			throw new ControllerException("El porcentaje de IVA es obligatorio.");
		if (detalleVenta.getSubtotalSinIva() == null)
			throw new ControllerException("El subtotal sin IVA es obligatorio.");
		if (detalleVenta.getSubtotalConIva() == null)
			throw new ControllerException("El subtotal con IVA es obligatorio.");
		if (detalleVenta.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			detalleVentaService.modificarDetalleVenta(detalleVenta);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar el detalle de venta: " + e.getMessage(), e);
		}
	}

	public void eliminarDetalleVenta(int idDetalleVenta) {
		if (idDetalleVenta <= 0)
			throw new ControllerException("El ID del detalle debe ser válido.");
		try {
			detalleVentaService.eliminarDetalleVenta(idDetalleVenta);
		} catch (Exception e) {
			throw new ControllerException("Error al eliminar el detalle de venta: " + e.getMessage(), e);
		}
	}

	public DetalleVenta buscarPorId(int idDetalleVenta) {
		if (idDetalleVenta <= 0)
			throw new ControllerException("El ID del detalle debe ser válido.");
		try {
			return detalleVentaService.buscarPorId(idDetalleVenta);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el detalle de venta: " + e.getMessage(), e);
		}
	}

	public List<DetalleVenta> listarTodos() {
		try {
			return detalleVentaService.listarTodos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los detalles de venta: " + e.getMessage(), e);
		}
	}

	public List<DetalleVenta> listarPorVenta(int idVenta) {
		if (idVenta <= 0)
			throw new ControllerException("El ID de la venta debe ser válido.");
		try {
			return detalleVentaService.listarPorVenta(idVenta);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por venta: " + e.getMessage(), e);
		}
	}

	public List<DetalleVenta> listarPorProducto(int idProducto) {
		if (idProducto <= 0)
			throw new ControllerException("El ID del producto debe ser válido.");
		try {
			return detalleVentaService.listarPorProducto(idProducto);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por producto: " + e.getMessage(), e);
		}
	}

	public DetalleVenta buscarPorProductoYVenta(int idProducto, int idVenta) {
		if (idProducto <= 0 || idVenta <= 0)
			throw new ControllerException("El ID de producto y de venta deben ser válidos.");
		try {
			return detalleVentaService.buscarPorProductoYVenta(idProducto, idVenta);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar detalle por producto y venta: " + e.getMessage(), e);
		}
	}

	public List<DetalleVenta> listarPorCantidadMayorQue(int cantidad) {
		if (cantidad <= 0)
			throw new ControllerException("La cantidad debe ser mayor que 0.");
		try {
			return detalleVentaService.listarPorCantidadMayorQue(cantidad);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por cantidad: " + e.getMessage(), e);
		}
	}

	public List<DetalleVenta> listarPorSubtotalSinIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de subtotales sin IVA.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los importes no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El importe mínimo no puede ser mayor que el máximo.");
		try {
			return detalleVentaService.listarPorSubtotalSinIvaEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por subtotal sin IVA: " + e.getMessage(), e);
		}
	}

	public List<DetalleVenta> listarPorSubtotalConIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de subtotales con IVA.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los importes no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El importe mínimo no puede ser mayor que el máximo.");
		try {
			return detalleVentaService.listarPorSubtotalConIvaEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por subtotal con IVA: " + e.getMessage(), e);
		}
	}
}
