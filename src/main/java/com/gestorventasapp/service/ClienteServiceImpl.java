package com.gestorventasapp.service;

import com.gestorventasapp.dao.ClienteDAO;
import com.gestorventasapp.dao.ClienteDAOImpl;
import com.gestorventasapp.model.Cliente;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.exceptions.ServiceException;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementación del servicio de clientes. Realiza validaciones exhaustivas y
 * gestiona toda la lógica de negocio de Cliente.
 */
public class ClienteServiceImpl implements ClienteService {

	private final ClienteDAO clienteDAO;

	// Este es el constructor para inyectar el DAO 
	public ClienteServiceImpl(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}

	@Override
	public void crearCliente(Cliente cliente) {
		validarCliente(cliente, true);
		if (clienteDAO.existsCifNif(cliente.getCifNif()))
			throw new ServiceException("Ya existe un cliente con ese CIF/NIF.");
		if (clienteDAO.existsEmail(cliente.getEmail()))
			throw new ServiceException("Ya existe un cliente con ese email.");
		clienteDAO.save(cliente);
	}

	@Override
	public void modificarCliente(Cliente cliente) {
		validarCliente(cliente, false);
		clienteDAO.update(cliente);
	}

	@Override
	public void darBajaLogicaCliente(int idCliente) {
		clienteDAO.delete(idCliente);
	}

	@Override
	public Cliente buscarPorId(int idCliente) {
		Cliente c = clienteDAO.findById(idCliente);
		if (c == null)
			throw new ServiceException("No existe cliente con ese ID.");
		return c;
	}

	@Override
	public Cliente buscarPorCifNif(String cifNif) {
		if (!validaCifNif(cifNif))
			throw new ServiceException("Formato de CIF/NIF inválido.");
		Cliente c = clienteDAO.findByCifNif(cifNif);
		if (c == null)
			throw new ServiceException("No existe cliente con ese CIF/NIF.");
		return c;
	}

	@Override
	public Cliente buscarPorEmail(String email) {
		if (!validaEmail(email))
			throw new ServiceException("Formato de email inválido.");
		Cliente c = clienteDAO.findByEmail(email);
		if (c == null)
			throw new ServiceException("No existe cliente con ese email.");
		return c;
	}

	@Override
	public List<Cliente> listarTodos() {
		return clienteDAO.findAll();
	}

	@Override
	public List<Cliente> listarActivos() {
		return clienteDAO.findAllActivos();
	}

	@Override
	public List<Cliente> listarInactivos() {
		return clienteDAO.findAllInactivos();
	}

	@Override
	public List<Cliente> listarPorEstado(Estado estado) {
		return clienteDAO.findByEstado(estado);
	}

	@Override
	public List<Cliente> buscarPorRazonSocial(String razonSocial) {
		if (razonSocial == null || razonSocial.trim().isEmpty())
			throw new ServiceException("La razón social no puede estar vacía.");
		return clienteDAO.findByRazonSocial(razonSocial);
	}

	@Override
	public List<Cliente> buscarPorFormaJuridica(String formaJuridica) {
		if (formaJuridica == null || formaJuridica.trim().isEmpty())
			throw new ServiceException("La forma jurídica no puede estar vacía.");
		return clienteDAO.findByFormaJuridica(formaJuridica);
	}

	@Override
	public List<Cliente> buscarPorLocalidad(String localidad) {
		if (localidad == null || localidad.trim().isEmpty())
			throw new ServiceException("La localidad no puede estar vacía.");
		return clienteDAO.findByLocalidad(localidad);
	}

	@Override
	public List<Cliente> buscarPorFechaAlta(LocalDate inicio, LocalDate fin) {
		if (inicio == null || fin == null)
			throw new ServiceException("Las fechas no pueden estar vacías.");
		if (inicio.isAfter(fin))
			throw new ServiceException("La fecha de inicio no puede ser posterior a la de fin.");
		return clienteDAO.findByFechaAlta(inicio, fin);
	}

	@Override
	public boolean existeCifNif(String cifNif) {
		return clienteDAO.existsCifNif(cifNif);
	}

	@Override
	public boolean existeEmail(String email) {
		return clienteDAO.existsEmail(email);
	}

	@Override
	public List<Cliente> buscarPorTelefono(String telefono) {
		if (!validaTelefono(telefono))
			throw new ServiceException("El teléfono debe tener exactamente 9 dígitos.");
		return clienteDAO.findByTelefono(telefono);
	}

	/**
	 * Valida todos los campos obligatorios, patrones y unicidad para Cliente.
	 * 
	 * @param cliente Objeto Cliente a validar
	 * @param esNuevo true si es alta, false si es modificación
	 * @throws ServiceException si algún dato no cumple las reglas de negocio
	 */
	private void validarCliente(Cliente cliente, boolean esNuevo) {
		if (cliente == null)
			throw new ServiceException("El objeto Cliente no puede ser nulo.");

		if (cliente.getRazonSocial() == null || cliente.getRazonSocial().trim().isEmpty())
			throw new ServiceException("La razón social es obligatoria.");

		if (cliente.getFormaJuridica() == null)
			throw new ServiceException("La forma jurídica es obligatoria.");

		if (cliente.getCifNif() == null || cliente.getCifNif().trim().isEmpty() || !validaCifNif(cliente.getCifNif()))
			throw new ServiceException("CIF/NIF obligatorio o con formato no válido.");

		if (cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty())
			throw new ServiceException("La dirección es obligatoria.");

		if (cliente.getLocalidad() == null || cliente.getLocalidad().trim().isEmpty())
			throw new ServiceException("La localidad es obligatoria.");

		if (cliente.getCodigoPostal() == null || cliente.getCodigoPostal().trim().isEmpty())
			throw new ServiceException("El código postal es obligatorio.");

		if (!cliente.getCodigoPostal().matches("^\\d{5,10}$"))
			throw new ServiceException("El código postal debe tener entre 5 y 10 dígitos numéricos.");
		if (cliente.getPais() == null || cliente.getPais().trim().isEmpty())
			throw new ServiceException("El país es obligatorio.");

		if (cliente.getTelefono() == null || !validaTelefono(cliente.getTelefono()))
			throw new ServiceException("El teléfono debe tener exactamente 9 dígitos.");

		if (cliente.getEmail() == null || !validaEmail(cliente.getEmail()))
			throw new ServiceException("El email no tiene formato válido.");

		if (cliente.getEstado() == null)
			throw new ServiceException("El estado es obligatorio.");

		if (cliente.getEstado() != Estado.activo && cliente.getEstado() != Estado.inactivo)
			throw new ServiceException("El estado solo puede ser activo o inactivo.");

	}

	// Validación de email
	private boolean validaEmail(String email) {
		return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
	}

	// Validación de teléfono (9 dígitos numéricos)
	private boolean validaTelefono(String telefono) {
		return telefono != null && telefono.matches("^[0-9]{9}$");
	}

	// Validación de CIF/NIF básico (puedes mejorar la regex según normativa)
	private boolean validaCifNif(String cifNif) {
		return cifNif != null && cifNif.matches("^[A-Za-z0-9]{8,10}$");
	}
}
