package com.gestorventasapp.service;

import com.gestorventasapp.model.Cliente;
import com.gestorventasapp.enums.Estado;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de clientes. Aplica toda la lógica de negocio y
 * validaciones sobre la entidad Cliente.
 */
public interface ClienteService {

	/**
	 * Da de alta un nuevo cliente tras validar todos los datos y reglas de negocio.
	 * 
	 * @param cliente Cliente a crear.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos o ya
	 *                                                         existe.
	 */
	void crearCliente(Cliente cliente);

	/**
	 * Modifica un cliente existente tras validar todos los datos.
	 * 
	 * @param cliente Cliente con los datos modificados.
	 * @throws com.gestorventasapp.exceptions.ServiceException si los datos son
	 *                                                         inválidos.
	 */
	void modificarCliente(Cliente cliente);

	/**
	 * Realiza la baja lógica (estado->inactivo) de un cliente.
	 * 
	 * @param idCliente Identificador del cliente.
	 */
	void darBajaLogicaCliente(int idCliente);

	/**
	 * Busca un cliente por su ID.
	 * 
	 * @param idCliente Identificador.
	 * @return Cliente encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe.
	 */
	Cliente buscarPorId(int idCliente);

	/**
	 * Busca un cliente por su CIF/NIF.
	 * 
	 * @param cifNif CIF o NIF.
	 * @return Cliente encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe o
	 *                                                         formato inválido.
	 */
	Cliente buscarPorCifNif(String cifNif);

	/**
	 * Busca un cliente por su email.
	 * 
	 * @param email Email del cliente.
	 * @return Cliente encontrado.
	 * @throws com.gestorventasapp.exceptions.ServiceException si no existe o
	 *                                                         formato inválido.
	 */
	Cliente buscarPorEmail(String email);

	/**
	 * Lista todos los clientes (activos e inactivos).
	 * 
	 * @return Lista completa de clientes.
	 */
	List<Cliente> listarTodos();

	/**
	 * Lista solo los clientes activos.
	 * 
	 * @return Lista de clientes activos.
	 */
	List<Cliente> listarActivos();

	/**
	 * Lista solo los clientes inactivos.
	 * 
	 * @return Lista de clientes inactivos.
	 */
	List<Cliente> listarInactivos();

	/**
	 * Lista los clientes por estado.
	 * 
	 * @param estado Estado a filtrar.
	 * @return Lista de clientes filtrados por estado.
	 */
	List<Cliente> listarPorEstado(Estado estado);

	/**
	 * Busca clientes por razón social (búsqueda parcial).
	 * 
	 * @param razonSocial Razón social o parte.
	 * @return Lista de clientes que coinciden.
	 */
	List<Cliente> buscarPorRazonSocial(String razonSocial);

	/**
	 * Busca clientes por forma jurídica.
	 * 
	 * @param formaJuridica Forma jurídica.
	 * @return Lista de clientes que coinciden.
	 */
	List<Cliente> buscarPorFormaJuridica(String formaJuridica);

	/**
	 * Busca clientes por localidad.
	 * 
	 * @param localidad Localidad.
	 * @return Lista de clientes que coinciden.
	 */
	List<Cliente> buscarPorLocalidad(String localidad);

	/**
	 * Busca clientes dados de alta entre dos fechas.
	 * 
	 * @param inicio Fecha inicio.
	 * @param fin    Fecha fin.
	 * @return Lista de clientes.
	 */
	List<Cliente> buscarPorFechaAlta(LocalDate inicio, LocalDate fin);

	/**
	 * Comprueba si existe un cliente con ese CIF/NIF.
	 * 
	 * @param cifNif CIF o NIF.
	 * @return true si existe.
	 */
	boolean existeCifNif(String cifNif);

	/**
	 * Comprueba si existe un cliente con ese email.
	 * 
	 * @param email Email.
	 * @return true si existe.
	 */
	boolean existeEmail(String email);

	/**
	 * Busca clientes por teléfono.
	 * 
	 * @param telefono Teléfono.
	 * @return Lista de clientes.
	 */
	List<Cliente> buscarPorTelefono(String telefono);
}
