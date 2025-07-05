package com.gestorventasapp.dao;

import com.gestorventasapp.model.Empleado;
import com.gestorventasapp.enums.Estado;

import java.time.LocalDate;
import java.util.List;

public interface EmpleadoDAO {

	void save(Empleado empleado); // Alta de nuevo empleado

	void update(Empleado empleado); // Modificación de empleado existente

	void delete(int idEmpleado); // Baja lógica (estado -> inactivo)

	Empleado findById(int idEmpleado); // Buscar empleado por ID

	Empleado findByDni(String dni); // Buscar empleado por DNI

	Empleado findByEmail(String email); // Buscar empleado por email

	List<Empleado> findAll(); // Listar todos los empleados

	List<Empleado> findAllActivos(); // Listar solo empleados activos

	List<Empleado> findAllInactivos(); // Listar solo empleados inactivos

	List<Empleado> findByEstado(Estado estado); // Listar empleados por estado

	List<Empleado> findByNombre(String nombre); // Buscar empleados por nombre

	List<Empleado> findByApellido(String apellido); // Buscar empleados por apellido1 o apellido2

	List<Empleado> findByLocalidad(String localidad); // Buscar empleados por localidad

	List<Empleado> findByFechaAlta(LocalDate fechaInicio, LocalDate fechaFin); // Buscar empleados
																				// dados de alta
																				// entre fechas

	boolean existsDni(String dni); // Validar existencia de DNI

	boolean existsEmail(String email); // Validar existencia de email

	List<Empleado> findByTelefono(String telefono); // Buscar empleados por teléfono
}
