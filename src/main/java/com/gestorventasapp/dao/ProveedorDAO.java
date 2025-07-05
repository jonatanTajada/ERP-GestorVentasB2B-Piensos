package com.gestorventasapp.dao;

import com.gestorventasapp.model.Proveedor;
import com.gestorventasapp.enums.Estado;
import java.time.LocalDate;
import java.util.List;

/**
 * DAO para operaciones CRUD y consultas de Proveedor.
 */
public interface ProveedorDAO {

	void save(Proveedor proveedor); // Alta de nuevo proveedor

	void update(Proveedor proveedor); // Modificación de proveedor existente

	void delete(int idProveedor); // Baja lógica (estado -> inactivo)

	Proveedor findById(int idProveedor); // Buscar proveedor por ID

	Proveedor findByCifNif(String cifNif); // Buscar proveedor por CIF/NIF

	Proveedor findByEmail(String email); // Buscar proveedor por email

	List<Proveedor> findAll(); // Listar todos los proveedores

	List<Proveedor> findAllActivos(); // Listar solo proveedores activos

	List<Proveedor> findAllInactivos(); // Listar solo proveedores inactivos

	List<Proveedor> findByEstado(Estado estado); // Listar proveedores por estado

	List<Proveedor> findByRazonSocial(String razonSocial); // Buscar proveedores por razón social

	List<Proveedor> findByFormaJuridica(String formaJuridica); // Filtrar por S.L., S.A., Cooperativa, Autónomo

	List<Proveedor> findByLocalidad(String localidad); // Buscar proveedores por localidad

	List<Proveedor> findByFechaAlta(LocalDate fechaInicio, LocalDate fechaFin); // Filtra proveedores dados de alta entre dos fechas (inclusive).

	boolean existsCifNif(String cifNif); // Validar existencia de CIF/NIF

	boolean existsEmail(String email); // Validar existencia de email

	List<Proveedor> findByTelefono(String telefono); // Buscar por teléfono
}
