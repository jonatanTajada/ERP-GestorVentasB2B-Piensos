package com.gestorventasapp.service;

import com.gestorventasapp.model.Iva;
import com.gestorventasapp.enums.Estado;

import java.math.BigDecimal;
import java.util.List;

public interface IvaService {

	void crearIva(Iva iva); // Alta con validaciones

	void modificarIva(Iva iva); // Modificación de IVA existente

	void darBajaLogicaIva(int idIva); // Baja lógica (estado -> inactivo)

	Iva buscarPorId(int idIva); // Buscar por ID

	List<Iva> listarTodos(); // Listar todos (activos e inactivos)

	List<Iva> listarActivos(); // Listar solo activos

	List<Iva> listarInactivos(); // Listar solo inactivos

	List<Iva> listarPorEstado(Estado estado); // Listar por estado

	Iva buscarPorDescripcion(String descripcion); // Buscar por descripción exacta

	Iva buscarPorPorcentaje(BigDecimal porcentaje); // Buscar por porcentaje exacto

	List<Iva> buscarPorDescripcionLike(String descripcion); // Buscar por descripción parcial

	List<Iva> buscarPorPorcentajeRango(BigDecimal min, BigDecimal max); // Buscar por rango de porcentaje

	boolean existeDescripcion(String descripcion); // Validación

	boolean existePorcentaje(BigDecimal porcentaje); // Validación
}
