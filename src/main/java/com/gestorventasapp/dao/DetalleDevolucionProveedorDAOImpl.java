package com.gestorventasapp.dao;

import com.gestorventasapp.model.DetalleDevolucionProveedor;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.util.HibernateUtil;
import com.gestorventasapp.exceptions.DAOException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DetalleDevolucionProveedorDAOImpl implements DetalleDevolucionProveedorDAO {

	@Override
	public void save(DetalleDevolucionProveedor detalle) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(detalle);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar el detalle de devolución a proveedor.", e);
		}
	}

	@Override
	public void update(DetalleDevolucionProveedor detalle) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(detalle);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar el detalle de devolución a proveedor.", e);
		}
	}

	@Override
	public void delete(int idDetalle) {
		// Baja lógica: estado = inactivo
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			DetalleDevolucionProveedor detalle = session.find(DetalleDevolucionProveedor.class, idDetalle);
			if (detalle != null && detalle.getEstado() != Estado.inactivo) {
				detalle.setEstado(Estado.inactivo);
				session.merge(detalle);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica el detalle de devolución a proveedor.", e);
		}
	}

	@Override
	public DetalleDevolucionProveedor findById(int idDetalle) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(DetalleDevolucionProveedor.class, idDetalle);
		} catch (Exception e) {
			throw new DAOException("Error al buscar el detalle de devolución a proveedor por ID.", e);
		}
	}

	@Override
	public List<DetalleDevolucionProveedor> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleDevolucionProveedor", DetalleDevolucionProveedor.class)
					.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de devolución a proveedor.", e);
		}
	}

	@Override
	public List<DetalleDevolucionProveedor> findAllActivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleDevolucionProveedor d WHERE d.estado = :estado",
					DetalleDevolucionProveedor.class).setParameter("estado", Estado.activo).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de devolución a proveedor activos.", e);
		}
	}

	@Override
	public List<DetalleDevolucionProveedor> findAllInactivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleDevolucionProveedor d WHERE d.estado = :estado",
					DetalleDevolucionProveedor.class).setParameter("estado", Estado.inactivo).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar los detalles de devolución a proveedor inactivos.", e);
		}
	}

	@Override
	public List<DetalleDevolucionProveedor> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM DetalleDevolucionProveedor d WHERE d.estado = :estado",
					DetalleDevolucionProveedor.class).setParameter("estado", estado).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles de devolución a proveedor por estado.", e);
		}
	}

	@Override
	public List<DetalleDevolucionProveedor> findByDevolucion(int idDevolucion) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionProveedor d WHERE d.devolucionProveedor.idDevolucionProveedor = :idDevolucion AND d.estado = :estado";
			Query<DetalleDevolucionProveedor> query = session.createQuery(hql, DetalleDevolucionProveedor.class);
			query.setParameter("idDevolucion", idDevolucion);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por devolución.", e);
		}
	}

	@Override
	public List<DetalleDevolucionProveedor> findByProducto(int idProducto) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionProveedor d WHERE d.producto.idProducto = :idProducto AND d.estado = :estado";
			Query<DetalleDevolucionProveedor> query = session.createQuery(hql, DetalleDevolucionProveedor.class);
			query.setParameter("idProducto", idProducto);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por producto.", e);
		}
	}

	@Override
	public DetalleDevolucionProveedor findByProductoAndDevolucion(int idProducto, int idDevolucion) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionProveedor d WHERE d.producto.idProducto = :idProducto AND d.devolucionProveedor.idDevolucionProveedor = :idDevolucion AND d.estado = :estado";
			Query<DetalleDevolucionProveedor> query = session.createQuery(hql, DetalleDevolucionProveedor.class);
			query.setParameter("idProducto", idProducto);
			query.setParameter("idDevolucion", idDevolucion);
			query.setParameter("estado", Estado.activo);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalle concreto por producto y devolución.", e);
		}
	}

	@Override
	public List<DetalleDevolucionProveedor> findByCantidadGreaterThan(int cantidad) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionProveedor d WHERE d.cantidad > :cantidad AND d.estado = :estado";
			Query<DetalleDevolucionProveedor> query = session.createQuery(hql, DetalleDevolucionProveedor.class);
			query.setParameter("cantidad", cantidad);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles con cantidad mayor a " + cantidad + ".", e);
		}
	}

	@Override
	public List<DetalleDevolucionProveedor> findBySubtotalSinIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionProveedor d WHERE d.subtotalSinIva BETWEEN :min AND :max AND d.estado = :estado";
			Query<DetalleDevolucionProveedor> query = session.createQuery(hql, DetalleDevolucionProveedor.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por subtotal sin IVA.", e);
		}
	}

	@Override
	public List<DetalleDevolucionProveedor> findBySubtotalConIvaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM DetalleDevolucionProveedor d WHERE d.subtotalConIva BETWEEN :min AND :max AND d.estado = :estado";
			Query<DetalleDevolucionProveedor> query = session.createQuery(hql, DetalleDevolucionProveedor.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar detalles por subtotal con IVA.", e);
		}
	}
}
