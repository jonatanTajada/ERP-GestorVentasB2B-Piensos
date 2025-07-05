package com.gestorventasapp.dao;

import com.gestorventasapp.model.Proveedor;
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

public class ProveedorDAOImpl implements ProveedorDAO {

	@Override
	public void save(Proveedor proveedor) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(proveedor);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar el proveedor.", e);
		}
	}

	@Override
	public void update(Proveedor proveedor) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(proveedor);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar el proveedor.", e);
		}
	}

	@Override
	public void delete(int idProveedor) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Proveedor proveedor = session.find(Proveedor.class, idProveedor);
			if (proveedor != null) {
				proveedor.setEstado(Estado.inactivo);
				session.merge(proveedor);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica el proveedor.", e);
		}
	}

	@Override
	public Proveedor findById(int idProveedor) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(Proveedor.class, idProveedor);
		} catch (Exception e) {
			throw new DAOException("Error al buscar el proveedor por ID.", e);
		}
	}

	@Override
	public Proveedor findByCifNif(String cifNif) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Proveedor p WHERE p.cifNif = :cifNif";
			Query<Proveedor> query = session.createQuery(hql, Proveedor.class);
			query.setParameter("cifNif", cifNif);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar el proveedor por CIF/NIF.", e);
		}
	}

	@Override
	public Proveedor findByEmail(String email) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Proveedor p WHERE p.email = :email";
			Query<Proveedor> query = session.createQuery(hql, Proveedor.class);
			query.setParameter("email", email);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar el proveedor por email.", e);
		}
	}

	@Override
	public List<Proveedor> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM Proveedor", Proveedor.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar todos los proveedores.", e);
		}
	}

	@Override
	public List<Proveedor> findAllActivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Proveedor p WHERE p.estado = :estado";
			Query<Proveedor> query = session.createQuery(hql, Proveedor.class);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar proveedores activos.", e);
		}
	}

	@Override
	public List<Proveedor> findAllInactivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Proveedor p WHERE p.estado = :estado";
			Query<Proveedor> query = session.createQuery(hql, Proveedor.class);
			query.setParameter("estado", Estado.inactivo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar proveedores inactivos.", e);
		}
	}

	@Override
	public List<Proveedor> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Proveedor p WHERE p.estado = :estado";
			Query<Proveedor> query = session.createQuery(hql, Proveedor.class);
			query.setParameter("estado", estado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar proveedores por estado.", e);
		}
	}

	@Override
	public List<Proveedor> findByRazonSocial(String razonSocial) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Proveedor p WHERE p.razonSocial LIKE :razonSocial";
			Query<Proveedor> query = session.createQuery(hql, Proveedor.class);
			query.setParameter("razonSocial", "%" + razonSocial + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar proveedores por razón social.", e);
		}
	}

	@Override
	public List<Proveedor> findByFormaJuridica(String formaJuridica) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Proveedor p WHERE p.formaJuridica = :formaJuridica";
			Query<Proveedor> query = session.createQuery(hql, Proveedor.class);
			query.setParameter("formaJuridica", formaJuridica);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar proveedores por forma jurídica.", e);
		}
	}

	@Override
	public List<Proveedor> findByLocalidad(String localidad) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Proveedor p WHERE p.localidad LIKE :localidad";
			Query<Proveedor> query = session.createQuery(hql, Proveedor.class);
			query.setParameter("localidad", "%" + localidad + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar proveedores por localidad.", e);
		}
	}

	@Override
	public List<Proveedor> findByFechaAlta(LocalDate fechaInicio, LocalDate fechaFin) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Proveedor p WHERE p.fechaAlta BETWEEN :inicio AND :fin";
			Query<Proveedor> query = session.createQuery(hql, Proveedor.class);
			Date inicio = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date fin = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
			query.setParameter("inicio", inicio);
			query.setParameter("fin", fin);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar proveedores por fecha de alta.", e);
		}
	}

	@Override
	public boolean existsCifNif(String cifNif) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT count(p) FROM Proveedor p WHERE p.cifNif = :cifNif";
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("cifNif", cifNif);
			return query.uniqueResult() > 0;
		} catch (Exception e) {
			throw new DAOException("Error al comprobar existencia de CIF/NIF.", e);
		}
	}

	@Override
	public boolean existsEmail(String email) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT count(p) FROM Proveedor p WHERE p.email = :email";
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("email", email);
			return query.uniqueResult() > 0;
		} catch (Exception e) {
			throw new DAOException("Error al comprobar existencia de email.", e);
		}
	}

	@Override
	public List<Proveedor> findByTelefono(String telefono) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Proveedor p WHERE p.telefono = :telefono";
			Query<Proveedor> query = session.createQuery(hql, Proveedor.class);
			query.setParameter("telefono", telefono);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar proveedores por teléfono.", e);
		}
	}
}
