package com.gestorventasapp.service;

import com.gestorventasapp.model.Empleado;
import com.gestorventasapp.enums.Estado;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de empleados. Aplica lógica de negocio, validaciones
 * y reglas profesionales sobre la entidad Empleado.
 */
public interface EmpleadoService {

	/**
	 * Da de alta un nuevo empleado tras validar todos los datos y reglas de
	 * negocio.
	 * 
	 * @param empleado Empleado a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o el
	 *                                                         empleado ya existe
	 *                                                         (DNI/email).
	 */
	void crearEmpleado(Empleado empleado);

	/**
	 * Modifica los datos de un empleado existente tras validar los datos.
	 * 
	 * @param empleado Empleado modificado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o el
	 *                                                         empleado no existe.
	 */
	void modificarEmpleado(Empleado empleado);

	/**
	 * Realiza la baja lógica (estado -> inactivo) de un empleado.
	 * 
	 * @param idEmpleado Identificador del empleado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si el empleado no
	 *                                                         existe.
	 */
	void darBajaLogicaEmpleado(int idEmpleado);

	/**
	 * Busca un empleado por su ID.
	 * 
	 * @param idEmpleado Identificador del empleado.
	 * @return Empleado encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	Empleado buscarPorId(int idEmpleado);

	/**
	 * Busca un empleado por DNI.
	 * 
	 * @param dni DNI del empleado.
	 * @return Empleado encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe o el
	 *                                                         formato es inválido.
	 */
	Empleado buscarPorDni(String dni);

	/**
	 * Busca un empleado por email.
	 * 
	 * @param email Email del empleado.
	 * @return Empleado encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe o el
	 *                                                         formato es inválido.
	 */
	Empleado buscarPorEmail(String email);

	/**
	 * Lista todos los empleados (activos e inactivos).
	 * 
	 * @return Lista completa de empleados.
	 */
	List<Empleado> listarTodos();

	/**
	 * Lista solo los empleados activos.
	 * 
	 * @return Lista de empleados activos.
	 */
	List<Empleado> listarActivos();

	/**
	 * Lista solo los empleados inactivos.
	 * 
	 * @return Lista de empleados inactivos.
	 */
	List<Empleado> listarInactivos();

	/**
	 * Lista empleados por estado (activo/inactivo).
	 * 
	 * @param estado Estado a filtrar.
	 * @return Lista de empleados filtrados por estado.
	 */
	List<Empleado> listarPorEstado(Estado estado);

	/**
	 * Busca empleados por nombre (búsqueda parcial o exacta).
	 * 
	 * @param nombre Nombre o parte del nombre.
	 * @return Lista de empleados que coinciden.
	 */
	List<Empleado> buscarPorNombre(String nombre);

	/**
	 * Busca empleados por apellido (primer o segundo apellido).
	 * 
	 * @param apellido Apellido o parte.
	 * @return Lista de empleados que coinciden.
	 */
	List<Empleado> buscarPorApellido(String apellido);

	/**
	 * Busca empleados por localidad.
	 * 
	 * @param localidad Localidad.
	 * @return Lista de empleados en esa localidad.
	 */
	List<Empleado> buscarPorLocalidad(String localidad);

	/**
	 * Busca empleados dados de alta entre dos fechas (incluidas).
	 * 
	 * @param fechaInicio Fecha de inicio (inclusive).
	 * @param fechaFin    Fecha de fin (inclusive).
	 * @return Lista de empleados dados de alta en ese rango.
	 */
	List<Empleado> buscarPorFechaAlta(LocalDate fechaInicio, LocalDate fechaFin);

	/**
	 * Comprueba si existe un empleado con ese DNI.
	 * 
	 * @param dni DNI a comprobar.
	 * @return true si existe.
	 */
	boolean existeDni(String dni);

	/**
	 * Comprueba si existe un empleado con ese email.
	 * 
	 * @param email Email a comprobar.
	 * @return true si existe.
	 */
	boolean existeEmail(String email);

	/**
	 * Busca empleados por teléfono.
	 * 
	 * @param telefono Teléfono.
	 * @return Lista de empleados con ese teléfono.
	 */
	List<Empleado> buscarPorTelefono(String telefono);
}
