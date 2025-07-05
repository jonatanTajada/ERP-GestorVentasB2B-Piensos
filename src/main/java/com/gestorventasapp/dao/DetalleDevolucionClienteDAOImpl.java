package com.gestorventasapp.dao;

import com.gestorventasapp.model.DetalleDevolucionCliente;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.util.HibernateUtil;
import com.gestorventasapp.exceptions.DAOException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DetalleDevolucionClienteDAOImpl implements DetalleDevolucionClienteDAO {

	@Override
	public void save(DetalleDevolucionCliente detalle) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(detalle);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar el detalle de devolución de cliente.", e);
		}
	}

	@Override
	public void update(DetalleDevolucionCliente detalle) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(detalle);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar el detalle de devolución de cliente.", e);
		}
	}

	@Override
	public void delete(int idDetalle) {
		// Baja lógica: estado = inactivo
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			DetalleDevolucionCliente detalle = session.find(DetalleDevolucionCliente.class, idDetalle);
			if (detalle != null && detalle.getEstado() != Estado.inactivo) {
				detalle.setEstado(Estado.inactivo);
				session.merge(detalle);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica el detalle de devolución de cliente.", e);
		}
	}

	@Override
	public DetalleDevolucionCliente findById(int idDetalle) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(DetalleDevolucionCliente.class, idDetalle);
		} catch (Exception e) {
			throw new DAOException("Error al buscar el detalle de devolución de cliente por ID.", e);
		}
	}

	@Override
	public List<DetalleDevolucionCliente> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleDevolucionCliente", DetalleDevolucionCliente.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de devolución de cliente.", e);
		}
	}

	@Override
	public List<DetalleDevolucionCliente> findAllActivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleDevolucionCliente d WHERE d.estado = :estado",
					DetalleDevolucionCliente.class).setParameter("estado", Estado.activo).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de devolución de cliente activos.", e);
		}
	}

	@Override
	public List<DetalleDevolucionCliente> findAllInactivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleDevolucionCliente d WHERE d.estado = :estado",
					DetalleDevolucionCliente.class).setParameter("estado", Estado.inactivo).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de devolución de cliente inactivos.", e);
		}
	}

	@Override
	public List<DetalleDevolucionCliente> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleDevolucionCliente d WHERE d.estado = :estado",
					DetalleDevolucionCliente.class).setParameter("estado", estado).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles de devolución de cliente por estado.", e);
		}
	}

	@Override
	public List<DetalleDevolucionCliente> findByDevolucion(int idDevolucion) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionCliente d WHERE d.devolucionCliente.idDevolucionCliente = :idDevolucion AND d.estado = :estado";
			Query<DetalleDevolucionCliente> query = session.createQuery(hql, DetalleDevolucionCliente.class);
			query.setParameter("idDevolucion", idDevolucion);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por devolución.", e);
		}
	}

	@Override
	public List<DetalleDevolucionCliente> findByProducto(int idProducto) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionCliente d WHERE d.producto.idProducto = :idProducto AND d.estado = :estado";
			Query<DetalleDevolucionCliente> query = session.createQuery(hql, DetalleDevolucionCliente.class);
			query.setParameter("idProducto", idProducto);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por producto.", e);
		}
	}

	@Override
	public DetalleDevolucionCliente findByProductoAndDevolucion(int idProducto, int idDevolucion) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionCliente d WHERE d.producto.idProducto = :idProducto AND d.devolucionCliente.idDevolucionCliente = :idDevolucion AND d.estado = :estado";
			Query<DetalleDevolucionCliente> query = session.createQuery(hql, DetalleDevolucionCliente.class);
			query.setParameter("idProducto", idProducto);
			query.setParameter("idDevolucion", idDevolucion);
			query.setParameter("estado", Estado.activo);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalle concreto por producto y devolución.", e);
		}
	}

	@Override
	public List<DetalleDevolucionCliente> findByCantidadGreaterThan(int cantidad) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionCliente d WHERE d.cantidad > :cantidad AND d.estado = :estado";
			Query<DetalleDevolucionCliente> query = session.createQuery(hql, DetalleDevolucionCliente.class);
			query.setParameter("cantidad", cantidad);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles con cantidad mayor a " + cantidad + ".", e);
		}
	}

	@Override
	public List<DetalleDevolucionCliente> findBySubtotalSinIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionCliente d WHERE d.subtotalSinIva BETWEEN :min AND :max AND d.estado = :estado";
			Query<DetalleDevolucionCliente> query = session.createQuery(hql, DetalleDevolucionCliente.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por subtotal sin IVA.", e);
		}
	}

	@Override
	public List<DetalleDevolucionCliente> findBySubtotalConIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionCliente d WHERE d.subtotalConIva BETWEEN :min AND :max AND d.estado = :estado";
			Query<DetalleDevolucionCliente> query = session.createQuery(hql, DetalleDevolucionCliente.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por subtotal con IVA.", e);
		}
	}
}
