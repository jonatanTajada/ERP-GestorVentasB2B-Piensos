package com.gestorventasapp.dao;

import java.math.BigDecimal;
import java.util.List;

import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.Iva;

public interface IvaDAO {

	void save(Iva iva); // Alta de nuevo IVA

	void update(Iva iva); // Modificación de IVA existente

	void delete(int idIva); // Baja lógica (estado -> inactivo)

	Iva findById(int idIva); // Buscar por ID

	Iva findByDescripcion(String descripcion); // Buscar por descripción exacta

	Iva findByPorcentaje(BigDecimal porcentaje); // Buscar por porcentaje exacto

	List<Iva> findAll(); // Listar todos los IVA (activos e inactivos)

	List<Iva> findAllActivos(); // Listar solo los IVA activos

	List<Iva> findAllInactivos(); // Listar solo los IVA inactivos

	List<Iva> findByEstado(Estado estado); // Listar IVA por estado

	List<Iva> findByDescripcionLike(String descripcion); // Buscar IVAs por descripción similar (útil para búsqueda en
															// interfaz)

	List<Iva> findByPorcentajeRango(BigDecimal min, BigDecimal max); // Buscar IVAs por rango

	boolean existsDescripcion(String descripcion); // Validar si existe esa descripción

	boolean existsPorcentaje(BigDecimal porcentaje); // Validar si existe ese porcentaje

}
