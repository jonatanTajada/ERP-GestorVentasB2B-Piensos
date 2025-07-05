package com.gestorventasapp.service;

import com.gestorventasapp.dao.CompraDAO;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.model.Compra;
import com.gestorventasapp.model.DetalleCompra;
import com.gestorventasapp.model.Empleado;
import com.gestorventasapp.model.Proveedor;
import com.gestorventasapp.model.Producto;
import com.gestorventasapp.exceptions.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementación de la lógica de negocio y validaciones para la entidad Compra.
 * Controla la creación, modificación, baja lógica y filtros avanzados de
 * compras.
 */
public class CompraServiceImpl implements CompraService {

	private final CompraDAO compraDAO;
	private final ProductoService productoService;

	/**
	 * Constructor con inyección de dependencia.
	 * 
	 * @param compraDAO DAO de compras.
	 */
	public CompraServiceImpl(CompraDAO compraDAO, ProductoService productoService) {
		this.compraDAO = compraDAO;
		this.productoService = productoService;
	}

	@Override
	public void crearCompra(Compra compra) {
		validarCompra(compra, true);
		compra.setEstado(Estado.activo);
		compraDAO.save(compra);
	}

	@Override
	public void modificarCompra(Compra compra) {
		if (compra == null || compra.getIdCompra() == null) {
			throw new ServiceException("La compra y su ID no pueden ser nulos.");
		}
		Compra existente = compraDAO.findById(compra.getIdCompra());
		if (existente == null) {
			throw new ServiceException("No existe la compra a modificar.");
		}
		validarCompra(compra, false);
		compraDAO.update(compra);
	}

	@Override
	public void darBajaLogicaCompra(int idCompra) {
		Compra compra = compraDAO.findById(idCompra);
		if (compra == null) {
			throw new ServiceException("No existe la compra a dar de baja.");
		}
		if (compra.getEstado() == Estado.inactivo) {
			throw new ServiceException("La compra ya está inactiva.");
		}
		compra.setEstado(Estado.inactivo);
		compraDAO.update(compra);
	}

	@Override
	public Compra buscarPorId(int idCompra) {
		Compra compra = compraDAO.findById(idCompra);
		if (compra == null) {
			throw new ServiceException("No existe una compra con ese ID.");
		}
		return compra;
	}

	@Override
	public List<Compra> listarTodas() {
		return compraDAO.findAll();
	}

	@Override
	public List<Compra> listarActivas() {
		return compraDAO.findAllActivas();
	}

	@Override
	public List<Compra> listarInactivas() {
		return compraDAO.findAllInactivas();
	}

	@Override
	public List<Compra> listarPorEstado(Estado estado) {
		if (estado == null)
			throw new ServiceException("El estado no puede ser nulo.");
		return compraDAO.findByEstado(estado);
	}

	@Override
	public List<Compra> buscarPorProveedor(int idProveedor) {
		if (idProveedor <= 0)
			throw new ServiceException("El ID de proveedor no es válido.");
		return compraDAO.findByProveedor(idProveedor);
	}

	@Override
	public List<Compra> buscarPorEmpleado(int idEmpleado) {
		if (idEmpleado <= 0)
			throw new ServiceException("El ID de empleado no es válido.");
		return compraDAO.findByEmpleado(idEmpleado);
	}

	@Override
	public List<Compra> buscarPorFecha(LocalDate fecha) {
		if (fecha == null)
			throw new ServiceException("La fecha no puede ser nula.");
		return compraDAO.findByFecha(fecha);
	}

	@Override
	public List<Compra> buscarPorFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		if (fechaInicio == null || fechaFin == null)
			throw new ServiceException("Las fechas no pueden ser nulas.");
		if (fechaFin.isBefore(fechaInicio))
			throw new ServiceException("La fecha fin no puede ser anterior a la fecha inicio.");
		return compraDAO.findByFechaRango(fechaInicio, fechaFin);
	}

	@Override
	public List<Compra> buscarPorTotalSinIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los importes no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return compraDAO.findByTotalSinIvaBetween(min.doubleValue(), max.doubleValue());
	}

	@Override
	public List<Compra> buscarPorTotalConIvaEntre(BigDecimal min, BigDecimal max) {
		if (min == null || max == null)
			throw new ServiceException("Los importes no pueden ser nulos.");
		if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("Los importes no pueden ser negativos.");
		if (max.compareTo(min) < 0)
			throw new ServiceException("El importe máximo no puede ser menor que el mínimo.");
		return compraDAO.findByTotalConIvaBetween(min.doubleValue(), max.doubleValue());
	}

	/**
	 * Valida el objeto Compra según las reglas de negocio y la estructura de la
	 * BBDD.
	 * 
	 * @param compra  Objeto a validar.
	 * @param esNuevo True si es alta.
	 * @throws ServiceException Si algún dato no cumple las restricciones.
	 */
	public void validarCompra(Compra compra, boolean esNuevo) {
		if (compra == null)
			throw new ServiceException("La compra no puede ser nula.");
		// Proveedor
		Proveedor proveedor = compra.getProveedor();
		if (proveedor == null || proveedor.getIdProveedor() == null)
			throw new ServiceException("El proveedor es obligatorio.");
		// Empleado
		Empleado empleado = compra.getEmpleado();
		if (empleado == null || empleado.getIdEmpleado() == null)
			throw new ServiceException("El empleado es obligatorio.");
		// Totales (pueden ser nulos en el alta, pero si existen deben ser >= 0)
		if (compra.getTotalSinIva() != null && compra.getTotalSinIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El total sin IVA no puede ser negativo.");
		if (compra.getTotalConIva() != null && compra.getTotalConIva().compareTo(BigDecimal.ZERO) < 0)
			throw new ServiceException("El total con IVA no puede ser negativo.");
		// Estado
		if (compra.getEstado() == null)
			throw new ServiceException("El estado es obligatorio.");
	}

	@Override
	public void crearCompraConDetalles(Compra compra, List<DetalleCompra> detallesCompra) {
		if (compra == null || detallesCompra == null || detallesCompra.isEmpty())
			throw new ServiceException("Compra y detalles requeridos.");

		try {
			// 1. Guardar la compra y los detalles (transacción)
			compraDAO.saveWithDetails(compra, detallesCompra);

			// 2. Actualizar el stock de cada producto comprado
			for (DetalleCompra det : detallesCompra) {
				if (det.getProducto() != null && det.getCantidad() != null && det.getCantidad() > 0) {
					// Llama al método que incrementa el stock de un producto
					productoService.actualizarYReactivarStock(det.getProducto().getIdProducto(), det.getCantidad());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("Error al guardar compra con detalles y actualizar stock.", e);
		}
	}

}
