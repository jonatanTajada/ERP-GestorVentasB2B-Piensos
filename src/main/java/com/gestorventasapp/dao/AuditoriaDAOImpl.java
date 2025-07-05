package com.gestorventasapp.dao;

import com.gestorventasapp.model.Auditoria;
import com.gestorventasapp.util.HibernateUtil;
import com.gestorventasapp.exceptions.DAOException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AuditoriaDAOImpl implements AuditoriaDAO {

	@Override
	public void save(Auditoria auditoria) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(auditoria);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar el registro de auditoría.", e);
		}
	}

	@Override
	public Auditoria findById(int idAuditoria) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(Auditoria.class, idAuditoria);
		} catch (Exception e) {
			throw new DAOException("Error al buscar la auditoría por ID.", e);
		}
	}

	@Override
	public List<Auditoria> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM Auditoria", Auditoria.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar todos los registros de auditoría.", e);
		}
	}

	@Override
	public List<Auditoria> findByUsuario(String usuario) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Auditoria a WHERE a.usuario = :usuario";
			Query<Auditoria> query = session.createQuery(hql, Auditoria.class);
			query.setParameter("usuario", usuario);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar auditoría por usuario.", e);
		}
	}

	@Override
	public List<Auditoria> findByAccion(String accion) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Auditoria a WHERE a.accion = :accion";
			Query<Auditoria> query = session.createQuery(hql, Auditoria.class);
			query.setParameter("accion", accion);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar auditoría por acción.", e);
		}
	}

	@Override
	public List<Auditoria> findByEntidad(String entidad) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Auditoria a WHERE a.entidad = :entidad";
			Query<Auditoria> query = session.createQuery(hql, Auditoria.class);
			query.setParameter("entidad", entidad);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar auditoría por entidad.", e);
		}
	}

	@Override
	public List<Auditoria> findByFechaHoraRango(LocalDateTime desde, LocalDateTime hasta) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Auditoria a WHERE a.fechaHora BETWEEN :desde AND :hasta";
			Query<Auditoria> query = session.createQuery(hql, Auditoria.class);
			Date d = Date.from(desde.atZone(ZoneId.systemDefault()).toInstant());
			Date h = Date.from(hasta.atZone(ZoneId.systemDefault()).toInstant());
			query.setParameter("desde", d);
			query.setParameter("hasta", h);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar auditoría por rango de fechas.", e);
		}
	}

	@Override
	public List<Auditoria> findByDescripcionLike(String descripcion) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Auditoria a WHERE a.descripcion LIKE :desc";
			Query<Auditoria> query = session.createQuery(hql, Auditoria.class);
			query.setParameter("desc", "%" + descripcion + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar auditoría por descripción.", e);
		}
	}
}
