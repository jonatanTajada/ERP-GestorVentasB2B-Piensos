package com.gestorventasapp.dao;

import com.gestorventasapp.model.DetalleCompra;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.util.HibernateUtil;
import com.gestorventasapp.exceptions.DAOException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DetalleCompraDAOImpl implements DetalleCompraDAO {

	@Override
	public void save(DetalleCompra detalleCompra) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(detalleCompra);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar el detalle de compra.", e);
		}
	}

	@Override
	public void update(DetalleCompra detalleCompra) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(detalleCompra);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar el detalle de compra.", e);
		}
	}

	@Override
	public void delete(int idDetalleCompra) {
		// Baja lógica: estado = inactivo
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			DetalleCompra detalle = session.find(DetalleCompra.class, idDetalleCompra);
			if (detalle != null && detalle.getEstado() != Estado.inactivo) {
				detalle.setEstado(Estado.inactivo);
				session.merge(detalle);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica el detalle de compra.", e);
		}
	}

	@Override
	public DetalleCompra findById(int idDetalleCompra) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(DetalleCompra.class, idDetalleCompra);
		} catch (Exception e) {
			throw new DAOException("Error al buscar el detalle de compra por ID.", e);
		}
	}

	@Override
	public List<DetalleCompra> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleCompra", DetalleCompra.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de compra.", e);
		}
	}

	@Override
	public List<DetalleCompra> findAllActivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleCompra d WHERE d.estado = :estado", DetalleCompra.class)
					.setParameter("estado", Estado.activo).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de compra activos.", e);
		}
	}

	@Override
	public List<DetalleCompra> findAllInactivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleCompra d WHERE d.estado = :estado", DetalleCompra.class)
					.setParameter("estado", Estado.inactivo).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de compra inactivos.", e);
		}
	}

	@Override
	public List<DetalleCompra> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleCompra d WHERE d.estado = :estado", DetalleCompra.class)
					.setParameter("estado", estado).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de compra por estado.", e);
		}
	}

	@Override
	public List<DetalleCompra> findByCompra(int idCompra) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleCompra d WHERE d.compra.idCompra = :idCompra AND d.estado = :estado";
			Query<DetalleCompra> query = session.createQuery(hql, DetalleCompra.class);
			query.setParameter("idCompra", idCompra);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por compra.", e);
		}
	}

	@Override
	public List<DetalleCompra> findByProducto(int idProducto) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleCompra d WHERE d.producto.idProducto = :idProducto AND d.estado = :estado";
			Query<DetalleCompra> query = session.createQuery(hql, DetalleCompra.class);
			query.setParameter("idProducto", idProducto);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por producto.", e);
		}
	}

	@Override
	public DetalleCompra findByProductoAndCompra(int idProducto, int idCompra) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleCompra d WHERE d.producto.idProducto = :idProducto AND d.compra.idCompra = :idCompra AND d.estado = :estado";
			Query<DetalleCompra> query = session.createQuery(hql, DetalleCompra.class);
			query.setParameter("idProducto", idProducto);
			query.setParameter("idCompra", idCompra);
			query.setParameter("estado", Estado.activo);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalle por producto y compra.", e);
		}
	}

	@Override
	public List<DetalleCompra> findByCantidadGreaterThan(int cantidad) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleCompra d WHERE d.cantidad > :cantidad AND d.estado = :estado";
			Query<DetalleCompra> query = session.createQuery(hql, DetalleCompra.class);
			query.setParameter("cantidad", cantidad);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles con cantidad mayor a " + cantidad + ".", e);
		}
	}

	@Override
	public List<DetalleCompra> findBySubtotalSinIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleCompra d WHERE d.subtotalSinIva BETWEEN :min AND :max AND d.estado = :estado";
			Query<DetalleCompra> query = session.createQuery(hql, DetalleCompra.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por subtotal sin IVA.", e);
		}
	}

	@Override
	public List<DetalleCompra> findBySubtotalConIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleCompra d WHERE d.subtotalConIva BETWEEN :min AND :max AND d.estado = :estado";
			Query<DetalleCompra> query = session.createQuery(hql, DetalleCompra.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por subtotal con IVA.", e);
		}
	}
}
