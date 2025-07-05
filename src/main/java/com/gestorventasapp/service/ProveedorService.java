package com.gestorventasapp.service;

import com.gestorventasapp.model.Proveedor;
import com.gestorventasapp.enums.Estado;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de proveedores. Aplica la lógica de negocio y
 * validaciones sobre la entidad Proveedor.
 */
public interface ProveedorService {

	/**
	 * Da de alta un nuevo proveedor tras validar todos los datos y reglas de
	 * negocio.
	 * 
	 * @param proveedor Proveedor a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o ya
	 *                                                         existe.
	 */
	void crearProveedor(Proveedor proveedor);

	/**
	 * Modifica un proveedor existente tras validar todos los datos.
	 * 
	 * @param proveedor Proveedor modificado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos.
	 */
	void modificarProveedor(Proveedor proveedor);

	/**
	 * Realiza la baja lógica (estado->inactivo) de un proveedor.
	 * 
	 * @param idProveedor Identificador del proveedor.
	 */
	void darBajaLogicaProveedor(int idProveedor);

	/**
	 * Busca un proveedor por su ID.
	 * 
	 * @param idProveedor Identificador.
	 * @return Proveedor encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	Proveedor buscarPorId(int idProveedor);

	/**
	 * Busca un proveedor por su CIF/NIF.
	 * 
	 * @param cifNif CIF o NIF.
	 * @return Proveedor encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe o
	 *                                                         formato inválido.
	 */
	Proveedor buscarPorCifNif(String cifNif);

	/**
	 * Busca un proveedor por email.
	 * 
	 * @param email Email del proveedor.
	 * @return Proveedor encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe o
	 *                                                         formato inválido.
	 */
	Proveedor buscarPorEmail(String email);

	/**
	 * Lista todos los proveedores (activos e inactivos).
	 * 
	 * @return Lista completa de proveedores.
	 */
	List<Proveedor> listarTodos();

	/**
	 * Lista solo los proveedores activos.
	 * 
	 * @return Lista de proveedores activos.
	 */
	List<Proveedor> listarActivos();

	/**
	 * Lista solo los proveedores inactivos.
	 * 
	 * @return Lista de proveedores inactivos.
	 */
	List<Proveedor> listarInactivos();

	/**
	 * Lista los proveedores por estado.
	 * 
	 * @param estado Estado a filtrar.
	 * @return Lista de proveedores filtrados por estado.
	 */
	List<Proveedor> listarPorEstado(Estado estado);

	/**
	 * Busca proveedores por razón social (búsqueda parcial).
	 * 
	 * @param razonSocial Razón social o parte.
	 * @return Lista de proveedores que coinciden.
	 */
	List<Proveedor> buscarPorRazonSocial(String razonSocial);

	/**
	 * Busca proveedores por forma jurídica.
	 * 
	 * @param formaJuridica Forma jurídica (S.L., S.A., Cooperativa, Autónomo).
	 * @return Lista de proveedores que coinciden.
	 */
	List<Proveedor> buscarPorFormaJuridica(String formaJuridica);

	/**
	 * Busca proveedores por localidad.
	 * 
	 * @param localidad Localidad.
	 * @return Lista de proveedores que coinciden.
	 */
	List<Proveedor> buscarPorLocalidad(String localidad);

	/**
	 * Comprueba si existe un proveedor con ese CIF/NIF.
	 * 
	 * @param cifNif CIF o NIF.
	 * @return true si existe.
	 */
	boolean existeCifNif(String cifNif);

	/**
	 * Comprueba si existe un proveedor con ese email.
	 * 
	 * @param email Email.
	 * @return true si existe.
	 */
	boolean existeEmail(String email);

	/**
	 * Busca proveedores por teléfono.
	 * 
	 * @param telefono Teléfono.
	 * @return Lista de proveedores.
	 */
	List<Proveedor> buscarPorTelefono(String telefono);

	/**
	 * Busca proveedores dados de alta entre dos fechas (inclusive).
	 * 
	 * @param inicio Fecha de inicio (inclusive).
	 * @param fin    Fecha de fin (inclusive).
	 * @return Lista de proveedores dados de alta en ese rango de fechas.
	 */
	List<Proveedor> buscarPorFechaAlta(LocalDate inicio, LocalDate fin);

}
