package com.gestorventasapp.service;

import java.math.BigDecimal;
import java.util.List;

import com.gestorventasapp.dao.IvaDAO;
import com.gestorventasapp.dao.IvaDAOImpl;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.exceptions.ServiceException;
import com.gestorventasapp.model.Iva;

public class IvaServiceImpl implements IvaService {

	private final IvaDAO ivaDAO;

	// Constructor para inyección de dependencia
	public IvaServiceImpl(IvaDAO ivaDAO) {
		this.ivaDAO = ivaDAO;
	}

	@Override
	public void crearIva(Iva iva) {
		validarIva(iva, true);
		if (ivaDAO.findByDescripcion(iva.getDescripcion()) != null)
			throw new ServiceException("Ya existe un IVA con esa descripción.");
		if (ivaDAO.findByPorcentaje(iva.getPorcentaje()) != null)
			throw new ServiceException("Ya existe un IVA con ese porcentaje.");
		ivaDAO.save(iva);
	}

	@Override
	public void modificarIva(Iva iva) {
		validarIva(iva, false);
		ivaDAO.update(iva);
	}

	@Override
	public void darBajaLogicaIva(int idIva) {
		ivaDAO.delete(idIva);
	}

	@Override
	public Iva buscarPorId(int idIva) {
		Iva iva = ivaDAO.findById(idIva);
		if (iva == null)
			throw new ServiceException("No existe IVA con ese ID.");
		return iva;
	}

	@Override
	public List<Iva> listarTodos() {
		return ivaDAO.findAll();
	}

	@Override
	public List<Iva> listarActivos() {
		return ivaDAO.findAllActivos();
	}

	@Override
	public List<Iva> listarInactivos() {
		return ivaDAO.findAllInactivos();
	}

	@Override
	public List<Iva> listarPorEstado(Estado estado) {
		return ivaDAO.findByEstado(estado);
	}

	@Override
	public Iva buscarPorDescripcion(String descripcion) {
		if (descripcion == null || descripcion.trim().isEmpty())
			throw new ServiceException("La descripción no puede estar vacía.");
		Iva iva = ivaDAO.findByDescripcion(descripcion);
		if (iva == null)
			throw new ServiceException("No existe IVA con esa descripción.");
		return iva;
	}

	@Override
	public Iva buscarPorPorcentaje(BigDecimal porcentaje) {
		if (porcentaje == null || porcentaje.compareTo(new BigDecimal("0.01")) < 0
				|| porcentaje.compareTo(new BigDecimal("100")) > 0) {
			throw new ServiceException("El porcentaje debe estar entre 0.01 y 100.");
		}
		Iva iva = ivaDAO.findByPorcentaje(porcentaje);
		if (iva == null)
			throw new ServiceException("No existe IVA con ese porcentaje.");
		return iva;
	}

	@Override
	public List<Iva> buscarPorDescripcionLike(String descripcion) {
		if (descripcion == null || descripcion.trim().isEmpty())
			throw new ServiceException("El texto de búsqueda no puede estar vacío.");
		return ivaDAO.findByDescripcionLike(descripcion);
	}

	@Override
	public List<Iva> buscarPorPorcentajeRango(BigDecimal min, BigDecimal max) {
		if (min == null || max == null || min.compareTo(new BigDecimal("0.01")) < 0
				|| max.compareTo(new BigDecimal("100")) > 0 || min.compareTo(max) > 0) {
			throw new ServiceException("Rango de porcentaje inválido.");
		}
		return ivaDAO.findByPorcentajeRango(min, max);
	}

	@Override
	public boolean existeDescripcion(String descripcion) {
		if (descripcion == null || descripcion.trim().isEmpty())
			throw new ServiceException("La descripción no puede estar vacía.");
		return ivaDAO.findByDescripcion(descripcion) != null;
	}

	@Override
	public boolean existePorcentaje(BigDecimal porcentaje) {
		if (porcentaje == null || porcentaje.compareTo(new BigDecimal("0.01")) < 0
				|| porcentaje.compareTo(new BigDecimal("100")) > 0) {
			throw new ServiceException("El porcentaje debe estar entre 0.01 y 100.");
		}
		return ivaDAO.findByPorcentaje(porcentaje) != null;
	}

	/**
	 * Valida todos los campos del IVA (formato, límites, unicidad, etc.)
	 * 
	 * @param iva     objeto a validar
	 * @param esNuevo true si es alta, false si es modificación
	 */
	private void validarIva(Iva iva, boolean esNuevo) {
		if (iva == null)
			throw new ServiceException("El objeto IVA no puede ser nulo.");
		if (iva.getDescripcion() == null || iva.getDescripcion().trim().isEmpty())
			throw new ServiceException("La descripción es obligatoria.");
		if (!iva.getDescripcion().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 %().,\\-]{2,50}$"))
			throw new ServiceException("La descripción tiene un formato no válido.");
		if (iva.getPorcentaje() == null)
			throw new ServiceException("El porcentaje es obligatorio.");
		if (iva.getPorcentaje().doubleValue() < 0.01 || iva.getPorcentaje().doubleValue() > 100)
			throw new ServiceException("El porcentaje debe estar entre 0.01 y 100.");
		if (iva.getEstado() == null)
			throw new ServiceException("El estado es obligatorio.");
		if (iva.getEstado() != Estado.activo && iva.getEstado() != Estado.inactivo)
			throw new ServiceException("El estado solo puede ser activo o inactivo.");
		// evitar duplicados en alta:
		if (esNuevo) {
			if (ivaDAO.findByDescripcion(iva.getDescripcion()) != null)
				throw new ServiceException("Ya existe un IVA con esa descripción.");
			if (ivaDAO.findByPorcentaje(iva.getPorcentaje()) != null)
				throw new ServiceException("Ya existe un IVA con ese porcentaje.");
		}
	}
}
