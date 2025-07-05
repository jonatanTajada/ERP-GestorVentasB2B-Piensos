package com.gestorventasapp.dao;

import com.gestorventasapp.model.DevolucionCliente;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.util.HibernateUtil;
import com.gestorventasapp.exceptions.DAOException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class DevolucionClienteDAOImpl implements DevolucionClienteDAO {

	@Override
	public void save(DevolucionCliente devolucion) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(devolucion);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar la devolución de cliente.", e);
		}
	}

	@Override
	public void update(DevolucionCliente devolucion) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(devolucion);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar la devolución de cliente.", e);
		}
	}

	@Override
	public void delete(int idDevolucion) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			DevolucionCliente devolucion = session.find(DevolucionCliente.class, idDevolucion);
			if (devolucion != null) {
				devolucion.setEstado(Estado.inactivo);
				session.merge(devolucion);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica la devolución de cliente.", e);
		}
	}

	@Override
	public DevolucionCliente findById(int idDevolucion) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(DevolucionCliente.class, idDevolucion);
		} catch (Exception e) {
			throw new DAOException("Error al buscar la devolución de cliente por ID.", e);
		}
	}

	@Override
	public List<DevolucionCliente> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DevolucionCliente", DevolucionCliente.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar todas las devoluciones de cliente.", e);
		}
	}

	@Override
	public List<DevolucionCliente> findAllActivas() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionCliente d WHERE d.estado = :estado";
			Query<DevolucionCliente> query = session.createQuery(hql, DevolucionCliente.class);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar devoluciones activas de cliente.", e);
		}
	}

	@Override
	public List<DevolucionCliente> findAllInactivas() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionCliente d WHERE d.estado = :estado";
			Query<DevolucionCliente> query = session.createQuery(hql, DevolucionCliente.class);
			query.setParameter("estado", Estado.inactivo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar devoluciones inactivas de cliente.", e);
		}
	}

	@Override
	public List<DevolucionCliente> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionCliente d WHERE d.estado = :estado";
			Query<DevolucionCliente> query = session.createQuery(hql, DevolucionCliente.class);
			query.setParameter("estado", estado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones de cliente por estado.", e);
		}
	}

	@Override
	public List<DevolucionCliente> findByCliente(int idCliente) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionCliente d WHERE d.idCliente = :idCliente";
			Query<DevolucionCliente> query = session.createQuery(hql, DevolucionCliente.class);
			query.setParameter("idCliente", idCliente);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por cliente.", e);
		}
	}

	@Override
	public List<DevolucionCliente> findByVenta(int idVenta) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionCliente d WHERE d.idVenta = :idVenta";
			Query<DevolucionCliente> query = session.createQuery(hql, DevolucionCliente.class);
			query.setParameter("idVenta", idVenta);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por venta.", e);
		}
	}

	@Override
	public List<DevolucionCliente> findByEmpleado(int idEmpleado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionCliente d WHERE d.idEmpleado = :idEmpleado";
			Query<DevolucionCliente> query = session.createQuery(hql, DevolucionCliente.class);
			query.setParameter("idEmpleado", idEmpleado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por empleado.", e);
		}
	}

	@Override
	public List<DevolucionCliente> findByFecha(LocalDate fecha) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionCliente d WHERE DATE(d.fecha) = :fecha";
			Query<DevolucionCliente> query = session.createQuery(hql, DevolucionCliente.class);
			Date fechaSql = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
			query.setParameter("fecha", fechaSql);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por fecha.", e);
		}
	}

	@Override
	public List<DevolucionCliente> findByFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionCliente d WHERE d.fecha BETWEEN :inicio AND :fin";
			Query<DevolucionCliente> query = session.createQuery(hql, DevolucionCliente.class);
			Date inicio = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date fin = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
			query.setParameter("inicio", inicio);
			query.setParameter("fin", fin);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por rango de fechas.", e);
		}
	}

	@Override
	public List<DevolucionCliente> findByMotivo(String motivo) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionCliente d WHERE d.motivo LIKE :motivo";
			Query<DevolucionCliente> query = session.createQuery(hql, DevolucionCliente.class);
			query.setParameter("motivo", "%" + motivo + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por motivo.", e);
		}
	}

	@Override
	public List<DevolucionCliente> findByTotalBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionCliente d WHERE d.total BETWEEN :min AND :max";
			Query<DevolucionCliente> query = session.createQuery(hql, DevolucionCliente.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por importe total.", e);
		}
	}
}
