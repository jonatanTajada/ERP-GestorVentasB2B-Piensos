package com.gestorventasapp.dao;

import com.gestorventasapp.model.DetalleVenta;
import com.gestorventasapp.model.Venta;
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

public class VentaDAOImpl implements VentaDAO {

	@Override
	public void save(Venta venta) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(venta);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar la venta.", e);
		}
	}

	@Override
	public void update(Venta venta) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(venta);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar la venta.", e);
		}
	}

	@Override
	public void delete(int idVenta) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Venta venta = session.find(Venta.class, idVenta);
			if (venta != null) {
				venta.setEstado(Estado.inactivo);
				session.merge(venta);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica la venta.", e);
		}
	}

	@Override
	public Venta findById(int idVenta) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(Venta.class, idVenta);
		} catch (Exception e) {
			throw new DAOException("Error al buscar la venta por ID.", e);
		}
	}

	@Override
	public List<Venta> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM Venta", Venta.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar todas las ventas.", e);
		}
	}

	@Override
	public List<Venta> findAllActivas() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Venta v WHERE v.estado = :estado";
			Query<Venta> query = session.createQuery(hql, Venta.class);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar ventas activas.", e);
		}
	}

	@Override
	public List<Venta> findAllInactivas() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Venta v WHERE v.estado = :estado";
			Query<Venta> query = session.createQuery(hql, Venta.class);
			query.setParameter("estado", Estado.inactivo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar ventas inactivas.", e);
		}
	}

	@Override
	public List<Venta> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Venta v WHERE v.estado = :estado";
			Query<Venta> query = session.createQuery(hql, Venta.class);
			query.setParameter("estado", estado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar ventas por estado.", e);
		}
	}

	@Override
	public List<Venta> findByCliente(int idCliente) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Venta v WHERE v.idCliente = :idCliente";
			Query<Venta> query = session.createQuery(hql, Venta.class);
			query.setParameter("idCliente", idCliente);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar ventas por cliente.", e);
		}
	}

	@Override
	public List<Venta> findByEmpleado(int idEmpleado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Venta v WHERE v.idEmpleado = :idEmpleado";
			Query<Venta> query = session.createQuery(hql, Venta.class);
			query.setParameter("idEmpleado", idEmpleado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar ventas por empleado.", e);
		}
	}

	@Override
	public List<Venta> findByFecha(LocalDate fecha) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Venta v WHERE DATE(v.fecha) = :fecha";
			Query<Venta> query = session.createQuery(hql, Venta.class);
			Date fechaSql = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
			query.setParameter("fecha", fechaSql);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar ventas por fecha.", e);
		}
	}

	@Override
	public List<Venta> findByFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin";
			Query<Venta> query = session.createQuery(hql, Venta.class);
			Date inicio = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date fin = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
			query.setParameter("inicio", inicio);
			query.setParameter("fin", fin);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar ventas por rango de fechas.", e);
		}
	}

	@Override
	public List<Venta> findByTotalSinIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Venta v WHERE v.totalSinIva BETWEEN :min AND :max";
			Query<Venta> query = session.createQuery(hql, Venta.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar ventas por rango de total sin IVA.", e);
		}
	}

	@Override
	public List<Venta> findByTotalConIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Venta v WHERE v.totalConIva BETWEEN :min AND :max";
			Query<Venta> query = session.createQuery(hql, Venta.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar ventas por rango de total con IVA.", e);
		}
	}

	@Override
	public void saveWithDetails(Venta venta, List<DetalleVenta> detallesVenta) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(venta); // Guarda la venta y obtiene el id generado
			session.flush(); // Asegura que el id se genere

			for (DetalleVenta detalle : detallesVenta) {
				detalle.setVenta(venta); // Asigna la venta (id_venta FK)
				session.persist(detalle);
			}

			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar venta con detalles", e);
		}
	}

}
