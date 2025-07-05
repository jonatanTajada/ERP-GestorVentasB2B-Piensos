package com.gestorventasapp.controller;

import com.gestorventasapp.model.Proveedor;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.service.ProveedorService;
import com.gestorventasapp.exceptions.ControllerException;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la gestión de proveedores. Orquesta la interacción entre la
 * vista y la capa de servicios, validando la entrada y gestionando los
 * resultados y errores.
 */
public class ProveedorController {

	private final ProveedorService proveedorService;

	/**
	 * Constructor con inyección de dependencias.
	 * 
	 * @param proveedorService Servicio de proveedores.
	 */
	public ProveedorController(ProveedorService proveedorService) {
		this.proveedorService = proveedorService;
	}

	/** Da de alta un nuevo proveedor tras validaciones mínimas. */
	public void crearProveedor(Proveedor proveedor) {
		if (proveedor == null)
			throw new ControllerException("El proveedor no puede ser nulo.");
		if (proveedor.getRazonSocial() == null || proveedor.getRazonSocial().trim().isEmpty())
			throw new ControllerException("La razón social es obligatoria.");
		if (proveedor.getCifNif() == null || proveedor.getCifNif().trim().isEmpty())
			throw new ControllerException("El CIF/NIF es obligatorio.");
		if (proveedor.getFormaJuridica() == null)
			throw new ControllerException("La forma jurídica es obligatoria.");
		if (proveedor.getTelefono() == null || proveedor.getTelefono().trim().isEmpty())
			throw new ControllerException("El teléfono es obligatorio.");
		if (proveedor.getEmail() == null || proveedor.getEmail().trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");

		try {
			proveedorService.crearProveedor(proveedor);
		} catch (Exception e) {
			throw new ControllerException("Error al crear el proveedor: " + e.getMessage(), e);
		}
	}

	/** Modifica un proveedor existente. */
	public void modificarProveedor(Proveedor proveedor) {
		if (proveedor == null || proveedor.getIdProveedor() == null)
			throw new ControllerException("El proveedor debe tener un ID válido.");
		if (proveedor.getRazonSocial() == null || proveedor.getRazonSocial().trim().isEmpty())
			throw new ControllerException("La razón social es obligatoria.");
		if (proveedor.getCifNif() == null || proveedor.getCifNif().trim().isEmpty())
			throw new ControllerException("El CIF/NIF es obligatorio.");
		if (proveedor.getFormaJuridica() == null)
			throw new ControllerException("La forma jurídica es obligatoria.");
		if (proveedor.getTelefono() == null || proveedor.getTelefono().trim().isEmpty())
			throw new ControllerException("El teléfono es obligatorio.");
		if (proveedor.getEmail() == null || proveedor.getEmail().trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");

		try {
			proveedorService.modificarProveedor(proveedor);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar el proveedor: " + e.getMessage(), e);
		}
	}

	/** Baja lógica (inactivo) de un proveedor. */
	public void darBajaLogicaProveedor(int idProveedor) {
		if (idProveedor <= 0)
			throw new ControllerException("El ID del proveedor debe ser válido.");
		try {
			proveedorService.darBajaLogicaProveedor(idProveedor);
		} catch (Exception e) {
			throw new ControllerException("Error al dar de baja el proveedor: " + e.getMessage(), e);
		}
	}

	/** Busca proveedor por ID. */
	public Proveedor buscarPorId(int idProveedor) {
		if (idProveedor <= 0)
			throw new ControllerException("El ID del proveedor debe ser válido.");
		try {
			return proveedorService.buscarPorId(idProveedor);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el proveedor: " + e.getMessage(), e);
		}
	}

	/** Busca proveedor por CIF/NIF. */
	public Proveedor buscarPorCifNif(String cifNif) {
		if (cifNif == null || cifNif.trim().isEmpty())
			throw new ControllerException("El CIF/NIF es obligatorio.");
		try {
			return proveedorService.buscarPorCifNif(cifNif);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el proveedor por CIF/NIF: " + e.getMessage(), e);
		}
	}

	/** Busca proveedor por email. */
	public Proveedor buscarPorEmail(String email) {
		if (email == null || email.trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");
		try {
			return proveedorService.buscarPorEmail(email);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el proveedor por email: " + e.getMessage(), e);
		}
	}

	/** Lista todos los proveedores. */
	public List<Proveedor> listarTodos() {
		try {
			return proveedorService.listarTodos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los proveedores: " + e.getMessage(), e);
		}
	}

	/** Lista solo los proveedores activos. */
	public List<Proveedor> listarActivos() {
		try {
			return proveedorService.listarActivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los proveedores activos: " + e.getMessage(), e);
		}
	}

	/** Lista solo los proveedores inactivos. */
	public List<Proveedor> listarInactivos() {
		try {
			return proveedorService.listarInactivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los proveedores inactivos: " + e.getMessage(), e);
		}
	}

	/** Lista proveedores por estado. */
	public List<Proveedor> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ControllerException("El estado no puede ser nulo.");
		try {
			return proveedorService.listarPorEstado(estado);
		} catch (Exception e) {
			throw new ControllerException("Error al listar proveedores por estado: " + e.getMessage(), e);
		}
	}

	/** Busca proveedores por razón social (parcial). */
	public List<Proveedor> buscarPorRazonSocial(String razonSocial) {
		if (razonSocial == null || razonSocial.trim().isEmpty())
			throw new ControllerException("La razón social es obligatoria para la búsqueda.");
		try {
			return proveedorService.buscarPorRazonSocial(razonSocial);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar proveedores por razón social: " + e.getMessage(), e);
		}
	}

	/** Busca proveedores por forma jurídica. */
	public List<Proveedor> buscarPorFormaJuridica(String formaJuridica) {
		if (formaJuridica == null || formaJuridica.trim().isEmpty())
			throw new ControllerException("La forma jurídica es obligatoria para la búsqueda.");
		try {
			return proveedorService.buscarPorFormaJuridica(formaJuridica);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar proveedores por forma jurídica: " + e.getMessage(), e);
		}
	}

	/** Busca proveedores por localidad. */
	public List<Proveedor> buscarPorLocalidad(String localidad) {
		if (localidad == null || localidad.trim().isEmpty())
			throw new ControllerException("La localidad es obligatoria para la búsqueda.");
		try {
			return proveedorService.buscarPorLocalidad(localidad);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar proveedores por localidad: " + e.getMessage(), e);
		}
	}

	/** Busca proveedores dados de alta entre dos fechas. */
	public List<Proveedor> buscarPorFechaAlta(LocalDate inicio, LocalDate fin) {
		if (inicio == null || fin == null)
			throw new ControllerException("Las fechas de inicio y fin son obligatorias.");
		if (inicio.isAfter(fin))
			throw new ControllerException("La fecha de inicio no puede ser posterior a la fecha de fin.");
		try {
			return proveedorService.buscarPorFechaAlta(inicio, fin);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar proveedores por fecha de alta: " + e.getMessage(), e);
		}
	}

	/** Verifica si existe un proveedor con ese CIF/NIF. */
	public boolean existeCifNif(String cifNif) {
		if (cifNif == null || cifNif.trim().isEmpty())
			throw new ControllerException("El CIF/NIF es obligatorio.");
		try {
			return proveedorService.existeCifNif(cifNif);
		} catch (Exception e) {
			throw new ControllerException("Error al comprobar existencia de CIF/NIF: " + e.getMessage(), e);
		}
	}

	/** Verifica si existe un proveedor con ese email. */
	public boolean existeEmail(String email) {
		if (email == null || email.trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");
		try {
			return proveedorService.existeEmail(email);
		} catch (Exception e) {
			throw new ControllerException("Error al comprobar existencia de email: " + e.getMessage(), e);
		}
	}

	/** Busca proveedores por teléfono. */
	public List<Proveedor> buscarPorTelefono(String telefono) {
		if (telefono == null || telefono.trim().isEmpty())
			throw new ControllerException("El teléfono es obligatorio para la búsqueda.");
		try {
			return proveedorService.buscarPorTelefono(telefono);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar proveedores por teléfono: " + e.getMessage(), e);
		}
	}
}
