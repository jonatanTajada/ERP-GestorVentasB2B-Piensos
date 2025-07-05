package com.gestorventasapp.service;

import com.gestorventasapp.dao.ProveedorDAO;
import com.gestorventasapp.dao.ProveedorDAOImpl;
import com.gestorventasapp.model.Proveedor;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.exceptions.ServiceException;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementación del servicio de proveedores. Realiza validaciones 
 * y gestiona toda la lógica de negocio de Proveedor.
 */
public class ProveedorServiceImpl implements ProveedorService {

	private final ProveedorDAO proveedorDAO;

	public ProveedorServiceImpl(ProveedorDAO proveedorDAO) {
		this.proveedorDAO = proveedorDAO;
	}

	@Override
	public void crearProveedor(Proveedor proveedor) {
		validarProveedor(proveedor, true);
		proveedorDAO.save(proveedor);
	}

	@Override
	public void modificarProveedor(Proveedor proveedor) {
		validarProveedor(proveedor, false);
		proveedorDAO.update(proveedor);
	}

	@Override
	public void darBajaLogicaProveedor(int idProveedor) {
		proveedorDAO.delete(idProveedor);
	}

	@Override
	public Proveedor buscarPorId(int idProveedor) {
		Proveedor p = proveedorDAO.findById(idProveedor);
		if (p == null)
			throw new ServiceException("No existe proveedor con ese ID.");
		return p;
	}

	@Override
	public Proveedor buscarPorCifNif(String cifNif) {
		if (!validaCifNif(cifNif))
			throw new ServiceException("Formato de CIF/NIF inválido.");
		Proveedor p = proveedorDAO.findByCifNif(cifNif);
		if (p == null)
			throw new ServiceException("No existe proveedor con ese CIF/NIF.");
		return p;
	}

	@Override
	public Proveedor buscarPorEmail(String email) {
		if (!validaEmail(email))
			throw new ServiceException("Formato de email inválido.");
		Proveedor p = proveedorDAO.findByEmail(email);
		if (p == null)
			throw new ServiceException("No existe proveedor con ese email.");
		return p;
	}

	@Override
	public List<Proveedor> listarTodos() {
		return proveedorDAO.findAll();
	}

	@Override
	public List<Proveedor> listarActivos() {
		return proveedorDAO.findAllActivos();
	}

	@Override
	public List<Proveedor> listarInactivos() {
		return proveedorDAO.findAllInactivos();
	}

	@Override
	public List<Proveedor> listarPorEstado(Estado estado) {
		return proveedorDAO.findByEstado(estado);
	}

	@Override
	public List<Proveedor> buscarPorRazonSocial(String razonSocial) {
		if (razonSocial == null || razonSocial.trim().isEmpty())
			throw new ServiceException("La razón social no puede estar vacía.");
		return proveedorDAO.findByRazonSocial(razonSocial);
	}

	@Override
	public List<Proveedor> buscarPorFormaJuridica(String formaJuridica) {
		if (formaJuridica == null || formaJuridica.trim().isEmpty())
			throw new ServiceException("La forma jurídica no puede estar vacía.");
		if (!formaJuridica.equals("S.L.") && !formaJuridica.equals("S.A.") && !formaJuridica.equals("Cooperativa")
				&& !formaJuridica.equals("Autónomo"))
			throw new ServiceException("La forma jurídica debe ser S.L., S.A., Cooperativa o Autónomo.");
		return proveedorDAO.findByFormaJuridica(formaJuridica);
	}

	@Override
	public List<Proveedor> buscarPorLocalidad(String localidad) {
		if (localidad == null || localidad.trim().isEmpty())
			throw new ServiceException("La localidad no puede estar vacía.");
		return proveedorDAO.findByLocalidad(localidad);
	}

	@Override
	public boolean existeCifNif(String cifNif) {
		return proveedorDAO.existsCifNif(cifNif);
	}

	@Override
	public boolean existeEmail(String email) {
		return proveedorDAO.existsEmail(email);
	}

	@Override
	public List<Proveedor> buscarPorTelefono(String telefono) {
		if (!validaTelefono(telefono))
			throw new ServiceException("El teléfono debe tener 9 dígitos y empezar por 6, 7, 8 o 9.");
		return proveedorDAO.findByTelefono(telefono);
	}

	@Override
	public List<Proveedor> buscarPorFechaAlta(LocalDate inicio, LocalDate fin) {
		if (inicio == null || fin == null)
			throw new ServiceException("Las fechas de inicio y fin son obligatorias.");
		if (inicio.isAfter(fin))
			throw new ServiceException("La fecha de inicio no puede ser posterior a la fecha de fin.");
		return proveedorDAO.findByFechaAlta(inicio, fin);
	}

	/**
	 * Valida todos los campos obligatorios, patrones y unicidad para Proveedor.
	 * 
	 * @param proveedor Objeto Proveedor a validar
	 * @param esNuevo   true si es alta, false si es modificación
	 * @throws ServiceException si algún dato no cumple las reglas de negocio
	 */
	private void validarProveedor(Proveedor proveedor, boolean esNuevo) {
		if (proveedor == null)
			throw new ServiceException("El objeto Proveedor no puede ser nulo.");
		if (proveedor.getRazonSocial() == null || proveedor.getRazonSocial().trim().isEmpty())
			throw new ServiceException("La razón social es obligatoria.");
		if (proveedor.getRazonSocial().length() > 100)
			throw new ServiceException("La razón social no puede tener más de 100 caracteres.");

		// Validación forma_juridica según ENUM de BBDD
		if (proveedor.getFormaJuridica() == null)
			throw new ServiceException("La forma jurídica es obligatoria.");

		if (proveedor.getCifNif() == null || proveedor.getCifNif().trim().isEmpty())
			throw new ServiceException("El CIF/NIF es obligatorio.");

		if (proveedor.getCifNif().length() > 20)
			throw new ServiceException("El CIF/NIF no puede tener más de 20 caracteres.");

		if (proveedor.getTelefono() == null || !proveedor.getTelefono().matches("^[6789][0-9]{8}$"))
			throw new ServiceException("El teléfono debe tener 9 dígitos y empezar por 6, 7, 8 o 9.");

		if (proveedor.getEmail() == null
				|| !proveedor.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
			throw new ServiceException("El email es obligatorio y debe tener un formato válido.");

		if (proveedor.getDireccion() == null || proveedor.getDireccion().trim().isEmpty())
			throw new ServiceException("La dirección es obligatoria.");
		if (proveedor.getLocalidad() == null || proveedor.getLocalidad().trim().isEmpty())
			throw new ServiceException("La localidad es obligatoria.");

		if (proveedor.getCodigoPostal() != null && proveedor.getCodigoPostal().length() > 10)
			throw new ServiceException("El código postal no puede tener más de 10 caracteres.");

		if (proveedor.getPais() == null || proveedor.getPais().trim().isEmpty())
			throw new ServiceException("El país es obligatorio.");

		// Estado Enum
		if (proveedor.getEstado() == null)
			throw new ServiceException("El estado es obligatorio.");
		if (proveedor.getEstado() != Estado.activo && proveedor.getEstado() != Estado.inactivo)
			throw new ServiceException("El estado solo puede ser activo o inactivo.");

		// Duplicados si es alta
		if (esNuevo) {
			if (proveedorDAO.existsCifNif(proveedor.getCifNif()))
				throw new ServiceException("Ya existe un proveedor con ese CIF/NIF.");
			if (proveedorDAO.existsEmail(proveedor.getEmail()))
				throw new ServiceException("Ya existe un proveedor con ese email.");
		}
	}

	// Validación de teléfono (9 dígitos, empieza por 6,7,8,9)
	private boolean validaTelefono(String telefono) {
		return telefono != null && telefono.matches("^[6789][0-9]{8}$");
	}

	// Validación de email (según el CHECK de la BBDD)
	private boolean validaEmail(String email) {
		return email != null && email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	}

	// Validación básica de CIF/NIF (puedes mejorar según normativa)
	private boolean validaCifNif(String cifNif) {
		return cifNif != null && cifNif.length() <= 20 && !cifNif.trim().isEmpty();
	}

}
