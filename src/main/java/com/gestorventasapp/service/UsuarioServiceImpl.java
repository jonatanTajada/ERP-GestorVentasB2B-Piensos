package com.gestorventasapp.service;

import com.gestorventasapp.dao.UsuarioDAO;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.enums.TipoUsuario;
import com.gestorventasapp.model.Empleado;
import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.exceptions.ServiceException;

import java.util.List;
import java.util.Objects;

/**
 * Implementación de la lógica de negocio y validaciones para la entidad
 * Usuario. Controla creación, modificación, baja lógica, reactivación, unicidad
 * y login.
 */
public class UsuarioServiceImpl implements UsuarioService {

	private final UsuarioDAO usuarioDAO;

	/**
	 * Constructor con inyección de dependencia.
	 *
	 * @param usuarioDAO DAO de usuarios.
	 */
	public UsuarioServiceImpl(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	@Override
	public void crearUsuario(Usuario usuario) {
		validarUsuario(usuario, true);

		// Control unicidad por nombre_usuario y por empleado (1:1)
		Usuario existenteNombre = usuarioDAO.findByNombreUsuario(usuario.getNombreUsuario());
		Usuario existenteEmpleado = usuarioDAO.findByIdEmpleado(usuario.getEmpleado().getIdEmpleado());

		// Si existe (inactivo) por nombre de usuario o idEmpleado → reactivar y
		// actualizar datos
		if (existenteNombre != null && existenteNombre.getEstado() == Estado.inactivo) {
			actualizarYReactivar(usuario, existenteNombre.getIdUsuario());
			return;
		}
		if (existenteEmpleado != null && existenteEmpleado.getEstado() == Estado.inactivo) {
			actualizarYReactivar(usuario, existenteEmpleado.getIdUsuario());
			return;
		}
		// Si existe y está activo (nombre o idEmpleado), error
		if ((existenteNombre != null && existenteNombre.getEstado() == Estado.activo)
				|| (existenteEmpleado != null && existenteEmpleado.getEstado() == Estado.activo)) {
			throw new ServiceException("Ya existe un usuario activo con ese nombre de usuario o para ese empleado.");
		}

		usuario.setEstado(Estado.activo);
		usuarioDAO.save(usuario);
	}

	@Override
	public void modificarUsuario(Usuario usuario) {
		if (usuario == null || usuario.getIdUsuario() == null) {
			throw new ServiceException("El usuario y su ID no pueden ser nulos.");
		}

		Usuario existente = usuarioDAO.findById(usuario.getIdUsuario());
		if (existente == null) {
			throw new ServiceException("No existe el usuario a modificar.");
		}

		validarUsuario(usuario, false);

		// Control de duplicados de nombre_usuario y de idEmpleado con otros usuarios
		Usuario otroConNombre = usuarioDAO.findByNombreUsuario(usuario.getNombreUsuario());
		if (otroConNombre != null && !Objects.equals(otroConNombre.getIdUsuario(), usuario.getIdUsuario())) {
			throw new ServiceException("Ya existe otro usuario con ese nombre de usuario.");
		}
		Usuario otroConEmpleado = usuarioDAO.findByIdEmpleado(usuario.getEmpleado().getIdEmpleado());
		if (otroConEmpleado != null && !Objects.equals(otroConEmpleado.getIdUsuario(), usuario.getIdUsuario())) {
			throw new ServiceException("Ya existe otro usuario para ese empleado.");
		}

		usuarioDAO.update(usuario);
	}

	@Override
	public void darBajaLogicaUsuario(int idUsuario) {
		Usuario usuario = usuarioDAO.findById(idUsuario);
		if (usuario == null) {
			throw new ServiceException("No existe el usuario a dar de baja.");
		}
		if (usuario.getEstado() == Estado.inactivo) {
			throw new ServiceException("El usuario ya está inactivo.");
		}
		usuario.setEstado(Estado.inactivo);
		usuarioDAO.update(usuario);
	}

	@Override
	public Usuario buscarPorId(int idUsuario) {
		Usuario usuario = usuarioDAO.findById(idUsuario);
		if (usuario == null) {
			throw new ServiceException("No existe un usuario con ese ID.");
		}
		return usuario;
	}

	@Override
	public Usuario buscarPorNombreUsuario(String nombreUsuario) {
		if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
			throw new ServiceException("El nombre de usuario no puede estar vacío.");
		}
		Usuario usuario = usuarioDAO.findByNombreUsuario(nombreUsuario.trim());
		if (usuario == null) {
			throw new ServiceException("No existe un usuario con ese nombre de usuario.");
		}
		return usuario;
	}

	@Override
	public Usuario buscarPorIdEmpleado(int idEmpleado) {
		if (idEmpleado <= 0) {
			throw new ServiceException("El ID de empleado no es válido.");
		}
		Usuario usuario = usuarioDAO.findByIdEmpleado(idEmpleado);
		if (usuario == null) {
			throw new ServiceException("No existe usuario para ese empleado.");
		}
		return usuario;
	}

	@Override
	public List<Usuario> listarTodos() {
		return usuarioDAO.findAll();
	}

	@Override
	public List<Usuario> listarActivos() {
		return usuarioDAO.findAllActivos();
	}

	@Override
	public List<Usuario> listarInactivos() {
		return usuarioDAO.findAllInactivos();
	}

	@Override
	public List<Usuario> listarPorEstado(Estado estado) {
		if (estado == null) {
			throw new ServiceException("El estado no puede ser nulo.");
		}
		return usuarioDAO.findByEstado(estado);
	}

	@Override
	public List<Usuario> listarPorTipo(TipoUsuario tipoUsuario) {
		if (tipoUsuario == null) {
			throw new ServiceException("El tipo de usuario no puede ser nulo.");
		}
		return usuarioDAO.findByTipo(tipoUsuario.name());
	}

	@Override
	public boolean existeNombreUsuario(String nombreUsuario) {
		if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
			throw new ServiceException("El nombre de usuario no puede estar vacío.");
		}
		return usuarioDAO.existsNombreUsuario(nombreUsuario.trim());
	}

	@Override
	public boolean existeIdEmpleado(int idEmpleado) {
		if (idEmpleado <= 0) {
			throw new ServiceException("El ID de empleado no es válido.");
		}
		return usuarioDAO.existsIdEmpleado(idEmpleado);
	}

	@Override
	public boolean checkLogin(String nombreUsuario, String contrasena) {
		if (nombreUsuario == null || nombreUsuario.trim().isEmpty() || contrasena == null || contrasena.isEmpty()) {
			throw new ServiceException("Usuario y contraseña no pueden estar vacíos.");
		}
		return usuarioDAO.checkLogin(nombreUsuario.trim(), contrasena);
	}

	/**
	 * Valida el objeto Usuario según reglas de negocio y estructura BBDD.
	 *
	 * @param usuario Objeto a validar.
	 * @param esNuevo True si es alta.
	 * @throws ServiceException si algún dato no cumple las restricciones.
	 */
	private void validarUsuario(Usuario usuario, boolean esNuevo) {
		if (usuario == null)
			throw new ServiceException("El usuario no puede ser nulo.");
		// Empleado relacionado
		Empleado empleado = usuario.getEmpleado();
		if (empleado == null || empleado.getIdEmpleado() == null) {
			throw new ServiceException("El empleado relacionado es obligatorio.");
		}
		// Nombre de usuario
		String nombreUsuario = usuario.getNombreUsuario();
		if (nombreUsuario == null || nombreUsuario.trim().isEmpty() || nombreUsuario.length() > 50) {
			throw new ServiceException("El nombre de usuario es obligatorio y no puede superar 50 caracteres.");
		}
		// Contraseña (validación básica, el cifrado puede ir en otra capa)
		String contrasena = usuario.getContrasena();
		if (contrasena == null || contrasena.length() < 6 || contrasena.length() > 100
				|| !contrasena.matches("^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,100}$")) {
			throw new ServiceException(
					"La contraseña debe ser alfanumérica, tener al menos una mayúscula, un número y entre 6 y 100 caracteres.");
		}
		// Tipo de usuario
		if (usuario.getTipo() == null) {
			throw new ServiceException("El tipo de usuario es obligatorio.");
		}
		// Estado
		if (usuario.getEstado() == null) {
			throw new ServiceException("El estado es obligatorio.");
		}
	}

	/**
	 * Actualiza datos y reactiva un usuario previamente inactivo.
	 *
	 * @param usuario   Datos nuevos.
	 * @param idUsuario ID del usuario a reactivar.
	 */
	private void actualizarYReactivar(Usuario usuario, Integer idUsuario) {
		usuario.setIdUsuario(idUsuario);
		usuario.setEstado(Estado.activo);
		usuarioDAO.update(usuario);
	}
}
