package com.gestorventasapp.dao;

import com.gestorventasapp.model.DevolucionProveedor;
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

public class DevolucionProveedorDAOImpl implements DevolucionProveedorDAO {

	@Override
	public void save(DevolucionProveedor devolucion) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(devolucion);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar la devolución a proveedor.", e);
		}
	}

	@Override
	public void update(DevolucionProveedor devolucion) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(devolucion);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar la devolución a proveedor.", e);
		}
	}

	@Override
	public void delete(int idDevolucion) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			DevolucionProveedor devolucion = session.find(DevolucionProveedor.class, idDevolucion);
			if (devolucion != null) {
				devolucion.setEstado(Estado.inactivo);
				session.merge(devolucion);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica la devolución a proveedor.", e);
		}
	}

	@Override
	public DevolucionProveedor findById(int idDevolucion) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(DevolucionProveedor.class, idDevolucion);
		} catch (Exception e) {
			throw new DAOException("Error al buscar la devolución a proveedor por ID.", e);
		}
	}

	@Override
	public List<DevolucionProveedor> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DevolucionProveedor", DevolucionProveedor.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar todas las devoluciones a proveedor.", e);
		}
	}

	@Override
	public List<DevolucionProveedor> findAllActivas() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionProveedor d WHERE d.estado = :estado";
			Query<DevolucionProveedor> query = session.createQuery(hql, DevolucionProveedor.class);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar devoluciones activas a proveedor.", e);
		}
	}

	@Override
	public List<DevolucionProveedor> findAllInactivas() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionProveedor d WHERE d.estado = :estado";
			Query<DevolucionProveedor> query = session.createQuery(hql, DevolucionProveedor.class);
			query.setParameter("estado", Estado.inactivo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar devoluciones inactivas a proveedor.", e);
		}
	}

	@Override
	public List<DevolucionProveedor> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionProveedor d WHERE d.estado = :estado";
			Query<DevolucionProveedor> query = session.createQuery(hql, DevolucionProveedor.class);
			query.setParameter("estado", estado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones a proveedor por estado.", e);
		}
	}

	@Override
	public List<DevolucionProveedor> findByProveedor(int idProveedor) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionProveedor d WHERE d.idProveedor = :idProveedor";
			Query<DevolucionProveedor> query = session.createQuery(hql, DevolucionProveedor.class);
			query.setParameter("idProveedor", idProveedor);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por proveedor.", e);
		}
	}

	@Override
	public List<DevolucionProveedor> findByCompra(int idCompra) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionProveedor d WHERE d.idCompra = :idCompra";
			Query<DevolucionProveedor> query = session.createQuery(hql, DevolucionProveedor.class);
			query.setParameter("idCompra", idCompra);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por compra.", e);
		}
	}

	@Override
	public List<DevolucionProveedor> findByEmpleado(int idEmpleado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionProveedor d WHERE d.idEmpleado = :idEmpleado";
			Query<DevolucionProveedor> query = session.createQuery(hql, DevolucionProveedor.class);
			query.setParameter("idEmpleado", idEmpleado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por empleado.", e);
		}
	}

	@Override
	public List<DevolucionProveedor> findByFecha(LocalDate fecha) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionProveedor d WHERE DATE(d.fecha) = :fecha";
			Query<DevolucionProveedor> query = session.createQuery(hql, DevolucionProveedor.class);
			Date fechaSql = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
			query.setParameter("fecha", fechaSql);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por fecha.", e);
		}
	}

	@Override
	public List<DevolucionProveedor> findByFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionProveedor d WHERE d.fecha BETWEEN :inicio AND :fin";
			Query<DevolucionProveedor> query = session.createQuery(hql, DevolucionProveedor.class);
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
	public List<DevolucionProveedor> findByMotivo(String motivo) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionProveedor d WHERE d.motivo LIKE :motivo";
			Query<DevolucionProveedor> query = session.createQuery(hql, DevolucionProveedor.class);
			query.setParameter("motivo", "%" + motivo + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por motivo.", e);
		}
	}

	@Override
	public List<DevolucionProveedor> findByTotalBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DevolucionProveedor d WHERE d.total BETWEEN :min AND :max";
			Query<DevolucionProveedor> query = session.createQuery(hql, DevolucionProveedor.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar devoluciones por importe total.", e);
		}
	}
}
