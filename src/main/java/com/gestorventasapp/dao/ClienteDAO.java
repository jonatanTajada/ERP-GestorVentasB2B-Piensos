package com.gestorventasapp.dao;

import com.gestorventasapp.model.Cliente;
import com.gestorventasapp.enums.Estado;

import java.time.LocalDate;
import java.util.List;

public interface ClienteDAO {

	void save(Cliente cliente); // Alta de nuevo cliente

	void update(Cliente cliente); // Modificación de cliente existente

	void delete(int idCliente); // Baja lógica (estado -> inactivo)

	Cliente findById(int idCliente); // Buscar cliente por ID

	Cliente findByCifNif(String cifNif); // Buscar cliente por CIF/NIF

	Cliente findByEmail(String email); // Buscar cliente por email

	List<Cliente> findAll(); // Listar todos los clientes

	List<Cliente> findAllActivos(); // Listar solo clientes activos

	List<Cliente> findAllInactivos(); // Listar solo clientes inactivos

	List<Cliente> findByEstado(Estado estado); // Listar clientes por estado

	List<Cliente> findByRazonSocial(String razonSocial); // Buscar clientes por razón social (puede devolver varios)

	List<Cliente> findByFormaJuridica(String formaJuridica); // Filtrar por S.L., S.A., Cooperativa, Autónomo

	List<Cliente> findByLocalidad(String localidad); // Buscar clientes por localidad

	List<Cliente> findByFechaAlta(LocalDate fechaInicio, LocalDate fechaFin); // Filtrar por fechade alta
																									 

	boolean existsCifNif(String cifNif); // Validar existencia de CIF/NIF

	boolean existsEmail(String email); // Validar existencia de email

	List<Cliente> findByTelefono(String telefono); // Buscar por teléfono (por si quieres localizar desde llamada)
}
