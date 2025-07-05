package com.gestorventasapp.dao;

import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.enums.Estado;
import java.util.List;

public interface UsuarioDAO {

	void save(Usuario usuario); // Alta de nuevo usuario

	void update(Usuario usuario); // Modificación de usuario existente

	void delete(int idUsuario); // Baja lógica (estado -> inactivo)

	Usuario findById(int idUsuario); // Buscar usuario por ID

	Usuario findByNombreUsuario(String nombreUsuario); // Buscar usuario por nombre de usuario (único, login)

	Usuario findByIdEmpleado(int idEmpleado); // Buscar usuario por id de empleado (relación 1:1)

	List<Usuario> findAll(); // Listar todos los usuarios (activos e inactivos)

	List<Usuario> findAllActivos(); // Listar solo usuarios activos

	List<Usuario> findAllInactivos(); // Listar solo usuarios inactivos

	List<Usuario> findByEstado(Estado estado); // Listar usuarios por estado

	List<Usuario> findByTipo(String tipo); // Buscar usuarios por tipo (admin, usuario...)

	boolean existsNombreUsuario(String nombreUsuario); // Validar existencia de nombre de usuario

	boolean existsIdEmpleado(int idEmpleado); // Validar existencia de usuario para un empleado

	boolean checkLogin(String nombreUsuario, String contrasena); // Validar login 

}
