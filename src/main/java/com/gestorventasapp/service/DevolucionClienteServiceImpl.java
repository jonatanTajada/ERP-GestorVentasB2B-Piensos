package com.gestorventasapp.service;

import com.gestorventasapp.dao.DevolucionClienteDAO;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.DevolucionCliente;
import com.gestorventasapp.model.Empleado;
import com.gestorventasapp.model.Venta;
import com.gestorventasapp.exceptions.ServiceException;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementación de la lógica de negocio y validaciones para la entidad
 * DevolucionCliente. Aplica bajas lógicas, validaciones y filtros avanzados.
 */
public class DevolucionClienteServiceImpl implements DevolucionClienteService {

	private final DevolucionClienteDAO devolucionClienteDAO;

	/**
	 * Constructor con inyección de dependencia.
	 *
	 * @param devolucionClienteDAO DAO de devoluciones de cliente.
	 */
	public DevolucionClienteServiceImpl(DevolucionClienteDAO devolucionClienteDAO) {
		this.devolucionClienteDAO = devolucionClienteDAO;
	}

	@Override
	public void crearDevolucion(DevolucionCliente devolucion) {
		validarDevolucion(devolucion, true);
		devolucion.setEstado(Estado.activo);
		devolucionClienteDAO.save(devolucion);
	}

	@Override
	public void modificarDevolucion(DevolucionCliente devolucion) {
		if (devolucion == null || devolucion.getIdDevolucionCliente() == null) {
			throw new ServiceException("La devolución y su ID no pueden ser nulos.");
		}
		DevolucionCliente existente = devolucionClienteDAO.findById(devolucion.getIdDevolucionCliente());
		if (existente == null) {
			throw new ServiceException("No existe la devolución a modificar.");
		}
		validarDevolucion(devolucion, false);
		devolucionClienteDAO.update(devolucion);
	}

	@Override
	public void darBajaLogicaDevolucion(int idDevolucion) {
		DevolucionCliente devolucion = devolucionClienteDAO.findById(idDevolucion);
		if (devolucion == null) {
			throw new ServiceException("No existe la devolución a dar de baja.");
		}
		if (devolucion.getEstado() == Estado.inactivo) {
			throw new ServiceException("La devolución ya está inactiva.");
		}
		devolucion.setEstado(Estado.inactivo);
		devolucionClienteDAO.update(devolucion);
	}

	@Override
	public DevolucionCliente buscarPorId(int idDevolucion) {
		DevolucionCliente devolucion = devolucionClienteDAO.findById(idDevolucion);
		if (devolucion == null) {
			throw new ServiceException("No existe una devolución con ese ID.");
		}
		return devolucion;
	}

	@Override
	public List<DevolucionCliente> listarTodas() {
		return devolucionClienteDAO.findAll();
	}

	@Override
	public List<DevolucionCliente> listarActivas() {
		return devolucionClienteDAO.findAllActivas();
	}

	@Override
	public List<DevolucionCliente> listarInactivas() {
		return devolucionClienteDAO.findAllInactivas();
	}

	@Override
	public List<DevolucionCliente> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ServiceException("El estado no puede ser nulo.");
		return devolucionClienteDAO.findByEstado(estado);
	}

	@Override
	public List<DevolucionCliente> buscarPorCliente(int idCliente) {
		if (idCliente <= 0)
			throw new ServiceException("El ID de cliente no es válido.");
		return devolucionClienteDAO.findByCliente(idCliente);
	}

	@Override
	public List<DevolucionCliente> buscarPorVenta(int idVenta) {
		if (idVenta <= 0)
			throw new ServiceException("El ID de venta no es válido.");
		return devolucionClienteDAO.findByVenta(idVenta);
	}

	@Override
	public List<DevolucionCliente> buscarPorEmpleado(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ServiceException("El ID de empleado no es válido.");
		return devolucionClienteDAO.findByEmpleado(idEmpleado);
	}

	@Override
	public List<DevolucionCliente> buscarPorFecha(LocalDate fecha) {
		if (fecha == null)
			throw new ServiceException("La fecha no puede ser nula.");
		return devolucionClienteDAO.findByFecha(fecha);
	}

	@Override
	public List<DevolucionCliente> buscarPorFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio == null || fechaFin == null)
			throw new ServiceException("Las fechas no pueden ser nulas.");
		if (fechaFin.isBefore(fechaInicio))
			throw new ServiceException("La fecha fin no puede ser anterior a la fecha inicio.");
		return devolucionClienteDAO.findByFechaRango(fechaInicio, fechaFin);
	}

	@Override
	public List<DevolucionCliente> buscarPorMotivo(String motivo) {
		if (motivo == null || motivo.trim().isEmpty())
			throw new ServiceException("El motivo no puede estar vacío.");
		return devolucionClienteDAO.findByMotivo(motivo.trim());
	}

	@Override
	public List<DevolucionCliente> buscarPorTotalEntre(double min, double max) {
		if (min < 0 || max < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max < min)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return devolucionClienteDAO.findByTotalBetween(min, max);
	}

	/**
	 * Valida el objeto DevolucionCliente según reglas de negocio y estructura de
	 * BBDD.
	 *
	 * @param devolucion Objeto a validar.
	 * @param esNuevo    True si es alta.
	 * @throws ServiceException si algún dato no cumple las restricciones.
	 */
	private void validarDevolucion(DevolucionCliente devolucion, boolean esNuevo) {
		if (devolucion == null)
			throw new ServiceException("La devolución no puede ser nula.");

		Venta venta = devolucion.getVenta();
		if (venta == null || venta.getIdVenta() == null)
			throw new ServiceException("La venta asociada es obligatoria.");

		Empleado empleado = devolucion.getEmpleado();
		if (empleado == null || empleado.getIdEmpleado() == null)
			throw new ServiceException("El empleado es obligatorio.");

		if (devolucion.getMotivo() != null && devolucion.getMotivo().length() > 255)
			throw new ServiceException("El motivo no puede tener más de 255 caracteres.");

		if (devolucion.getEstado() == null)
			throw new ServiceException("El estado es obligatorio.");
	}
}
