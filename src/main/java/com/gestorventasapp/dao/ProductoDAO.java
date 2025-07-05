package com.gestorventasapp.dao;

import com.gestorventasapp.model.Producto;
import com.gestorventasapp.enums.Estado;
import java.util.List;

public interface ProductoDAO {

	void save(Producto producto); // Alta de nuevo producto

	void update(Producto producto); // Modificación de producto existente

	void delete(int idProducto); // Baja lógica (estado -> inactivo)

	Producto findById(int idProducto); // Buscar producto por ID

	List<Producto> findAll(); // Listar todos los productos (activos e inactivos)

	List<Producto> findAllActivos(); // Listar solo productos activos

	List<Producto> findAllInactivos(); // Listar solo productos inactivos

	List<Producto> findByEstado(Estado estado); // Listar productos por estado

	List<Producto> findByNombre(String nombre); // Buscar productos por nombre (puede devolver varios)

	List<Producto> findByTipoAnimal(String tipoAnimal); // Buscar productos por tipo de animal

	List<Producto> findByMarca(String marca); // Buscar productos por marca

	List<Producto> findByProveedor(int idProveedor); // Buscar productos por proveedor

	List<Producto> findByFormato(String formato); // Buscar productos por formato

	List<Producto> findByStockMinimo(); // Buscar productos con stock igual o menor al mínimo

	List<Producto> findByStockMenorQue(int cantidad); // Buscar productos con stock < cantidad

	List<Producto> findByStockMayorQue(int cantidad); // Buscar productos con stock > cantidad

	boolean existsNombre(String nombre); // Validar existencia por nombre

	boolean existsByMarcaAndFormatoAndProveedor(String marca, String formato, int idProveedor); // Validar combinación
																								// única

	List<Producto> findByPrecioVentaBetween(double min, double max); // Buscar productos por rango de precio de venta

	List<Producto> findByPrecioCompraBetween(double min, double max); // Buscar productos por rango de precio de compra

}
