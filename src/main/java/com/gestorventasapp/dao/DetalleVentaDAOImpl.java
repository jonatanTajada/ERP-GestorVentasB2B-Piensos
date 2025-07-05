package com.gestorventasapp.dao;

import com.gestorventasapp.model.DetalleVenta;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.util.HibernateUtil;
import com.gestorventasapp.exceptions.DAOException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DetalleVentaDAOImpl implements DetalleVentaDAO {

	@Override
	public void save(DetalleVenta detalleVenta) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(detalleVenta);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar el detalle de venta.", e);
		}
	}

	@Override
	public void update(DetalleVenta detalleVenta) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(detalleVenta);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar el detalle de venta.", e);
		}
	}

	@Override
	public void delete(int idDetalleVenta) {
		// Baja lógica: estado = inactivo
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			DetalleVenta detalle = session.find(DetalleVenta.class, idDetalleVenta);
			if (detalle != null && detalle.getEstado() != Estado.inactivo) {
				detalle.setEstado(Estado.inactivo);
				session.merge(detalle);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica el detalle de venta.", e);
		}
	}

	@Override
	public DetalleVenta findById(int idDetalleVenta) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(DetalleVenta.class, idDetalleVenta);
		} catch (Exception e) {
			throw new DAOException("Error al buscar el detalle de venta por ID.", e);
		}
	}

	@Override
	public List<DetalleVenta> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleVenta", DetalleVenta.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de venta.", e);
		}
	}

	@Override
	public List<DetalleVenta> findAllActivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleVenta d WHERE d.estado = :estado", DetalleVenta.class)
					.setParameter("estado", Estado.activo).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de venta activos.", e);
		}
	}

	@Override
	public List<DetalleVenta> findAllInactivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleVenta d WHERE d.estado = :estado", DetalleVenta.class)
					.setParameter("estado", Estado.inactivo).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de venta inactivos.", e);
		}
	}

	@Override
	public List<DetalleVenta> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleVenta d WHERE d.estado = :estado", DetalleVenta.class)
					.setParameter("estado", estado).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de venta por estado.", e);
		}
	}

	@Override
	public List<DetalleVenta> findByVenta(int idVenta) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleVenta d WHERE d.venta.idVenta = :idVenta AND d.estado = :estado";
			Query<DetalleVenta> query = session.createQuery(hql, DetalleVenta.class);
			query.setParameter("idVenta", idVenta);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por venta.", e);
		}
	}

	@Override
	public List<DetalleVenta> findByProducto(int idProducto) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleVenta d WHERE d.producto.idProducto = :idProducto AND d.estado = :estado";
			Query<DetalleVenta> query = session.createQuery(hql, DetalleVenta.class);
			query.setParameter("idProducto", idProducto);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por producto.", e);
		}
	}

	@Override
	public DetalleVenta findByProductoAndVenta(int idProducto, int idVenta) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleVenta d WHERE d.producto.idProducto = :idProducto AND d.venta.idVenta = :idVenta AND d.estado = :estado";
			Query<DetalleVenta> query = session.createQuery(hql, DetalleVenta.class);
			query.setParameter("idProducto", idProducto);
			query.setParameter("idVenta", idVenta);
			query.setParameter("estado", Estado.activo);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalle por producto y venta.", e);
		}
	}

	@Override
	public List<DetalleVenta> findByCantidadGreaterThan(int cantidad) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleVenta d WHERE d.cantidad > :cantidad AND d.estado = :estado";
			Query<DetalleVenta> query = session.createQuery(hql, DetalleVenta.class);
			query.setParameter("cantidad", cantidad);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles con cantidad mayor a " + cantidad + ".", e);
		}
	}

	@Override
	public List<DetalleVenta> findBySubtotalSinIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleVenta d WHERE d.subtotalSinIva BETWEEN :min AND :max AND d.estado = :estado";
			Query<DetalleVenta> query = session.createQuery(hql, DetalleVenta.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por subtotal sin IVA.", e);
		}
	}

	@Override
	public List<DetalleVenta> findBySubtotalConIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleVenta d WHERE d.subtotalConIva BETWEEN :min AND :max AND d.estado = :estado";
			Query<DetalleVenta> query = session.createQuery(hql, DetalleVenta.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por subtotal con IVA.", e);
		}
	}
}
