package com.gestorventasapp.controller;

import com.gestorventasapp.model.Cliente;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.service.ClienteService;
import com.gestorventasapp.exceptions.ControllerException;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la gestión de clientes. Orquesta la interacción entre la
 * vista y la capa de servicios, validando la entrada y gestionando los
 * resultados y errores.
 */
public class ClienteController {

	private final ClienteService clienteService;

	/**
	 * Constructor con inyección de dependencias.
	 * 
	 * @param clienteService Servicio de clientes.
	 */
	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	/** Da de alta un nuevo cliente tras validaciones mínimas. */
	public void crearCliente(Cliente cliente) {
		if (cliente == null)
			throw new ControllerException("El cliente no puede ser nulo.");
		if (cliente.getRazonSocial() == null || cliente.getRazonSocial().trim().isEmpty())
			throw new ControllerException("La razón social es obligatoria.");
		if (cliente.getCifNif() == null || cliente.getCifNif().trim().isEmpty())
			throw new ControllerException("El CIF/NIF es obligatorio.");
		if (cliente.getFormaJuridica() == null)
			throw new ControllerException("La forma jurídica es obligatoria.");
		if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty())
			throw new ControllerException("El teléfono es obligatorio.");
		if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");

		try {
			clienteService.crearCliente(cliente);
		} catch (Exception e) {
			throw new ControllerException("Error al crear el cliente: " + e.getMessage(), e);
		}
	}

	/** Modifica un cliente existente. */
	public void modificarCliente(Cliente cliente) {
		if (cliente == null || cliente.getIdCliente() == null)
			throw new ControllerException("El cliente debe tener un ID válido.");
		if (cliente.getRazonSocial() == null || cliente.getRazonSocial().trim().isEmpty())
			throw new ControllerException("La razón social es obligatoria.");
		if (cliente.getCifNif() == null || cliente.getCifNif().trim().isEmpty())
			throw new ControllerException("El CIF/NIF es obligatorio.");
		if (cliente.getFormaJuridica() == null)
			throw new ControllerException("La forma jurídica es obligatoria.");
		if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty())
			throw new ControllerException("El teléfono es obligatorio.");
		if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");

		try {
			clienteService.modificarCliente(cliente);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar el cliente: " + e.getMessage(), e);
		}
	}

	/** Baja lógica (inactivo) de un cliente. */
	public void darBajaLogicaCliente(int idCliente) {
		if (idCliente <= 0)
			throw new ControllerException("El ID del cliente debe ser válido.");
		try {
			clienteService.darBajaLogicaCliente(idCliente);
		} catch (Exception e) {
			throw new ControllerException("Error al dar de baja el cliente: " + e.getMessage(), e);
		}
	}

	/** Busca cliente por ID. */
	public Cliente buscarPorId(int idCliente) {
		if (idCliente <= 0)
			throw new ControllerException("El ID del cliente debe ser válido.");
		try {
			return clienteService.buscarPorId(idCliente);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el cliente: " + e.getMessage(), e);
		}
	}

	/** Busca cliente por CIF/NIF. */
	public Cliente buscarPorCifNif(String cifNif) {
		if (cifNif == null || cifNif.trim().isEmpty())
			throw new ControllerException("El CIF/NIF es obligatorio.");
		try {
			return clienteService.buscarPorCifNif(cifNif);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el cliente por CIF/NIF: " + e.getMessage(), e);
		}
	}

	/** Busca cliente por email. */
	public Cliente buscarPorEmail(String email) {
		if (email == null || email.trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");
		try {
			return clienteService.buscarPorEmail(email);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el cliente por email: " + e.getMessage(), e);
		}
	}

	/** Lista todos los clientes. */
	public List<Cliente> listarTodos() {
		try {
			return clienteService.listarTodos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los clientes: " + e.getMessage(), e);
		}
	}

	/** Lista solo los clientes activos. */
	public List<Cliente> listarActivos() {
		try {
			return clienteService.listarActivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los clientes activos: " + e.getMessage(), e);
		}
	}

	/** Lista solo los clientes inactivos. */
	public List<Cliente> listarInactivos() {
		try {
			return clienteService.listarInactivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los clientes inactivos: " + e.getMessage(), e);
		}
	}

	/** Lista clientes por estado. */
	public List<Cliente> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ControllerException("El estado no puede ser nulo.");
		try {
			return clienteService.listarPorEstado(estado);
		} catch (Exception e) {
			throw new ControllerException("Error al listar clientes por estado: " + e.getMessage(), e);
		}
	}

	/** Busca clientes por razón social (parcial). */
	public List<Cliente> buscarPorRazonSocial(String razonSocial) {
		if (razonSocial == null || razonSocial.trim().isEmpty())
			throw new ControllerException("La razón social es obligatoria para la búsqueda.");
		try {
			return clienteService.buscarPorRazonSocial(razonSocial);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar clientes por razón social: " + e.getMessage(), e);
		}
	}

	/** Busca clientes por forma jurídica. */
	public List<Cliente> buscarPorFormaJuridica(String formaJuridica) {
		if (formaJuridica == null || formaJuridica.trim().isEmpty())
			throw new ControllerException("La forma jurídica es obligatoria para la búsqueda.");
		try {
			return clienteService.buscarPorFormaJuridica(formaJuridica);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar clientes por forma jurídica: " + e.getMessage(), e);
		}
	}

	/** Busca clientes por localidad. */
	public List<Cliente> buscarPorLocalidad(String localidad) {
		if (localidad == null || localidad.trim().isEmpty())
			throw new ControllerException("La localidad es obligatoria para la búsqueda.");
		try {
			return clienteService.buscarPorLocalidad(localidad);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar clientes por localidad: " + e.getMessage(), e);
		}
	}

	/** Busca clientes dados de alta entre dos fechas. */
	public List<Cliente> buscarPorFechaAlta(LocalDate inicio, LocalDate fin) {
		if (inicio == null || fin == null)
			throw new ControllerException("Las fechas de inicio y fin son obligatorias.");
		if (inicio.isAfter(fin))
			throw new ControllerException("La fecha de inicio no puede ser posterior a la fecha de fin.");
		try {
			return clienteService.buscarPorFechaAlta(inicio, fin);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar clientes por fecha de alta: " + e.getMessage(), e);
		}
	}

	/** Verifica si existe un cliente con ese CIF/NIF. */
	public boolean existeCifNif(String cifNif) {
		if (cifNif == null || cifNif.trim().isEmpty())
			throw new ControllerException("El CIF/NIF es obligatorio.");
		try {
			return clienteService.existeCifNif(cifNif);
		} catch (Exception e) {
			throw new ControllerException("Error al comprobar existencia de CIF/NIF: " + e.getMessage(), e);
		}
	}

	/** Verifica si existe un cliente con ese email. */
	public boolean existeEmail(String email) {
		if (email == null || email.trim().isEmpty())
			throw new ControllerException("El email es obligatorio.");
		try {
			return clienteService.existeEmail(email);
		} catch (Exception e) {
			throw new ControllerException("Error al comprobar existencia de email: " + e.getMessage(), e);
		}
	}

	/** Busca clientes por teléfono. */
	public List<Cliente> buscarPorTelefono(String telefono) {
		if (telefono == null || telefono.trim().isEmpty())
			throw new ControllerException("El teléfono es obligatorio para la búsqueda.");
		try {
			return clienteService.buscarPorTelefono(telefono);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar clientes por teléfono: " + e.getMessage(), e);
		}
	}
}
