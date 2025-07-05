package com.gestorventasapp.controller;

import com.gestorventasapp.model.DevolucionCliente;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.service.DevolucionClienteService;
import com.gestorventasapp.exceptions.ControllerException;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la gestión de devoluciones de clientes. Orquesta la
 * interacción entre la vista y la capa de servicios, validando la entrada y
 * gestionando los resultados y errores.
 */
public class DevolucionClienteController {

	private final DevolucionClienteService devolucionClienteService;

	public DevolucionClienteController(DevolucionClienteService devolucionClienteService) {
		this.devolucionClienteService = devolucionClienteService;
	}

	public void crearDevolucion(DevolucionCliente devolucion) {
		if (devolucion == null)
			throw new ControllerException("La devolución no puede ser nula.");
		if (devolucion.getVenta() == null)
			throw new ControllerException("La venta asociada es obligatoria.");
		if (devolucion.getEmpleado() == null)
			throw new ControllerException("El empleado es obligatorio.");
		if (devolucion.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			devolucionClienteService.crearDevolucion(devolucion);
		} catch (Exception e) {
			throw new ControllerException("Error al crear la devolución: " + e.getMessage(), e);
		}
	}

	public void modificarDevolucion(DevolucionCliente devolucion) {
		if (devolucion == null || devolucion.getIdDevolucionCliente() == null)
			throw new ControllerException("La devolución y su ID no pueden ser nulos.");
		if (devolucion.getVenta() == null)
			throw new ControllerException("La venta asociada es obligatoria.");
		if (devolucion.getEmpleado() == null)
			throw new ControllerException("El empleado es obligatorio.");
		if (devolucion.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			devolucionClienteService.modificarDevolucion(devolucion);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar la devolución: " + e.getMessage(), e);
		}
	}

	public void darBajaLogicaDevolucion(int idDevolucion) {
		if (idDevolucion <= 0)
			throw new ControllerException("El ID de la devolución debe ser válido.");
		try {
			devolucionClienteService.darBajaLogicaDevolucion(idDevolucion);
		} catch (Exception e) {
			throw new ControllerException("Error al dar de baja la devolución: " + e.getMessage(), e);
		}
	}

	public DevolucionCliente buscarPorId(int idDevolucion) {
		if (idDevolucion <= 0)
			throw new ControllerException("El ID de la devolución debe ser válido.");
		try {
			return devolucionClienteService.buscarPorId(idDevolucion);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar la devolución: " + e.getMessage(), e);
		}
	}

	public List<DevolucionCliente> listarTodas() {
		try {
			return devolucionClienteService.listarTodas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las devoluciones: " + e.getMessage(), e);
		}
	}

	public List<DevolucionCliente> listarActivas() {
		try {
			return devolucionClienteService.listarActivas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las devoluciones activas: " + e.getMessage(), e);
		}
	}

	public List<DevolucionCliente> listarInactivas() {
		try {
			return devolucionClienteService.listarInactivas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las devoluciones inactivas: " + e.getMessage(), e);
		}
	}

	public List<DevolucionCliente> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ControllerException("El estado no puede ser nulo.");
		try {
			return devolucionClienteService.listarPorEstado(estado);
		} catch (Exception e) {
			throw new ControllerException("Error al listar devoluciones por estado: " + e.getMessage(), e);
		}
	}

	public List<DevolucionCliente> buscarPorCliente(int idCliente) {
		if (idCliente <= 0)
			throw new ControllerException("El ID del cliente debe ser válido.");
		try {
			return devolucionClienteService.buscarPorCliente(idCliente);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por cliente: " + e.getMessage(), e);
		}
	}

	public List<DevolucionCliente> buscarPorVenta(int idVenta) {
		if (idVenta <= 0)
			throw new ControllerException("El ID de la venta debe ser válido.");
		try {
			return devolucionClienteService.buscarPorVenta(idVenta);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por venta: " + e.getMessage(), e);
		}
	}

	public List<DevolucionCliente> buscarPorEmpleado(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ControllerException("El ID del empleado debe ser válido.");
		try {
			return devolucionClienteService.buscarPorEmpleado(idEmpleado);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por empleado: " + e.getMessage(), e);
		}
	}

	public List<DevolucionCliente> buscarPorFecha(LocalDate fecha) {
		if (fecha == null)
			throw new ControllerException("La fecha es obligatoria.");
		try {
			return devolucionClienteService.buscarPorFecha(fecha);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por fecha: " + e.getMessage(), e);
		}
	}

	public List<DevolucionCliente> buscarPorFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio == null || fechaFin == null)
			throw new ControllerException("Las fechas de inicio y fin son obligatorias.");
		if (fechaFin.isBefore(fechaInicio))
			throw new ControllerException("La fecha fin no puede ser anterior a la fecha inicio.");
		try {
			return devolucionClienteService.buscarPorFechaRango(fechaInicio, fechaFin);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por rango de fechas: " + e.getMessage(), e);
		}
	}

	public List<DevolucionCliente> buscarPorMotivo(String motivo) {
		if (motivo == null || motivo.trim().isEmpty())
			throw new ControllerException("El motivo es obligatorio para la búsqueda.");
		try {
			return devolucionClienteService.buscarPorMotivo(motivo);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por motivo: " + e.getMessage(), e);
		}
	}

	public List<DevolucionCliente> buscarPorTotalEntre(double min, double max) {
		if (min < 0 || max < 0)
			throw new ControllerException("Los importes no pueden ser negativos.");
		if (min > max)
			throw new ControllerException("El importe mínimo no puede ser mayor que el máximo.");
		try {
			return devolucionClienteService.buscarPorTotalEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por importe: " + e.getMessage(), e);
		}
	}
}
