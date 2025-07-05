package com.gestorventasapp.controller;

import com.gestorventasapp.model.DetalleCompra;
import com.gestorventasapp.service.DetalleCompraService;
import com.gestorventasapp.exceptions.ControllerException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador para la gestión de detalles de compra. Orquesta la interacción
 * entre la vista y la capa de servicios, validando la entrada y gestionando los
 * resultados y errores.
 */
public class DetalleCompraController {

	private final DetalleCompraService detalleCompraService;

	public DetalleCompraController(DetalleCompraService detalleCompraService) {
		this.detalleCompraService = detalleCompraService;
	}

	public void crearDetalleCompra(DetalleCompra detalleCompra) {
		if (detalleCompra == null)
			throw new ControllerException("El detalle de compra no puede ser nulo.");
		if (detalleCompra.getCompra() == null)
			throw new ControllerException("La compra asociada es obligatoria.");
		if (detalleCompra.getProducto() == null)
			throw new ControllerException("El producto es obligatorio.");
		if (detalleCompra.getCantidad() == null || detalleCompra.getCantidad() <= 0)
			throw new ControllerException("La cantidad debe ser mayor que 0.");
		if (detalleCompra.getPrecioUnitario() == null)
			throw new ControllerException("El precio unitario es obligatorio.");
		if (detalleCompra.getPorcentajeIva() == null)
			throw new ControllerException("El porcentaje de IVA es obligatorio.");
		if (detalleCompra.getSubtotalSinIva() == null)
			throw new ControllerException("El subtotal sin IVA es obligatorio.");
		if (detalleCompra.getSubtotalConIva() == null)
			throw new ControllerException("El subtotal con IVA es obligatorio.");
		if (detalleCompra.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			detalleCompraService.crearDetalleCompra(detalleCompra);
		} catch (Exception e) {
			throw new ControllerException("Error al crear el detalle de compra: " + e.getMessage(), e);
		}
	}

	public void modificarDetalleCompra(DetalleCompra detalleCompra) {
		if (detalleCompra == null || detalleCompra.getIdDetalleCompra() == null)
			throw new ControllerException("El detalle y su ID no pueden ser nulos.");
		if (detalleCompra.getCompra() == null)
			throw new ControllerException("La compra asociada es obligatoria.");
		if (detalleCompra.getProducto() == null)
			throw new ControllerException("El producto es obligatorio.");
		if (detalleCompra.getCantidad() == null || detalleCompra.getCantidad() <= 0)
			throw new ControllerException("La cantidad debe ser mayor que 0.");
		if (detalleCompra.getPrecioUnitario() == null)
			throw new ControllerException("El precio unitario es obligatorio.");
		if (detalleCompra.getPorcentajeIva() == null)
			throw new ControllerException("El porcentaje de IVA es obligatorio.");
		if (detalleCompra.getSubtotalSinIva() == null)
			throw new ControllerException("El subtotal sin IVA es obligatorio.");
		if (detalleCompra.getSubtotalConIva() == null)
			throw new ControllerException("El subtotal con IVA es obligatorio.");
		if (detalleCompra.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			detalleCompraService.modificarDetalleCompra(detalleCompra);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar el detalle de compra: " + e.getMessage(), e);
		}
	}

	public void eliminarDetalleCompra(int idDetalleCompra) {
		if (idDetalleCompra <= 0)
			throw new ControllerException("El ID del detalle debe ser válido.");
		try {
			detalleCompraService.eliminarDetalleCompra(idDetalleCompra);
		} catch (Exception e) {
			throw new ControllerException("Error al eliminar el detalle de compra: " + e.getMessage(), e);
		}
	}

	public DetalleCompra buscarPorId(int idDetalleCompra) {
		if (idDetalleCompra <= 0)
			throw new ControllerException("El ID del detalle debe ser válido.");
		try {
			return detalleCompraService.buscarPorId(idDetalleCompra);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el detalle de compra: " + e.getMessage(), e);
		}
	}

	public List<DetalleCompra> listarTodos() {
		try {
			return detalleCompraService.listarTodos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los detalles de compra: " + e.getMessage(), e);
		}
	}

	public List<DetalleCompra> listarPorCompra(int idCompra) {
		if (idCompra <= 0)
			throw new ControllerException("El ID de la compra debe ser válido.");
		try {
			return detalleCompraService.listarPorCompra(idCompra);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por compra: " + e.getMessage(), e);
		}
	}

	public List<DetalleCompra> listarPorProducto(int idProducto) {
		if (idProducto <= 0)
			throw new ControllerException("El ID del producto debe ser válido.");
		try {
			return detalleCompraService.listarPorProducto(idProducto);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por producto: " + e.getMessage(), e);
		}
	}

	public DetalleCompra buscarPorProductoYCompra(int idProducto, int idCompra) {
		if (idProducto <= 0 || idCompra <= 0)
			throw new ControllerException("El ID de producto y de compra deben ser válidos.");
		try {
			return detalleCompraService.buscarPorProductoYCompra(idProducto, idCompra);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar detalle por producto y compra: " + e.getMessage(), e);
		}
	}

	public List<DetalleCompra> listarPorCantidadMayorQue(int cantidad) {
		if (cantidad <= 0)
			throw new ControllerException("La cantidad debe ser mayor que 0.");
		try {
			return detalleCompraService.listarPorCantidadMayorQue(cantidad);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por cantidad: " + e.getMessage(), e);
		}
	}

	public List<DetalleCompra> listarPorSubtotalSinIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de subtotales sin IVA.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los importes no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El importe mínimo no puede ser mayor que el máximo.");
		try {
			return detalleCompraService.listarPorSubtotalSinIvaEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por subtotal sin IVA: " + e.getMessage(), e);
		}
	}

	public List<DetalleCompra> listarPorSubtotalConIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de subtotales con IVA.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los importes no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El importe mínimo no puede ser mayor que el máximo.");
		try {
			return detalleCompraService.listarPorSubtotalConIvaEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al listar detalles por subtotal con IVA: " + e.getMessage(), e);
		}
	}
}
