package com.gestorventasapp.service;

import com.gestorventasapp.dao.DevolucionProveedorDAO;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.DevolucionProveedor;
import com.gestorventasapp.model.Compra;
import com.gestorventasapp.model.Empleado;
import com.gestorventasapp.exceptions.ServiceException;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementación de la lógica de negocio y validaciones para la entidad
 * DevolucionProveedor. Aplica bajas lógicas, validaciones y filtros avanzados.
 */
public class DevolucionProveedorServiceImpl implements DevolucionProveedorService {

	private final DevolucionProveedorDAO devolucionProveedorDAO;

	/**
	 * Constructor con inyección de dependencia.
	 *
	 * @param devolucionProveedorDAO DAO de devoluciones a proveedor.
	 */
	public DevolucionProveedorServiceImpl(DevolucionProveedorDAO devolucionProveedorDAO) {
		this.devolucionProveedorDAO = devolucionProveedorDAO;
	}

	@Override
	public void crearDevolucion(DevolucionProveedor devolucion) {
		validarDevolucion(devolucion, true);
		devolucion.setEstado(Estado.activo);
		devolucionProveedorDAO.save(devolucion);
	}

	@Override
	public void modificarDevolucion(DevolucionProveedor devolucion) {
		if (devolucion == null || devolucion.getIdDevolucionProveedor() == null) {
			throw new ServiceException("La devolución y su ID no pueden ser nulos.");
		}
		DevolucionProveedor existente = devolucionProveedorDAO.findById(devolucion.getIdDevolucionProveedor());
		if (existente == null) {
			throw new ServiceException("No existe la devolución a modificar.");
		}
		validarDevolucion(devolucion, false);
		devolucionProveedorDAO.update(devolucion);
	}

	@Override
	public void darBajaLogicaDevolucion(int idDevolucion) {
		DevolucionProveedor devolucion = devolucionProveedorDAO.findById(idDevolucion);
		if (devolucion == null) {
			throw new ServiceException("No existe la devolución a dar de baja.");
		}
		if (devolucion.getEstado() == Estado.inactivo) {
			throw new ServiceException("La devolución ya está inactiva.");
		}
		devolucion.setEstado(Estado.inactivo);
		devolucionProveedorDAO.update(devolucion);
	}

	@Override
	public DevolucionProveedor buscarPorId(int idDevolucion) {
		DevolucionProveedor devolucion = devolucionProveedorDAO.findById(idDevolucion);
		if (devolucion == null) {
			throw new ServiceException("No existe una devolución con ese ID.");
		}
		return devolucion;
	}

	@Override
	public List<DevolucionProveedor> listarTodas() {
		return devolucionProveedorDAO.findAll();
	}

	@Override
	public List<DevolucionProveedor> listarActivas() {
		return devolucionProveedorDAO.findAllActivas();
	}

	@Override
	public List<DevolucionProveedor> listarInactivas() {
		return devolucionProveedorDAO.findAllInactivas();
	}

	@Override
	public List<DevolucionProveedor> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ServiceException("El estado no puede ser nulo.");
		return devolucionProveedorDAO.findByEstado(estado);
	}

	@Override
	public List<DevolucionProveedor> buscarPorProveedor(int idProveedor) {
		if (idProveedor <= 0)
			throw new ServiceException("El ID de proveedor no es válido.");
		return devolucionProveedorDAO.findByProveedor(idProveedor);
	}

	@Override
	public List<DevolucionProveedor> buscarPorCompra(int idCompra) {
		if (idCompra <= 0)
			throw new ServiceException("El ID de compra no es válido.");
		return devolucionProveedorDAO.findByCompra(idCompra);
	}

	@Override
	public List<DevolucionProveedor> buscarPorEmpleado(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ServiceException("El ID de empleado no es válido.");
		return devolucionProveedorDAO.findByEmpleado(idEmpleado);
	}

	@Override
	public List<DevolucionProveedor> buscarPorFecha(LocalDate fecha) {
		if (fecha == null)
			throw new ServiceException("La fecha no puede ser nula.");
		return devolucionProveedorDAO.findByFecha(fecha);
	}

	@Override
	public List<DevolucionProveedor> buscarPorFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio == null || fechaFin == null)
			throw new ServiceException("Las fechas no pueden ser nulas.");
		if (fechaFin.isBefore(fechaInicio))
			throw new ServiceException("La fecha fin no puede ser anterior a la fecha inicio.");
		return devolucionProveedorDAO.findByFechaRango(fechaInicio, fechaFin);
	}

	@Override
	public List<DevolucionProveedor> buscarPorMotivo(String motivo) {
		if (motivo == null || motivo.trim().isEmpty())
			throw new ServiceException("El motivo no puede estar vacío.");
		return devolucionProveedorDAO.findByMotivo(motivo.trim());
	}

	@Override
	public List<DevolucionProveedor> buscarPorTotalEntre(double min, double max) {
		if (min < 0 || max < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max < min)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return devolucionProveedorDAO.findByTotalBetween(min, max);
	}

	/**
	 * Valida el objeto DevolucionProveedor según reglas de negocio y estructura de
	 * BBDD.
	 *
	 * @param devolucion Objeto a validar.
	 * @param esNuevo    True si es alta.
	 * @throws ServiceException si algún dato no cumple las restricciones.
	 */
	private void validarDevolucion(DevolucionProveedor devolucion, boolean esNuevo) {
		if (devolucion == null)
			throw new ServiceException("La devolución no puede ser nula.");

		Compra compra = devolucion.getCompra();
		if (compra == null || compra.getIdCompra() == null)
			throw new ServiceException("La compra asociada es obligatoria.");

		Empleado empleado = devolucion.getEmpleado();
		if (empleado == null || empleado.getIdEmpleado() == null)
			throw new ServiceException("El empleado es obligatorio.");

		if (devolucion.getMotivo() != null && devolucion.getMotivo().length() > 255)
			throw new ServiceException("El motivo no puede tener más de 255 caracteres.");

		if (devolucion.getEstado() == null)
			throw new ServiceException("El estado es obligatorio.");
	}
}
