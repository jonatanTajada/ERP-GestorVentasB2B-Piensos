package com.gestorventasapp.service;

import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.enums.TipoUsuario;

import java.util.List;

/**
 * Servicio para la gestión de usuarios del sistema. Aplica la lógica de
 * negocio, validaciones y seguridad sobre la entidad Usuario.
 */
public interface UsuarioService {

	/**
	 * Da de alta un nuevo usuario, validando todas las reglas de negocio. Si el
	 * empleado ya tenía usuario inactivo, se reactiva con los nuevos datos.
	 *
	 * @param usuario Usuario a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos, ya existe
	 *                                                         el nombre de usuario,
	 *                                                         o el empleado ya
	 *                                                         tiene usuario activo.
	 */
	void crearUsuario(Usuario usuario);

	/**
	 * Modifica los datos de un usuario existente, validando datos y unicidad.
	 *
	 * @param usuario Usuario modificado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos, el usuario
	 *                                                         no existe o se
	 *                                                         produce duplicidad.
	 */
	void modificarUsuario(Usuario usuario);

	/**
	 * Realiza la baja lógica (estado -> inactivo) de un usuario.
	 *
	 * @param idUsuario Identificador del usuario.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	void darBajaLogicaUsuario(int idUsuario);

	/**
	 * Busca un usuario por su ID.
	 *
	 * @param idUsuario Identificador.
	 * @return Usuario encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	Usuario buscarPorId(int idUsuario);

	/**
	 * Busca un usuario por su nombre de usuario (login).
	 *
	 * @param nombreUsuario Nombre de usuario.
	 * @return Usuario encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe o
	 *                                                         formato inválido.
	 */
	Usuario buscarPorNombreUsuario(String nombreUsuario);

	/**
	 * Busca un usuario por el ID de empleado.
	 *
	 * @param idEmpleado ID de empleado relacionado.
	 * @return Usuario encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	Usuario buscarPorIdEmpleado(int idEmpleado);

	/**
	 * Lista todos los usuarios (activos e inactivos).
	 *
	 * @return Lista completa de usuarios.
	 */
	List<Usuario> listarTodos();

	/**
	 * Lista solo los usuarios activos.
	 *
	 * @return Lista de usuarios activos.
	 */
	List<Usuario> listarActivos();

	/**
	 * Lista solo los usuarios inactivos.
	 *
	 * @return Lista de usuarios inactivos.
	 */
	List<Usuario> listarInactivos();

	/**
	 * Lista los usuarios por estado (activo/inactivo).
	 *
	 * @param estado Estado a filtrar.
	 * @return Lista de usuarios filtrados por estado.
	 */
	List<Usuario> listarPorEstado(Estado estado);

	/**
	 * Lista los usuarios por tipo (admin, usuario...).
	 *
	 * @param tipoUsuario Tipo de usuario.
	 * @return Lista de usuarios de ese tipo.
	 */
	List<Usuario> listarPorTipo(TipoUsuario tipoUsuario);

	/**
	 * Comprueba si existe el nombre de usuario (único).
	 *
	 * @param nombreUsuario Nombre de usuario.
	 * @return true si existe.
	 */
	boolean existeNombreUsuario(String nombreUsuario);

	/**
	 * Comprueba si existe usuario asignado a un empleado concreto.
	 *
	 * @param idEmpleado ID de empleado.
	 * @return true si existe.
	 */
	boolean existeIdEmpleado(int idEmpleado);

	/**
	 * Valida el login de usuario por nombre de usuario y contraseña.
	 * 
	 * @param nombreUsuario Nombre de usuario.
	 * @param contrasena    Contraseña (ya cifrada o en texto plano según tu
	 *                      política).
	 * @return true si el login es válido.
	 */
	boolean checkLogin(String nombreUsuario, String contrasena);

}
