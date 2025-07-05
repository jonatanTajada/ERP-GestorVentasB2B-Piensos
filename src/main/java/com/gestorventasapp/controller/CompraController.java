package com.gestorventasapp.controller;

import com.gestorventasapp.model.Compra;
import com.gestorventasapp.model.DetalleCompra;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.service.CompraService;
import com.gestorventasapp.exceptions.ControllerException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la gestión de compras. Orquesta la interacción entre la
 * vista y la capa de servicios, validando la entrada y gestionando los
 * resultados y errores.
 */
public class CompraController {

	private final CompraService compraService;

	public CompraController(CompraService compraService) {
		this.compraService = compraService;
	}

	public void crearCompra(Compra compra) {
		if (compra == null)
			throw new ControllerException("La compra no puede ser nula.");
		if (compra.getProveedor() == null)
			throw new ControllerException("El proveedor es obligatorio.");
		if (compra.getEmpleado() == null)
			throw new ControllerException("El empleado es obligatorio.");
		if (compra.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			compraService.crearCompra(compra);
		} catch (Exception e) {
			throw new ControllerException("Error al crear la compra: " + e.getMessage(), e);
		}
	}

	public void modificarCompra(Compra compra) {
		if (compra == null || compra.getIdCompra() == null)
			throw new ControllerException("La compra y su ID no pueden ser nulos.");
		if (compra.getProveedor() == null)
			throw new ControllerException("El proveedor es obligatorio.");
		if (compra.getEmpleado() == null)
			throw new ControllerException("El empleado es obligatorio.");
		if (compra.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			compraService.modificarCompra(compra);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar la compra: " + e.getMessage(), e);
		}
	}

	public void darBajaLogicaCompra(int idCompra) {
		if (idCompra <= 0)
			throw new ControllerException("El ID de la compra debe ser válido.");
		try {
			compraService.darBajaLogicaCompra(idCompra);
		} catch (Exception e) {
			throw new ControllerException("Error al dar de baja la compra: " + e.getMessage(), e);
		}
	}

	public Compra buscarPorId(int idCompra) {
		if (idCompra <= 0)
			throw new ControllerException("El ID de la compra debe ser válido.");
		try {
			return compraService.buscarPorId(idCompra);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar la compra: " + e.getMessage(), e);
		}
	}

	public List<Compra> listarTodas() {
		try {
			return compraService.listarTodas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las compras: " + e.getMessage(), e);
		}
	}

	public List<Compra> listarActivas() {
		try {
			return compraService.listarActivas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las compras activas: " + e.getMessage(), e);
		}
	}

	public List<Compra> listarInactivas() {
		try {
			return compraService.listarInactivas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las compras inactivas: " + e.getMessage(), e);
		}
	}

	public List<Compra> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ControllerException("El estado no puede ser nulo.");
		try {
			return compraService.listarPorEstado(estado);
		} catch (Exception e) {
			throw new ControllerException("Error al listar compras por estado: " + e.getMessage(), e);
		}
	}

	public List<Compra> buscarPorProveedor(int idProveedor) {
		if (idProveedor <= 0)
			throw new ControllerException("El ID del proveedor debe ser válido.");
		try {
			return compraService.buscarPorProveedor(idProveedor);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar compras por proveedor: " + e.getMessage(), e);
		}
	}

	public List<Compra> buscarPorEmpleado(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ControllerException("El ID del empleado debe ser válido.");
		try {
			return compraService.buscarPorEmpleado(idEmpleado);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar compras por empleado: " + e.getMessage(), e);
		}
	}

	public List<Compra> buscarPorFecha(LocalDate fecha) {
		if (fecha == null)
			throw new ControllerException("La fecha es obligatoria.");
		try {
			return compraService.buscarPorFecha(fecha);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar compras por fecha: " + e.getMessage(), e);
		}
	}

	public List<Compra> buscarPorFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio == null || fechaFin == null)
			throw new ControllerException("Las fechas de inicio y fin son obligatorias.");
		if (fechaFin.isBefore(fechaInicio))
			throw new ControllerException("La fecha fin no puede ser anterior a la fecha inicio.");
		try {
			return compraService.buscarPorFechaRango(fechaInicio, fechaFin);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar compras por rango de fechas: " + e.getMessage(), e);
		}
	}

	public List<Compra> buscarPorTotalSinIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de totales sin IVA.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los totales no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El total mínimo no puede ser mayor que el máximo.");
		try {
			return compraService.buscarPorTotalSinIvaEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar compras por total sin IVA: " + e.getMessage(), e);
		}
	}

	public List<Compra> buscarPorTotalConIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de totales con IVA.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los totales no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El total mínimo no puede ser mayor que el máximo.");
		try {
			return compraService.buscarPorTotalConIvaEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar compras por total con IVA: " + e.getMessage(), e);
		}
	}

	public void crearCompraConDetalles(Compra compra, List<DetalleCompra> detallesCompra) {
		if (compra == null)
			throw new ControllerException("La compra no puede ser nula.");
		if (detallesCompra == null || detallesCompra.isEmpty())
			throw new ControllerException("Debe agregar al menos un detalle.");
		try {
			compraService.crearCompraConDetalles(compra, detallesCompra);
		} catch (Exception e) {
			throw new ControllerException("Error al crear la compra con detalles: " + e.getMessage(), e);
		}
	}

}
