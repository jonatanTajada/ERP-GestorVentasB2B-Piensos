package com.gestorventasapp.controller;

import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.enums.TipoUsuario;
import com.gestorventasapp.service.UsuarioService;
import com.gestorventasapp.exceptions.ControllerException;

import java.util.List;

/**
 * Controlador para la gestión de usuarios. Orquesta la interacción entre la
 * vista y la capa de servicios, validando la entrada y gestionando los
 * resultados y errores.
 */
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void crearUsuario(Usuario usuario) {
		if (usuario == null)
			throw new ControllerException("El usuario no puede ser nulo.");
		if (usuario.getEmpleado() == null)
			throw new ControllerException("El empleado asociado es obligatorio.");
		if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty())
			throw new ControllerException("El nombre de usuario es obligatorio.");
		if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty())
			throw new ControllerException("La contraseña es obligatoria.");
		if (usuario.getTipo() == null)
			throw new ControllerException("El tipo de usuario es obligatorio.");
		if (usuario.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			usuarioService.crearUsuario(usuario);
		} catch (Exception e) {
			throw new ControllerException("Error al crear el usuario: " + e.getMessage(), e);
		}
	}

	public void modificarUsuario(Usuario usuario) {
		if (usuario == null || usuario.getIdUsuario() == null)
			throw new ControllerException("El usuario y su ID no pueden ser nulos.");
		if (usuario.getEmpleado() == null)
			throw new ControllerException("El empleado asociado es obligatorio.");
		if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty())
			throw new ControllerException("El nombre de usuario es obligatorio.");
		if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty())
			throw new ControllerException("La contraseña es obligatoria.");
		if (usuario.getTipo() == null)
			throw new ControllerException("El tipo de usuario es obligatorio.");
		if (usuario.getEstado() == null)
			throw new ControllerException("El estado es obligatorio.");
		try {
			usuarioService.modificarUsuario(usuario);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar el usuario: " + e.getMessage(), e);
		}
	}

	public void darBajaLogicaUsuario(int idUsuario) {
		if (idUsuario <= 0)
			throw new ControllerException("El ID del usuario debe ser válido.");
		try {
			usuarioService.darBajaLogicaUsuario(idUsuario);
		} catch (Exception e) {
			throw new ControllerException("Error al dar de baja el usuario: " + e.getMessage(), e);
		}
	}

	public Usuario buscarPorId(int idUsuario) {
		if (idUsuario <= 0)
			throw new ControllerException("El ID del usuario debe ser válido.");
		try {
			return usuarioService.buscarPorId(idUsuario);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el usuario: " + e.getMessage(), e);
		}
	}

	public Usuario buscarPorNombreUsuario(String nombreUsuario) {
		if (nombreUsuario == null || nombreUsuario.trim().isEmpty())
			throw new ControllerException("El nombre de usuario es obligatorio.");
		try {
			return usuarioService.buscarPorNombreUsuario(nombreUsuario);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el usuario por nombre de usuario: " + e.getMessage(), e);
		}
	}

	public Usuario buscarPorIdEmpleado(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ControllerException("El ID de empleado debe ser válido.");
		try {
			return usuarioService.buscarPorIdEmpleado(idEmpleado);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el usuario por ID de empleado: " + e.getMessage(), e);
		}
	}

	public List<Usuario> listarTodos() {
		try {
			return usuarioService.listarTodos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los usuarios: " + e.getMessage(), e);
		}
	}

	public List<Usuario> listarActivos() {
		try {
			return usuarioService.listarActivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los usuarios activos: " + e.getMessage(), e);
		}
	}

	public List<Usuario> listarInactivos() {
		try {
			return usuarioService.listarInactivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los usuarios inactivos: " + e.getMessage(), e);
		}
	}

	public List<Usuario> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ControllerException("El estado no puede ser nulo.");
		try {
			return usuarioService.listarPorEstado(estado);
		} catch (Exception e) {
			throw new ControllerException("Error al listar usuarios por estado: " + e.getMessage(), e);
		}
	}

	public List<Usuario> listarPorTipo(TipoUsuario tipoUsuario) {
		if (tipoUsuario == null)
			throw new ControllerException("El tipo de usuario no puede ser nulo.");
		try {
			return usuarioService.listarPorTipo(tipoUsuario);
		} catch (Exception e) {
			throw new ControllerException("Error al listar usuarios por tipo: " + e.getMessage(), e);
		}
	}

	public boolean existeNombreUsuario(String nombreUsuario) {
		if (nombreUsuario == null || nombreUsuario.trim().isEmpty())
			throw new ControllerException("El nombre de usuario es obligatorio.");
		try {
			return usuarioService.existeNombreUsuario(nombreUsuario);
		} catch (Exception e) {
			throw new ControllerException("Error al comprobar existencia de nombre de usuario: " + e.getMessage(), e);
		}
	}

	public boolean existeIdEmpleado(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ControllerException("El ID de empleado debe ser válido.");
		try {
			return usuarioService.existeIdEmpleado(idEmpleado);
		} catch (Exception e) {
			throw new ControllerException("Error al comprobar existencia de usuario para empleado: " + e.getMessage(),
					e);
		}
	}

	public boolean checkLogin(String nombreUsuario, String contrasena) {
		if (nombreUsuario == null || nombreUsuario.trim().isEmpty())
			throw new ControllerException("El nombre de usuario es obligatorio.");
		if (contrasena == null || contrasena.trim().isEmpty())
			throw new ControllerException("La contraseña es obligatoria.");
		try {
			return usuarioService.checkLogin(nombreUsuario, contrasena);
		} catch (Exception e) {
			throw new ControllerException("Error al validar login de usuario: " + e.getMessage(), e);
		}
	}
}
