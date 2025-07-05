package com.gestorventasapp.dao;

import com.gestorventasapp.model.Usuario;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.util.HibernateUtil;
import com.gestorventasapp.exceptions.DAOException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {

	@Override
	public void save(Usuario usuario) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(usuario);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar el usuario.", e);
		}
	}

	@Override
	public void update(Usuario usuario) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(usuario);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar el usuario.", e);
		}
	}

	@Override
	public void delete(int idUsuario) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Usuario usuario = session.find(Usuario.class, idUsuario);
			if (usuario != null) {
				usuario.setEstado(Estado.inactivo);
				session.merge(usuario);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica el usuario.", e);
		}
	}

	@Override
	public Usuario findById(int idUsuario) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(Usuario.class, idUsuario);
		} catch (Exception e) {
			throw new DAOException("Error al buscar el usuario por ID.", e);
		}
	}

	@Override
	public Usuario findByNombreUsuario(String nombreUsuario) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario";
			Query<Usuario> query = session.createQuery(hql, Usuario.class);
			query.setParameter("nombreUsuario", nombreUsuario);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar el usuario por nombre de usuario.", e);
		}
	}

	@Override
	public Usuario findByIdEmpleado(int idEmpleado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Usuario u WHERE u.idEmpleado = :idEmpleado";
			Query<Usuario> query = session.createQuery(hql, Usuario.class);
			query.setParameter("idEmpleado", idEmpleado);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar el usuario por ID de empleado.", e);
		}
	}

	@Override
	public List<Usuario> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM Usuario", Usuario.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar todos los usuarios.", e);
		}
	}

	@Override
	public List<Usuario> findAllActivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Usuario u WHERE u.estado = :estado";
			Query<Usuario> query = session.createQuery(hql, Usuario.class);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar usuarios activos.", e);
		}
	}

	@Override
	public List<Usuario> findAllInactivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Usuario u WHERE u.estado = :estado";
			Query<Usuario> query = session.createQuery(hql, Usuario.class);
			query.setParameter("estado", Estado.inactivo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar usuarios inactivos.", e);
		}
	}

	@Override
	public List<Usuario> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Usuario u WHERE u.estado = :estado";
			Query<Usuario> query = session.createQuery(hql, Usuario.class);
			query.setParameter("estado", estado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar usuarios por estado.", e);
		}
	}

	@Override
	public List<Usuario> findByTipo(String tipo) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Usuario u WHERE u.tipo = :tipo";
			Query<Usuario> query = session.createQuery(hql, Usuario.class);
			query.setParameter("tipo", tipo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar usuarios por tipo.", e);
		}
	}

	@Override
	public boolean existsNombreUsuario(String nombreUsuario) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT count(u) FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario";
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("nombreUsuario", nombreUsuario);
			return query.uniqueResult() > 0;
		} catch (Exception e) {
			throw new DAOException("Error al comprobar existencia de nombre de usuario.", e);
		}
	}

	@Override
	public boolean existsIdEmpleado(int idEmpleado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT count(u) FROM Usuario u WHERE u.idEmpleado = :idEmpleado";
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("idEmpleado", idEmpleado);
			return query.uniqueResult() > 0;
		} catch (Exception e) {
			throw new DAOException("Error al comprobar existencia de usuario para el empleado.", e);
		}
	}

	@Override
	public boolean checkLogin(String nombreUsuario, String contrasena) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario AND u.contrasena = :contrasena AND u.estado = :estado";
			Query<Usuario> query = session.createQuery(hql, Usuario.class);
			query.setParameter("nombreUsuario", nombreUsuario);
			query.setParameter("contrasena", contrasena); // Ojo: Solo válido si guardas el hash, nunca la contraseña en
															// claro
			query.setParameter("estado", Estado.activo);
			return query.uniqueResult() != null;
		} catch (Exception e) {
			throw new DAOException("Error al validar login del usuario.", e);
		}
	}
}
