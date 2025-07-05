package com.gestorventasapp.service;

import com.gestorventasapp.dao.EmpleadoDAO;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.Empleado;
import com.gestorventasapp.service.EmpleadoService;
import com.gestorventasapp.exceptions.ServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Implementación de la lógica de negocio y validaciones para la entidad
 * Empleado. Permite creación, modificación, consulta, baja lógica y
 * reactivación profesional de empleados.
 */
public class EmpleadoServiceImpl implements EmpleadoService {

	private final EmpleadoDAO empleadoDAO;

	/**
	 * Constructor con inyección de dependencia.
	 * 
	 * @param empleadoDAO DAO de empleados.
	 */
	public EmpleadoServiceImpl(EmpleadoDAO empleadoDAO) {
		this.empleadoDAO = empleadoDAO;
	}

	@Override
	public void crearEmpleado(Empleado empleado) {
		validarEmpleado(empleado, true);

		Empleado existenteDni = empleadoDAO.findByDni(empleado.getDni());
		Empleado existenteEmail = empleadoDAO.findByEmail(empleado.getEmail());

		// Si existe por DNI o email y está inactivo, lo reactivamos con los datos
		// nuevos
		if (existenteDni != null && existenteDni.getEstado() == Estado.inactivo) {
			actualizarYReactivar(empleado, existenteDni.getIdEmpleado());
			return;
		}
		if (existenteEmail != null && existenteEmail.getEstado() == Estado.inactivo) {
			actualizarYReactivar(empleado, existenteEmail.getIdEmpleado());
			return;
		}
		// Si existe y está activo, lanzamos excepción
		if ((existenteDni != null && existenteDni.getEstado() == Estado.activo)
				|| (existenteEmail != null && existenteEmail.getEstado() == Estado.activo)) {
			throw new ServiceException("Ya existe un empleado activo con ese DNI o email.");
		}
		empleado.setFechaAlta(LocalDateTime.now());
		empleado.setEstado(Estado.activo);
		empleadoDAO.save(empleado);
	}

	@Override
	public void modificarEmpleado(Empleado empleado) {
		if (empleado == null || empleado.getIdEmpleado() == null) {
			throw new ServiceException("El empleado y su ID no pueden ser nulos.");
		}

		Empleado existente = empleadoDAO.findById(empleado.getIdEmpleado());
		if (existente == null) {
			throw new ServiceException("No existe el empleado a modificar.");
		}

		validarEmpleado(empleado, false);

		// No permitir duplicados de DNI/email con otros empleados
		Empleado conDni = empleadoDAO.findByDni(empleado.getDni());
		if (conDni != null && !Objects.equals(conDni.getIdEmpleado(), empleado.getIdEmpleado())) {
			throw new ServiceException("Ya existe otro empleado con ese DNI.");
		}
		Empleado conEmail = empleadoDAO.findByEmail(empleado.getEmail());
		if (conEmail != null && !Objects.equals(conEmail.getIdEmpleado(), empleado.getIdEmpleado())) {
			throw new ServiceException("Ya existe otro empleado con ese email.");
		}

		empleadoDAO.update(empleado);
	}

	@Override
	public void darBajaLogicaEmpleado(int idEmpleado) {
		Empleado empleado = empleadoDAO.findById(idEmpleado);
		if (empleado == null) {
			throw new ServiceException("No existe el empleado a dar de baja.");
		}
		if (empleado.getEstado() == Estado.inactivo) {
			throw new ServiceException("El empleado ya está inactivo.");
		}
		empleado.setEstado(Estado.inactivo);
		empleadoDAO.update(empleado);
	}

	@Override
	public Empleado buscarPorId(int idEmpleado) {
		Empleado empleado = empleadoDAO.findById(idEmpleado);
		if (empleado == null) {
			throw new ServiceException("No existe un empleado con ese ID.");
		}
		return empleado;
	}

	@Override
	public Empleado buscarPorDni(String dni) {
		if (dni == null || !dni.matches("^[0-9]{8}[A-Z]$")) {
			throw new ServiceException("El formato del DNI no es válido (ejemplo: 12345678A).");
		}
		Empleado empleado = empleadoDAO.findByDni(dni);
		if (empleado == null) {
			throw new ServiceException("No existe un empleado con ese DNI.");
		}
		return empleado;
	}

	@Override
	public Empleado buscarPorEmail(String email) {
		if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
			throw new ServiceException("El formato del email no es válido.");
		}
		Empleado empleado = empleadoDAO.findByEmail(email);
		if (empleado == null) {
			throw new ServiceException("No existe un empleado con ese email.");
		}
		return empleado;
	}

	@Override
	public List<Empleado> listarTodos() {
		return empleadoDAO.findAll();
	}

	@Override
	public List<Empleado> listarActivos() {
		return empleadoDAO.findAllActivos();
	}

	@Override
	public List<Empleado> listarInactivos() {
		return empleadoDAO.findAllInactivos();
	}

	@Override
	public List<Empleado> listarPorEstado(Estado estado) {
		if (estado == null) {
			throw new ServiceException("El estado no puede ser nulo.");
		}
		return empleadoDAO.findByEstado(estado);
	}

	@Override
	public List<Empleado> buscarPorNombre(String nombre) {
		if (nombre == null || nombre.trim().isEmpty()) {
			throw new ServiceException("El nombre no puede estar vacío.");
		}
		return empleadoDAO.findByNombre(nombre.trim());
	}

	@Override
	public List<Empleado> buscarPorApellido(String apellido) {
		if (apellido == null || apellido.trim().isEmpty()) {
			throw new ServiceException("El apellido no puede estar vacío.");
		}
		return empleadoDAO.findByApellido(apellido.trim());
	}

	@Override
	public List<Empleado> buscarPorLocalidad(String localidad) {
		if (localidad == null || localidad.trim().isEmpty()) {
			throw new ServiceException("La localidad no puede estar vacía.");
		}
		return empleadoDAO.findByLocalidad(localidad.trim());
	}

	@Override
	public List<Empleado> buscarPorFechaAlta(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio == null || fechaFin == null) {
			throw new ServiceException("Las fechas no pueden ser nulas.");
		}
		if (fechaFin.isBefore(fechaInicio)) {
			throw new ServiceException("La fecha fin no puede ser anterior a la fecha inicio.");
		}
		return empleadoDAO.findByFechaAlta(fechaInicio, fechaFin);
	}

	@Override
	public boolean existeDni(String dni) {
		if (dni == null || !dni.matches("^[0-9]{8}[A-Z]$")) {
			throw new ServiceException("El formato del DNI no es válido.");
		}
		return empleadoDAO.existsDni(dni);
	}

	@Override
	public boolean existeEmail(String email) {
		if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
			throw new ServiceException("El formato del email no es válido.");
		}
		return empleadoDAO.existsEmail(email);
	}

	@Override
	public List<Empleado> buscarPorTelefono(String telefono) {
		if (telefono == null || !telefono.matches("^[6789][0-9]{8}$")) {
			throw new ServiceException("El teléfono debe tener 9 dígitos y empezar por 6, 7, 8 o 9.");
		}
		return empleadoDAO.findByTelefono(telefono);
	}

	/**
	 * Valida el objeto Empleado según las reglas de negocio y la estructura de la
	 * BBDD.
	 * 
	 * @param empleado Objeto a validar.
	 * @param esNuevo  True si es creación (requiere validar unicidad de DNI/email).
	 * @throws ServiceException Si algún dato no cumple las restricciones.
	 */
	private void validarEmpleado(Empleado empleado, boolean esNuevo) {
		if (empleado == null) {
			throw new ServiceException("El empleado no puede ser nulo.");
		}
		// DNI
		if (empleado.getDni() == null || !empleado.getDni().matches("^[0-9]{8}[A-Z]$")) {
			throw new ServiceException("El DNI debe tener 8 dígitos y una letra mayúscula.");
		}
		// Nombre
		if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()
				|| empleado.getNombre().length() > 50) {
			throw new ServiceException("El nombre es obligatorio y no puede superar 50 caracteres.");
		}
		// Apellido1
		if (empleado.getApellido1() == null || empleado.getApellido1().trim().isEmpty()
				|| empleado.getApellido1().length() > 50) {
			throw new ServiceException("El primer apellido es obligatorio y no puede superar 50 caracteres.");
		}
		// Apellido2 (opcional)
		if (empleado.getApellido2() != null && empleado.getApellido2().length() > 50) {
			throw new ServiceException("El segundo apellido no puede superar 50 caracteres.");
		}
		// Dirección (opcional)
		if (empleado.getDireccion() != null && empleado.getDireccion().length() > 150) {
			throw new ServiceException("La dirección no puede superar 150 caracteres.");
		}
		// Localidad (opcional)
		if (empleado.getLocalidad() != null && empleado.getLocalidad().length() > 80) {
			throw new ServiceException("La localidad no puede superar 80 caracteres.");
		}
		// Código postal
		if (empleado.getCodigoPostal() == null || !empleado.getCodigoPostal().matches("^[0-9]{5}$")) {
			throw new ServiceException("El código postal debe tener exactamente 5 dígitos.");
		}
		// País (opcional)
		if (empleado.getPais() != null && empleado.getPais().length() > 50) {
			throw new ServiceException("El país no puede superar 50 caracteres.");
		}
		// Teléfono
		if (empleado.getTelefono() == null || !empleado.getTelefono().matches("^[6789][0-9]{8}$")) {
			throw new ServiceException("El teléfono debe tener 9 dígitos y empezar por 6, 7, 8 o 9.");
		}
		// Email
		if (empleado.getEmail() == null || empleado.getEmail().length() > 100
				|| !empleado.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
			throw new ServiceException("El email es obligatorio, válido y no puede superar 100 caracteres.");
		}
		// Estado (solo valores válidos)
		if (empleado.getEstado() == null) {
			throw new ServiceException("El estado es obligatorio.");
		}
	}

	/**
	 * Actualiza los datos y reactiva un empleado previamente inactivo.
	 * 
	 * @param empleado   Datos nuevos.
	 * @param idEmpleado ID del empleado a reactivar.
	 */
	private void actualizarYReactivar(Empleado empleado, Integer idEmpleado) {
		empleado.setIdEmpleado(idEmpleado);
		empleado.setFechaAlta(LocalDateTime.now());
		empleado.setEstado(Estado.activo);
		empleadoDAO.update(empleado);
	}
}
