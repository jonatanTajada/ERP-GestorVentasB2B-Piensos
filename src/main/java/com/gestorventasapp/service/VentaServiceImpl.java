package com.gestorventasapp.service;

import com.gestorventasapp.dao.VentaDAO;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.Cliente;
import com.gestorventasapp.model.DetalleVenta;
import com.gestorventasapp.model.Empleado;
import com.gestorventasapp.model.Venta;
import com.gestorventasapp.exceptions.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementación de la lógica de negocio y validaciones para la entidad Venta.
 * Aplica bajas lógicas, validaciones y filtros avanzados.
 */
public class VentaServiceImpl implements VentaService {

	private final VentaDAO ventaDAO;

	/**
	 * Constructor con inyección de dependencia.
	 *
	 * @param ventaDAO DAO de ventas.
	 */
	public VentaServiceImpl(VentaDAO ventaDAO) {
		this.ventaDAO = ventaDAO;
	}

	@Override
	public void crearVenta(Venta venta) {
		validarVenta(venta, true);
		venta.setEstado(Estado.activo);
		ventaDAO.save(venta);
	}

	@Override
	public void modificarVenta(Venta venta) {
		if (venta == null || venta.getIdVenta() == null) {
			throw new ServiceException("La venta y su ID no pueden ser nulos.");
		}
		Venta existente = ventaDAO.findById(venta.getIdVenta());
		if (existente == null) {
			throw new ServiceException("No existe la venta a modificar.");
		}
		validarVenta(venta, false);
		ventaDAO.update(venta);
	}

	@Override
	public void darBajaLogicaVenta(int idVenta) {
		Venta venta = ventaDAO.findById(idVenta);
		if (venta == null) {
			throw new ServiceException("No existe la venta a dar de baja.");
		}
		if (venta.getEstado() == Estado.inactivo) {
			throw new ServiceException("La venta ya está inactiva.");
		}
		venta.setEstado(Estado.inactivo);
		ventaDAO.update(venta);
	}

	@Override
	public Venta buscarPorId(int idVenta) {
		Venta venta = ventaDAO.findById(idVenta);
		if (venta == null) {
			throw new ServiceException("No existe una venta con ese ID.");
		}
		return venta;
	}

	@Override
	public List<Venta> listarTodas() {
		return ventaDAO.findAll();
	}

	@Override
	public List<Venta> listarActivas() {
		return ventaDAO.findAllActivas();
	}

	@Override
	public List<Venta> listarInactivas() {
		return ventaDAO.findAllInactivas();
	}

	@Override
	public List<Venta> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ServiceException("El estado no puede ser nulo.");
		return ventaDAO.findByEstado(estado);
	}

	@Override
	public List<Venta> buscarPorCliente(int idCliente) {
		if (idCliente <= 0)
			throw new ServiceException("El ID de cliente no es válido.");
		return ventaDAO.findByCliente(idCliente);
	}

	@Override
	public List<Venta> buscarPorEmpleado(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ServiceException("El ID de empleado no es válido.");
		return ventaDAO.findByEmpleado(idEmpleado);
	}

	@Override
	public List<Venta> buscarPorFecha(LocalDate fecha) {
		if (fecha == null)
			throw new ServiceException("La fecha no puede ser nula.");
		return ventaDAO.findByFecha(fecha);
	}

	@Override
	public List<Venta> buscarPorFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio == null || fechaFin == null)
			throw new ServiceException("Las fechas no pueden ser nulas.");
		if (fechaFin.isBefore(fechaInicio))
			throw new ServiceException("La fecha fin no puede ser anterior a la fecha inicio.");
		return ventaDAO.findByFechaRango(fechaInicio, fechaFin);
	}

	@Override
	public List<Venta> buscarPorTotalSinIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los importes no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return ventaDAO.findByTotalSinIvaBetween(min.doubleValue(), max.doubleValue());
	}

	@Override
	public List<Venta> buscarPorTotalConIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los importes no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return ventaDAO.findByTotalConIvaBetween(min.doubleValue(), max.doubleValue());
	}

	/**
	 * Valida el objeto Venta según las reglas de negocio y la estructura de la
	 * BBDD.
	 *
	 * @param venta   Objeto a validar.
	 * @param esNuevo True si es alta.
	 * @throws ServiceException Si algún dato no cumple las restricciones.
	 */
	private void validarVenta(Venta venta, boolean esNuevo) {
		if (venta == null)
			throw new ServiceException("La venta no puede ser nula.");
		// Cliente
		Cliente cliente = venta.getCliente();
		if (cliente == null || cliente.getIdCliente() == null)
			throw new ServiceException("El cliente es obligatorio.");
		// Empleado
		Empleado empleado = venta.getEmpleado();
		if (empleado == null || empleado.getIdEmpleado() == null)
			throw new ServiceException("El empleado es obligatorio.");
		// Totales (pueden ser nulos en el alta, pero si existen deben ser >= 0)
		if (venta.getTotalSinIva() != null && venta.getTotalSinIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El total sin IVA no puede ser negativo.");
		if (venta.getTotalConIva() != null && venta.getTotalConIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El total con IVA no puede ser negativo.");
		// Estado
		if (venta.getEstado() == null)
			throw new ServiceException("El estado es obligatorio.");
	}

	@Override
	public void crearVentaConDetalles(Venta venta, List<DetalleVenta> detallesVenta) {
		if (venta == null || detallesVenta == null || detallesVenta.isEmpty())
			throw new ServiceException("Venta y detalles requeridos.");

		try {
			ventaDAO.saveWithDetails(venta, detallesVenta);
		} catch (Exception e) {
			throw new ServiceException("Error al guardar venta con detalles.", e);
		}
	}

}
