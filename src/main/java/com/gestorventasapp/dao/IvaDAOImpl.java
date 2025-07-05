package com.gestorventasapp.dao;

import com.gestorventasapp.model.Iva;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.util.HibernateUtil;
import com.gestorventasapp.exceptions.DAOException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.List;

public class IvaDAOImpl implements IvaDAO {

	@Override
	public void save(Iva iva) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(iva);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar el IVA.", e);
		}
	}

	@Override
	public void update(Iva iva) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(iva);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar el IVA.", e);
		}
	}

	@Override
	public void delete(int idIva) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Iva iva = session.find(Iva.class, idIva);
			if (iva != null) {
				iva.setEstado(Estado.inactivo);
				session.merge(iva);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica el IVA.", e);
		}
	}

	@Override
	public Iva findById(int idIva) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(Iva.class, idIva);
		} catch (Exception e) {
			throw new DAOException("Error al buscar el IVA por ID.", e);
		}
	}

	@Override
	public Iva findByDescripcion(String descripcion) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Iva i WHERE i.descripcion = :desc";
			Query<Iva> query = session.createQuery(hql, Iva.class);
			query.setParameter("desc", descripcion);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar IVA por descripción.", e);
		}
	}

	@Override
	public Iva findByPorcentaje(BigDecimal porcentaje) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Iva i WHERE i.porcentaje = :porc";
			Query<Iva> query = session.createQuery(hql, Iva.class);
			query.setParameter("porc", porcentaje);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar IVA por porcentaje.", e);
		}
	}

	@Override
	public List<Iva> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM Iva", Iva.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar todos los IVAs.", e);
		}
	}

	@Override
	public List<Iva> findAllActivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Iva i WHERE i.estado = :estado";
			Query<Iva> query = session.createQuery(hql, Iva.class);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar IVAs activos.", e);
		}
	}

	@Override
	public List<Iva> findAllInactivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Iva i WHERE i.estado = :estado";
			Query<Iva> query = session.createQuery(hql, Iva.class);
			query.setParameter("estado", Estado.inactivo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar IVAs inactivos.", e);
		}
	}

	@Override
	public List<Iva> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Iva i WHERE i.estado = :estado";
			Query<Iva> query = session.createQuery(hql, Iva.class);
			query.setParameter("estado", estado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar IVAs por estado.", e);
		}
	}

	@Override
	public List<Iva> findByDescripcionLike(String descripcion) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Iva i WHERE i.descripcion LIKE :desc";
			Query<Iva> query = session.createQuery(hql, Iva.class);
			query.setParameter("desc", "%" + descripcion + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar IVAs por descripción similar.", e);
		}
	}

	@Override
	public List<Iva> findByPorcentajeRango(BigDecimal min, BigDecimal max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Iva i WHERE i.porcentaje BETWEEN :min AND :max";
			Query<Iva> query = session.createQuery(hql, Iva.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar IVAs por rango de porcentaje.", e);
		}
	}

	@Override
	public boolean existsDescripcion(String descripcion) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT count(i) FROM Iva i WHERE i.descripcion = :desc";
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("desc", descripcion);
			return query.uniqueResult() > 0;
		} catch (Exception e) {
			throw new DAOException("Error al comprobar existencia de descripción de IVA.", e);
		}
	}

	@Override
	public boolean existsPorcentaje(BigDecimal porcentaje) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT count(i) FROM Iva i WHERE i.porcentaje = :porc";
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("porc", porcentaje);
			return query.uniqueResult() > 0;
		} catch (Exception e) {
			throw new DAOException("Error al comprobar existencia de porcentaje de IVA.", e);
		}
	}
}
