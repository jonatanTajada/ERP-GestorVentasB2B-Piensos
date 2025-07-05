package com.gestorventasapp.controller;

import com.gestorventasapp.model.DetalleVenta;
import com.gestorventasapp.model.Venta;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.service.VentaService;
import com.gestorventasapp.exceptions.ControllerException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la gestión de ventas. Orquesta la interacción entre la vista
 * y la capa de servicios, validando la entrada y gestionando los resultados y
 * errores.
 */
public class VentaController {

	private final VentaService ventaService;

	public VentaController(VentaService ventaService) {
		this.ventaService = ventaService;
	}

	public void crearVenta(Venta venta) {
		if (venta == null)
			throw new ControllerException("La venta no puede ser nula.");
		if (venta.getCliente() == null)
			throw new ControllerException("El cliente es obligatorio.");
		if (venta.getEmpleado() == null)
			throw new ControllerException("El empleado es obligatorio.");
		if (venta.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			ventaService.crearVenta(venta);
		} catch (Exception e) {
			throw new ControllerException("Error al crear la venta: " + e.getMessage(), e);
		}
	}

	public void modificarVenta(Venta venta) {
		if (venta == null || venta.getIdVenta() == null)
			throw new ControllerException("La venta y su ID no pueden ser nulos.");
		if (venta.getCliente() == null)
			throw new ControllerException("El cliente es obligatorio.");
		if (venta.getEmpleado() == null)
			throw new ControllerException("El empleado es obligatorio.");
		if (venta.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			ventaService.modificarVenta(venta);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar la venta: " + e.getMessage(), e);
		}
	}

	public void darBajaLogicaVenta(int idVenta) {
		if (idVenta <= 0)
			throw new ControllerException("El ID de la venta debe ser válido.");
		try {
			ventaService.darBajaLogicaVenta(idVenta);
		} catch (Exception e) {
			throw new ControllerException("Error al dar de baja la venta: " + e.getMessage(), e);
		}
	}

	public Venta buscarPorId(int idVenta) {
		if (idVenta <= 0)
			throw new ControllerException("El ID de la venta debe ser válido.");
		try {
			return ventaService.buscarPorId(idVenta);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar la venta: " + e.getMessage(), e);
		}
	}

	public List<Venta> listarTodas() {
		try {
			return ventaService.listarTodas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las ventas: " + e.getMessage(), e);
		}
	}

	public List<Venta> listarActivas() {
		try {
			return ventaService.listarActivas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las ventas activas: " + e.getMessage(), e);
		}
	}

	public List<Venta> listarInactivas() {
		try {
			return ventaService.listarInactivas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las ventas inactivas: " + e.getMessage(), e);
		}
	}

	public List<Venta> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ControllerException("El estado no puede ser nulo.");
		try {
			return ventaService.listarPorEstado(estado);
		} catch (Exception e) {
			throw new ControllerException("Error al listar ventas por estado: " + e.getMessage(), e);
		}
	}

	public List<Venta> buscarPorCliente(int idCliente) {
		if (idCliente <= 0)
			throw new ControllerException("El ID del cliente debe ser válido.");
		try {
			return ventaService.buscarPorCliente(idCliente);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar ventas por cliente: " + e.getMessage(), e);
		}
	}

	public List<Venta> buscarPorEmpleado(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ControllerException("El ID del empleado debe ser válido.");
		try {
			return ventaService.buscarPorEmpleado(idEmpleado);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar ventas por empleado: " + e.getMessage(), e);
		}
	}

	public List<Venta> buscarPorFecha(LocalDate fecha) {
		if (fecha == null)
			throw new ControllerException("La fecha es obligatoria.");
		try {
			return ventaService.buscarPorFecha(fecha);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar ventas por fecha: " + e.getMessage(), e);
		}
	}

	public List<Venta> buscarPorFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio == null || fechaFin == null)
			throw new ControllerException("Las fechas de inicio y fin son obligatorias.");
		if (fechaFin.isBefore(fechaInicio))
			throw new ControllerException("La fecha fin no puede ser anterior a la fecha inicio.");
		try {
			return ventaService.buscarPorFechaRango(fechaInicio, fechaFin);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar ventas por rango de fechas: " + e.getMessage(), e);
		}
	}

	public List<Venta> buscarPorTotalSinIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de totales sin IVA.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los totales no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El total mínimo no puede ser mayor que el máximo.");
		try {
			return ventaService.buscarPorTotalSinIvaEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar ventas por total sin IVA: " + e.getMessage(), e);
		}
	}

	public List<Venta> buscarPorTotalConIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ControllerException("Debe indicar el rango de totales con IVA.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ControllerException("Los totales no pueden ser negativos.");
		if (min.compareTo(max) > 0)
			throw new ControllerException("El total mínimo no puede ser mayor que el máximo.");
		try {
			return ventaService.buscarPorTotalConIvaEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar ventas por total con IVA: " + e.getMessage(), e);
		}
	}

	/**
	 * Crea una venta junto con sus detalles (transacción completa).
	 * 
	 * @param venta         Venta a crear.
	 * @param detallesVenta Lista de detalles de la venta.
	 */
	public void crearVentaConDetalles(Venta venta, List<DetalleVenta> detallesVenta) {
		if (venta == null)
			throw new ControllerException("La venta no puede ser nula.");
		if (detallesVenta == null || detallesVenta.isEmpty())
			throw new ControllerException("Debe añadir al menos un detalle de venta.");
		try {
			ventaService.crearVentaConDetalles(venta, detallesVenta);
		} catch (Exception e) {
			throw new ControllerException("Error al crear la venta con detalles: " + e.getMessage(), e);
		}
	}

}
