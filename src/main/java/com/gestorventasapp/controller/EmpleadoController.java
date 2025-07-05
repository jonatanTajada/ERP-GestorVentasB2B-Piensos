package com.gestorventasapp.controller;

import com.gestorventasapp.model.Empleado;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.service.EmpleadoService;
import com.gestorventasapp.exceptions.ControllerException;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la gestión de empleados. Orquesta la interacción entre la
 * vista y la capa de servicios, validando la entrada y gestionando los
 * resultados y errores.
 */
public class EmpleadoController {

	private final EmpleadoService empleadoService;

	/**
	 * Constructor con inyección de dependencias.
	 * 
	 * @param empleadoService Servicio de empleados.
	 */
	public EmpleadoController(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}

	/** Da de alta un nuevo empleado. */
	public void crearEmpleado(Empleado empleado) {
		if (empleado == null)
			throw new ControllerException("El empleado no puede ser nulo.");
		if (empleado.getDni() == null || empleado.getDni().trim().isEmpty())
			throw new ControllerException("El DNI es obligatorio.");
		if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty())
			throw new ControllerException("El nombre es obligatorio.");
		if (empleado.getApellido1() == null || empleado.getApellido1().trim().isEmpty())
			throw new ControllerException("El primer apellido es obligatorio.");
		if (empleado.getCodigoPostal() == null || empleado.getCodigoPostal().trim().isEmpty())
			throw new ControllerException("El código postal es obligatorio.");
		if (empleado.getTelefono() == null || empleado.getTelefono().trim().isEmpty())
			throw new ControllerException("El teléfono es obligatorio.");
		if (empleado.getEmail() == null || empleado.getEmail().trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");
		if (empleado.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");

		try {
			empleadoService.crearEmpleado(empleado);
		} catch (Exception e) {
			throw new ControllerException("Error al crear el empleado: " + e.getMessage(), e);
		}
	}

	/** Modifica un empleado existente. */
	public void modificarEmpleado(Empleado empleado) {
		if (empleado == null || empleado.getIdEmpleado() == null)
			throw new ControllerException("El empleado y su ID no pueden ser nulos.");
		if (empleado.getDni() == null || empleado.getDni().trim().isEmpty())
			throw new ControllerException("El DNI es obligatorio.");
		if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty())
			throw new ControllerException("El nombre es obligatorio.");
		if (empleado.getApellido1() == null || empleado.getApellido1().trim().isEmpty())
			throw new ControllerException("El primer apellido es obligatorio.");
		if (empleado.getCodigoPostal() == null || empleado.getCodigoPostal().trim().isEmpty())
			throw new ControllerException("El código postal es obligatorio.");
		if (empleado.getTelefono() == null || empleado.getTelefono().trim().isEmpty())
			throw new ControllerException("El teléfono es obligatorio.");
		if (empleado.getEmail() == null || empleado.getEmail().trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");
		if (empleado.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");

		try {
			empleadoService.modificarEmpleado(empleado);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar el empleado: " + e.getMessage(), e);
		}
	}

	/** Baja lógica (inactivo) de un empleado. */
	public void darBajaLogicaEmpleado(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ControllerException("El ID del empleado debe ser válido.");
		try {
			empleadoService.darBajaLogicaEmpleado(idEmpleado);
		} catch (Exception e) {
			throw new ControllerException("Error al dar de baja el empleado: " + e.getMessage(), e);
		}
	}

	/** Busca empleado por ID. */
	public Empleado buscarPorId(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ControllerException("El ID del empleado debe ser válido.");
		try {
			return empleadoService.buscarPorId(idEmpleado);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el empleado: " + e.getMessage(), e);
		}
	}

	/** Busca empleado por DNI. */
	public Empleado buscarPorDni(String dni) {
		if (dni == null || dni.trim().isEmpty())
			throw new ControllerException("El DNI es obligatorio.");
		try {
			return empleadoService.buscarPorDni(dni);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el empleado por DNI: " + e.getMessage(), e);
		}
	}

	/** Busca empleado por email. */
	public Empleado buscarPorEmail(String email) {
		if (email == null || email.trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");
		try {
			return empleadoService.buscarPorEmail(email);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el empleado por email: " + e.getMessage(), e);
		}
	}

	/** Lista todos los empleados. */
	public List<Empleado> listarTodos() {
		try {
			return empleadoService.listarTodos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los empleados: " + e.getMessage(), e);
		}
	}

	/** Lista solo los empleados activos. */
	public List<Empleado> listarActivos() {
		try {
			return empleadoService.listarActivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los empleados activos: " + e.getMessage(), e);
		}
	}

	/** Lista solo los empleados inactivos. */
	public List<Empleado> listarInactivos() {
		try {
			return empleadoService.listarInactivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los empleados inactivos: " + e.getMessage(), e);
		}
	}

	/** Lista empleados por estado. */
	public List<Empleado> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ControllerException("El estado no puede ser nulo.");
		try {
			return empleadoService.listarPorEstado(estado);
		} catch (Exception e) {
			throw new ControllerException("Error al listar empleados por estado: " + e.getMessage(), e);
		}
	}

	/** Busca empleados por nombre. */
	public List<Empleado> buscarPorNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty())
			throw new ControllerException("El nombre es obligatorio para la búsqueda.");
		try {
			return empleadoService.buscarPorNombre(nombre);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar empleados por nombre: " + e.getMessage(), e);
		}
	}

	/** Busca empleados por apellido. */
	public List<Empleado> buscarPorApellido(String apellido) {
		if (apellido == null || apellido.trim().isEmpty())
			throw new ControllerException("El apellido es obligatorio para la búsqueda.");
		try {
			return empleadoService.buscarPorApellido(apellido);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar empleados por apellido: " + e.getMessage(), e);
		}
	}

	/** Busca empleados por localidad. */
	public List<Empleado> buscarPorLocalidad(String localidad) {
		if (localidad == null || localidad.trim().isEmpty())
			throw new ControllerException("La localidad es obligatoria para la búsqueda.");
		try {
			return empleadoService.buscarPorLocalidad(localidad);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar empleados por localidad: " + e.getMessage(), e);
		}
	}

	/** Busca empleados dados de alta entre dos fechas. */
	public List<Empleado> buscarPorFechaAlta(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio == null || fechaFin == null)
			throw new ControllerException("Las fechas de inicio y fin son obligatorias.");
		if (fechaFin.isBefore(fechaInicio))
			throw new ControllerException("La fecha de fin no puede ser anterior a la fecha de inicio.");
		try {
			return empleadoService.buscarPorFechaAlta(fechaInicio, fechaFin);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar empleados por fecha de alta: " + e.getMessage(), e);
		}
	}

	/** Verifica si existe un empleado con ese DNI. */
	public boolean existeDni(String dni) {
		if (dni == null || dni.trim().isEmpty())
			throw new ControllerException("El DNI es obligatorio.");
		try {
			return empleadoService.existeDni(dni);
		} catch (Exception e) {
			throw new ControllerException("Error al comprobar existencia de DNI: " + e.getMessage(), e);
		}
	}

	/** Verifica si existe un empleado con ese email. */
	public boolean existeEmail(String email) {
		if (email == null || email.trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");
		try {
			return empleadoService.existeEmail(email);
		} catch (Exception e) {
			throw new ControllerException("Error al comprobar existencia de email: " + e.getMessage(), e);
		}
	}

	/** Busca empleados por teléfono. */
	public List<Empleado> buscarPorTelefono(String telefono) {
		if (telefono == null || telefono.trim().isEmpty())
			throw new ControllerException("El teléfono es obligatorio para la búsqueda.");
		try {
			return empleadoService.buscarPorTelefono(telefono);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar empleados por teléfono: " + e.getMessage(), e);
		}
	}
}
