package com.gestorventasapp.dao;

import com.gestorventasapp.model.Empleado;
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

public class EmpleadoDAOImpl implements EmpleadoDAO {

	@Override
	public void save(Empleado empleado) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(empleado);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar el empleado.", e);
		}
	}

	@Override
	public void update(Empleado empleado) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(empleado);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar el empleado.", e);
		}
	}

	@Override
	public void delete(int idEmpleado) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Empleado empleado = session.find(Empleado.class, idEmpleado);
			if (empleado != null) {
				empleado.setEstado(Estado.inactivo);
				session.merge(empleado);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica el empleado.", e);
		}
	}

	@Override
	public Empleado findById(int idEmpleado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(Empleado.class, idEmpleado);
		} catch (Exception e) {
			throw new DAOException("Error al buscar el empleado por ID.", e);
		}
	}

	@Override
	public Empleado findByDni(String dni) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Empleado e WHERE e.dni = :dni";
			Query<Empleado> query = session.createQuery(hql, Empleado.class);
			query.setParameter("dni", dni);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar el empleado por DNI.", e);
		}
	}

	@Override
	public Empleado findByEmail(String email) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Empleado e WHERE e.email = :email";
			Query<Empleado> query = session.createQuery(hql, Empleado.class);
			query.setParameter("email", email);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar el empleado por email.", e);
		}
	}

	@Override
	public List<Empleado> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM Empleado", Empleado.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar todos los empleados.", e);
		}
	}

	@Override
	public List<Empleado> findAllActivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Empleado e WHERE e.estado = :estado";
			Query<Empleado> query = session.createQuery(hql, Empleado.class);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar empleados activos.", e);
		}
	}

	@Override
	public List<Empleado> findAllInactivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Empleado e WHERE e.estado = :estado";
			Query<Empleado> query = session.createQuery(hql, Empleado.class);
			query.setParameter("estado", Estado.inactivo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar empleados inactivos.", e);
		}
	}

	@Override
	public List<Empleado> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Empleado e WHERE e.estado = :estado";
			Query<Empleado> query = session.createQuery(hql, Empleado.class);
			query.setParameter("estado", estado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar empleados por estado.", e);
		}
	}

	@Override
	public List<Empleado> findByNombre(String nombre) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Empleado e WHERE e.nombre LIKE :nombre";
			Query<Empleado> query = session.createQuery(hql, Empleado.class);
			query.setParameter("nombre", "%" + nombre + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar empleados por nombre.", e);
		}
	}

	@Override
	public List<Empleado> findByApellido(String apellido) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Empleado e WHERE e.apellido1 LIKE :apellido OR e.apellido2 LIKE :apellido";
			Query<Empleado> query = session.createQuery(hql, Empleado.class);
			query.setParameter("apellido", "%" + apellido + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar empleados por apellido.", e);
		}
	}

	@Override
	public List<Empleado> findByLocalidad(String localidad) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Empleado e WHERE e.localidad LIKE :localidad";
			Query<Empleado> query = session.createQuery(hql, Empleado.class);
			query.setParameter("localidad", "%" + localidad + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar empleados por localidad.", e);
		}
	}

	@Override
	public List<Empleado> findByFechaAlta(LocalDate fechaInicio, LocalDate fechaFin) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Empleado e WHERE e.fechaAlta BETWEEN :inicio AND :fin";
			Query<Empleado> query = session.createQuery(hql, Empleado.class);
			Date inicio = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date fin = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
			query.setParameter("inicio", inicio);
			query.setParameter("fin", fin);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar empleados por fecha de alta.", e);
		}
	}

	@Override
	public boolean existsDni(String dni) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT count(e) FROM Empleado e WHERE e.dni = :dni";
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("dni", dni);
			return query.uniqueResult() > 0;
		} catch (Exception e) {
			throw new DAOException("Error al comprobar existencia de DNI.", e);
		}
	}

	@Override
	public boolean existsEmail(String email) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT count(e) FROM Empleado e WHERE e.email = :email";
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("email", email);
			return query.uniqueResult() > 0;
		} catch (Exception e) {
			throw new DAOException("Error al comprobar existencia de email.", e);
		}
	}

	@Override
	public List<Empleado> findByTelefono(String telefono) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Empleado e WHERE e.telefono = :telefono";
			Query<Empleado> query = session.createQuery(hql, Empleado.class);
			query.setParameter("telefono", telefono);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar empleados por teléfono.", e);
		}
	}
}
