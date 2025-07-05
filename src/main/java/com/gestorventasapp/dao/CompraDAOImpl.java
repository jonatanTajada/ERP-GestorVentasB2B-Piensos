package com.gestorventasapp.dao;

import com.gestorventasapp.model.Compra;
import com.gestorventasapp.model.DetalleCompra;
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

public class CompraDAOImpl implements CompraDAO {

	@Override
	public void save(Compra compra) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(compra);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar la compra.", e);
		}
	}

	@Override
	public void update(Compra compra) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(compra);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar la compra.", e);
		}
	}

	@Override
	public void delete(int idCompra) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Compra compra = session.find(Compra.class, idCompra);
			if (compra != null) {
				compra.setEstado(Estado.inactivo);
				session.merge(compra);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica la compra.", e);
		}
	}

	@Override
	public Compra findById(int idCompra) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(Compra.class, idCompra);
		} catch (Exception e) {
			throw new DAOException("Error al buscar la compra por ID.", e);
		}
	}

	@Override
	public List<Compra> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM Compra", Compra.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar todas las compras.", e);
		}
	}

	@Override
	public List<Compra> findAllActivas() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Compra c WHERE c.estado = :estado";
			Query<Compra> query = session.createQuery(hql, Compra.class);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar compras activas.", e);
		}
	}

	@Override
	public List<Compra> findAllInactivas() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Compra c WHERE c.estado = :estado";
			Query<Compra> query = session.createQuery(hql, Compra.class);
			query.setParameter("estado", Estado.inactivo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar compras inactivas.", e);
		}
	}

	@Override
	public List<Compra> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Compra c WHERE c.estado = :estado";
			Query<Compra> query = session.createQuery(hql, Compra.class);
			query.setParameter("estado", estado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar compras por estado.", e);
		}
	}

	@Override
	public List<Compra> findByProveedor(int idProveedor) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Compra c WHERE c.idProveedor = :idProveedor";
			Query<Compra> query = session.createQuery(hql, Compra.class);
			query.setParameter("idProveedor", idProveedor);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar compras por proveedor.", e);
		}
	}

	@Override
	public List<Compra> findByEmpleado(int idEmpleado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Compra c WHERE c.idEmpleado = :idEmpleado";
			Query<Compra> query = session.createQuery(hql, Compra.class);
			query.setParameter("idEmpleado", idEmpleado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar compras por empleado.", e);
		}
	}

	@Override
	public List<Compra> findByFecha(LocalDate fecha) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Compra c WHERE DATE(c.fecha) = :fecha";
			Query<Compra> query = session.createQuery(hql, Compra.class);
			Date fechaSql = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
			query.setParameter("fecha", fechaSql);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar compras por fecha.", e);
		}
	}

	@Override
	public List<Compra> findByFechaRango(LocalDate fechaInicio, LocalDate fechaFin) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Compra c WHERE c.fecha BETWEEN :inicio AND :fin";
			Query<Compra> query = session.createQuery(hql, Compra.class);
			Date inicio = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date fin = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
			query.setParameter("inicio", inicio);
			query.setParameter("fin", fin);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar compras por rango de fechas.", e);
		}
	}

	@Override
	public List<Compra> findByTotalSinIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Compra c WHERE c.totalSinIva BETWEEN :min AND :max";
			Query<Compra> query = session.createQuery(hql, Compra.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar compras por rango de total sin IVA.", e);
		}
	}

	@Override
	public List<Compra> findByTotalConIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Compra c WHERE c.totalConIva BETWEEN :min AND :max";
			Query<Compra> query = session.createQuery(hql, Compra.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar compras por rango de total con IVA.", e);
		}
	}

	@Override
	public void saveWithDetails(Compra compra, List<DetalleCompra> detallesCompra) {
		Transaction transaction = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(compra); // Guarda la compra y obtiene el id generado
			session.flush(); // Asegura que el id se genere

			for (DetalleCompra detalle : detallesCompra) {
				detalle.setCompra(compra); // Asigna la compra (id_compra FK)
				session.persist(detalle);
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar compra con detalles", e);
		}
	}

}
