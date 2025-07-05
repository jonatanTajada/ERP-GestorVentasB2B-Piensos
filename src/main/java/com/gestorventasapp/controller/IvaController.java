package com.gestorventasapp.controller;

import com.gestorventasapp.model.Iva;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.service.IvaService;
import com.gestorventasapp.exceptions.ControllerException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador para la gestión de IVAs. Orquesta la interacción entre la vista y
 * la capa de servicios, validando la entrada y gestionando los resultados y
 * errores.
 */
public class IvaController {

	private final IvaService ivaService;

	/**
	 * Constructor con inyección de dependencias.
	 * 
	 * @param ivaService Servicio de IVA a utilizar.
	 */
	public IvaController(IvaService ivaService) {
		this.ivaService = ivaService;
	}

	/**
	 * Crea un nuevo registro de IVA tras validar los datos mínimos.
	 * 
	 * @param iva Objeto IVA a crear.
	 */
	public void crearIva(Iva iva) {
		if (iva == null)
			throw new ControllerException("El objeto IVA no puede ser nulo.");
		if (iva.getDescripcion() == null || iva.getDescripcion().trim().isEmpty())
			throw new ControllerException("La descripción del IVA es obligatoria.");
		if (iva.getPorcentaje() == null)
			throw new ControllerException("El porcentaje de IVA es obligatorio.");

		try {
			ivaService.crearIva(iva);
		} catch (Exception e) {
			throw new ControllerException("Error al crear el IVA: " + e.getMessage(), e);
		}
	}

	/**
	 * Modifica un registro de IVA existente.
	 * 
	 * @param iva Objeto IVA con los datos actualizados.
	 */
	public void modificarIva(Iva iva) {
		if (iva == null || iva.getIdIva() == null)
			throw new ControllerException("El IVA a modificar debe tener un ID válido.");
		if (iva.getDescripcion() == null || iva.getDescripcion().trim().isEmpty())
			throw new ControllerException("La descripción del IVA es obligatoria.");
		if (iva.getPorcentaje() == null)
			throw new ControllerException("El porcentaje de IVA es obligatorio.");

		try {
			ivaService.modificarIva(iva);
		} catch (Exception e) {
			throw new ControllerException("Error al modificar el IVA: " + e.getMessage(), e);
		}
	}

	/**
	 * Realiza la baja lógica (estado -> inactivo) de un IVA.
	 * 
	 * @param idIva ID del IVA a dar de baja.
	 */
	public void darBajaLogicaIva(int idIva) {
		if (idIva <= 0)
			throw new ControllerException("El ID de IVA debe ser válido.");
		try {
			ivaService.darBajaLogicaIva(idIva);
		} catch (Exception e) {
			throw new ControllerException("Error al dar de baja el IVA: " + e.getMessage(), e);
		}
	}

	/**
	 * Busca un IVA por su ID.
	 * 
	 * @param idIva ID a buscar.
	 * @return Objeto IVA encontrado, o null si no existe.
	 */
	public Iva buscarPorId(int idIva) {
		if (idIva <= 0)
			throw new ControllerException("El ID de IVA debe ser válido.");
		try {
			return ivaService.buscarPorId(idIva);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar el IVA: " + e.getMessage(), e);
		}
	}

	/**
	 * Lista todos los IVAs (activos e inactivos).
	 * 
	 * @return Lista de IVAs.
	 */
	public List<Iva> listarTodos() {
		try {
			return ivaService.listarTodos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los IVAs: " + e.getMessage(), e);
		}
	}

	/**
	 * Lista solo los IVAs activos.
	 * 
	 * @return Lista de IVAs activos.
	 */
	public List<Iva> listarActivos() {
		try {
			return ivaService.listarActivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los IVAs activos: " + e.getMessage(), e);
		}
	}

	/**
	 * Lista solo los IVAs inactivos.
	 * 
	 * @return Lista de IVAs inactivos.
	 */
	public List<Iva> listarInactivos() {
		try {
			return ivaService.listarInactivos();
		} catch (Exception e) {
			throw new ControllerException("Error al listar los IVAs inactivos: " + e.getMessage(), e);
		}
	}

	/**
	 * Lista los IVAs según el estado indicado.
	 * 
	 * @param estado Estado a filtrar.
	 * @return Lista de IVAs según estado.
	 */
	public List<Iva> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ControllerException("El estado no puede ser nulo.");
		try {
			return ivaService.listarPorEstado(estado);
		} catch (Exception e) {
			throw new ControllerException("Error al listar los IVAs por estado: " + e.getMessage(), e);
		}
	}

	/**
	 * Busca un IVA por descripción exacta.
	 * 
	 * @param descripcion Descripción a buscar.
	 * @return IVA encontrado, o null si no existe.
	 */
	public Iva buscarPorDescripcion(String descripcion) {
		if (descripcion == null || descripcion.trim().isEmpty())
			throw new ControllerException("La descripción no puede estar vacía.");
		try {
			return ivaService.buscarPorDescripcion(descripcion);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar IVA por descripción: " + e.getMessage(), e);
		}
	}

	/**
	 * Busca un IVA por porcentaje exacto.
	 * 
	 * @param porcentaje Porcentaje a buscar.
	 * @return IVA encontrado, o null si no existe.
	 */
	public Iva buscarPorPorcentaje(BigDecimal porcentaje) {
		try {
			return ivaService.buscarPorPorcentaje(porcentaje);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar IVA por porcentaje: " + e.getMessage(), e);
		}
	}

	/**
	 * Busca IVAs por descripción parcial (búsqueda tipo like).
	 * 
	 * @param descripcion Descripción parcial a buscar.
	 * @return Lista de IVAs coincidentes.
	 */
	public List<Iva> buscarPorDescripcionLike(String descripcion) {
		if (descripcion == null || descripcion.trim().isEmpty())
			throw new ControllerException("La descripción no puede estar vacía.");
		try {
			return ivaService.buscarPorDescripcionLike(descripcion);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar IVAs por descripción parcial: " + e.getMessage(), e);
		}
	}

	/**
	 * Busca IVAs por rango de porcentaje.
	 * 
	 * @param min Valor mínimo.
	 * @param max Valor máximo.
	 * @return Lista de IVAs en el rango indicado.
	 */
	public List<Iva> buscarPorPorcentajeRango(BigDecimal min, BigDecimal max) {
		if (min == null || max == null || min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0
				|| min.compareTo(max) > 0) {

			throw new ControllerException("El rango de porcentaje no es válido.");
		}

		try {
			return ivaService.buscarPorPorcentajeRango(min, max);
		} catch (Exception e) {
			throw new ControllerException("Error al buscar IVAs por rango: " + e.getMessage(), e);
		}
	}

	/**
	 * Verifica si existe un IVA con la descripción indicada.
	 * 
	 * @param descripcion Descripción a verificar.
	 * @return true si existe, false si no.
	 */
	public boolean existeDescripcion(String descripcion) {
		if (descripcion == null || descripcion.trim().isEmpty())
			throw new ControllerException("La descripción no puede estar vacía.");
		try {
			return ivaService.existeDescripcion(descripcion);
		} catch (Exception e) {
			throw new ControllerException("Error al comprobar existencia de descripción: " + e.getMessage(), e);
		}
	}

	/**
	 * Verifica si existe un IVA con el porcentaje indicado.
	 * 
	 * @param porcentaje Porcentaje a verificar.
	 * @return true si existe, false si no.
	 */
	public boolean existePorcentaje(BigDecimal porcentaje) {
		try {
			return ivaService.existePorcentaje(porcentaje);
		} catch (Exception e) {
			throw new ControllerException("Error al comprobar existencia de porcentaje: " + e.getMessage(), e);
		}
	}
}
