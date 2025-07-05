package com.gestorventasapp.controller;

import com.gestorventasapp.model.DevolucionProveedor;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.service.DevolucionProveedorService;
import com.gestorventasapp.exceptions.ControllerException;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la gestión de devoluciones a proveedores. Orquesta la
 * interacción entre la vista y la capa de servicios, validando la entrada y
 * gestionando los resultados y errores.
 */
public class DevolucionProveedorController {

	private final DevolucionProveedorService devolucionProveedorService;

	public DevolucionProveedorController(DevolucionProveedorService devolucionProveedorService) {
		this.devolucionProveedorService = devolucionProveedorService;
	}

	public void crearDevolucion(DevolucionProveedor devolucion) {
		if (devolucion == null)
			throw new ControllerException("La devolución no puede ser nula.");
		if (devolucion.getCompra() == null)
			throw new ControllerException("La compra asociada es obligatoria.");
		if (devolucion.getEmpleado() == null)
			throw new ControllerException("El empleado es obligatorio.");
		if (devolucion.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			devolucionProveedorService.crearDevolucion(devolucion);
		} catch (Exception e) {
			throw new ControllerException("Error al crear la devolución: " + e.getMessage(), e);
		}
	}

	public void modificarDevolucion(DevolucionProveedor devolucion) {
		if (devolucion == null || devolucion.getIdDevolucionProveedor() == null)
			throw new ControllerException("La devolución y su ID no pueden ser nulos.");
		if (devolucion.getCompra() == null)
			throw new ControllerException("La compra asociada es obligatoria.");
		if (devolucion.getEmpleado() == null)
			throw new ControllerException("El empleado es obligatorio.");
		if (devolucion.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			devolucionProveedorService.modificarDevolucion(devolucion);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar la devolución: " + e.getMessage(), e);
		}
	}

	public void darBajaLogicaDevolucion(int idDevolucion) {
		if (idDevolucion <= 0)
			throw new ControllerException("El ID de la devolución debe ser válido.");
		try {
			devolucionProveedorService.darBajaLogicaDevolucion(idDevolucion);
		} catch (Exception e) {
			throw new ControllerException("Error al dar de baja la devolución: " + e.getMessage(), e);
		}
	}

	public DevolucionProveedor buscarPorId(int idDevolucion) {
		if (idDevolucion <= 0)
			throw new ControllerException("El ID de la devolución debe ser válido.");
		try {
			return devolucionProveedorService.buscarPorId(idDevolucion);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar la devolución: " + e.getMessage(), e);
		}
	}

	public List<DevolucionProveedor> listarTodas() {
		try {
			return devolucionProveedorService.listarTodas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las devoluciones: " + e.getMessage(), e);
		}
	}

	public List<DevolucionProveedor> listarActivas() {
		try {
			return devolucionProveedorService.listarActivas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las devoluciones activas: " + e.getMessage(), e);
		}
	}

	public List<DevolucionProveedor> listarInactivas() {
		try {
			return devolucionProveedorService.listarInactivas();
		} catch (Exception e) {
			throw new ControllerException("Error al listar las devoluciones inactivas: " + e.getMessage(), e);
		}
	}

	public List<DevolucionProveedor> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ControllerException("El estado no puede ser nulo.");
		try {
			return devolucionProveedorService.listarPorEstado(estado);
		} catch (Exception e) {
			throw new ControllerException("Error al listar devoluciones por estado: " + e.getMessage(), e);
		}
	}

	public List<DevolucionProveedor> buscarPorProveedor(int idProveedor) {
		if (idProveedor <= 0)
			throw new ControllerException("El ID del proveedor debe ser válido.");
		try {
			return devolucionProveedorService.buscarPorProveedor(idProveedor);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por proveedor: " + e.getMessage(), e);
		}
	}

	public List<DevolucionProveedor> buscarPorCompra(int idCompra) {
		if (idCompra <= 0)
			throw new ControllerException("El ID de la compra debe ser válido.");
		try {
			return devolucionProveedorService.buscarPorCompra(idCompra);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por compra: " + e.getMessage(), e);
		}
	}

	public List<DevolucionProveedor> buscarPorEmpleado(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ControllerException("El ID del empleado debe ser válido.");
		try {
			return devolucionProveedorService.buscarPorEmpleado(idEmpleado);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por empleado: " + e.getMessage(), e);
		}
	}

	public List<DevolucionProveedor> buscarPorFecha(LocalDate fecha) {
		if (fecha == null)
			throw new ControllerException("La fecha es obligatoria.");
		try {
			return devolucionProveedorService.buscarPorFecha(fecha);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por fecha: " + e.getMessage(), e);
		}
	}

	public List<DevolucionProveedor> buscarPorFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio == null || fechaFin == null)
			throw new ControllerException("Las fechas de inicio y fin son obligatorias.");
		if (fechaFin.isBefore(fechaInicio))
			throw new ControllerException("La fecha fin no puede ser anterior a la fecha inicio.");
		try {
			return devolucionProveedorService.buscarPorFechaRango(fechaInicio, fechaFin);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por rango de fechas: " + e.getMessage(), e);
		}
	}

	public List<DevolucionProveedor> buscarPorMotivo(String motivo) {
		if (motivo == null || motivo.trim().isEmpty())
			throw new ControllerException("El motivo es obligatorio para la búsqueda.");
		try {
			return devolucionProveedorService.buscarPorMotivo(motivo);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por motivo: " + e.getMessage(), e);
		}
	}

	public List<DevolucionProveedor> buscarPorTotalEntre(double min, double max) {
		if (min < 0 || max < 0)
			throw new ControllerException("Los importes no pueden ser negativos.");
		if (min > max)
			throw new ControllerException("El importe mínimo no puede ser mayor que el máximo.");
		try {
			return devolucionProveedorService.buscarPorTotalEntre(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar devoluciones por importe: " + e.getMessage(), e);
		}
	}
}
