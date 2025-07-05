package com.gestorventasapp.dao;

import com.gestorventasapp.model.Cliente;
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

public class ClienteDAOImpl implements ClienteDAO {

	@Override
	public void save(Cliente cliente) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(cliente);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar el cliente.", e);
		}
	}

	@Override
	public void update(Cliente cliente) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(cliente);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar el cliente.", e);
		}
	}

	@Override
	public void delete(int idCliente) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Cliente cliente = session.find(Cliente.class, idCliente);
			if (cliente != null) {
				cliente.setEstado(Estado.inactivo);
				session.merge(cliente);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica el cliente.", e);
		}
	}

	@Override
	public Cliente findById(int idCliente) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(Cliente.class, idCliente);
		} catch (Exception e) {
			throw new DAOException("Error al buscar el cliente por ID.", e);
		}
	}

	@Override
	public Cliente findByCifNif(String cifNif) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Cliente c WHERE c.cifNif = :cifNif";
			Query<Cliente> query = session.createQuery(hql, Cliente.class);
			query.setParameter("cifNif", cifNif);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar el cliente por CIF/NIF.", e);
		}
	}

	@Override
	public Cliente findByEmail(String email) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Cliente c WHERE c.email = :email";
			Query<Cliente> query = session.createQuery(hql, Cliente.class);
			query.setParameter("email", email);
			return query.uniqueResult();
		} catch (Exception e) {
			throw new DAOException("Error al buscar el cliente por email.", e);
		}
	}

	@Override
	public List<Cliente> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM Cliente", Cliente.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar todos los clientes.", e);
		}
	}

	@Override
	public List<Cliente> findAllActivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Cliente c WHERE c.estado = :estado";
			Query<Cliente> query = session.createQuery(hql, Cliente.class);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar clientes activos.", e);
		}
	}

	@Override
	public List<Cliente> findAllInactivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Cliente c WHERE c.estado = :estado";
			Query<Cliente> query = session.createQuery(hql, Cliente.class);
			query.setParameter("estado", Estado.inactivo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar clientes inactivos.", e);
		}
	}

	@Override
	public List<Cliente> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Cliente c WHERE c.estado = :estado";
			Query<Cliente> query = session.createQuery(hql, Cliente.class);
			query.setParameter("estado", estado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar clientes por estado.", e);
		}
	}

	@Override
	public List<Cliente> findByRazonSocial(String razonSocial) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Cliente c WHERE c.razonSocial LIKE :razonSocial";
			Query<Cliente> query = session.createQuery(hql, Cliente.class);
			query.setParameter("razonSocial", "%" + razonSocial + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar clientes por razón social.", e);
		}
	}

	@Override
	public List<Cliente> findByFormaJuridica(String formaJuridica) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Cliente c WHERE c.formaJuridica = :formaJuridica";
			Query<Cliente> query = session.createQuery(hql, Cliente.class);
			query.setParameter("formaJuridica", formaJuridica);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar clientes por forma jurídica.", e);
		}
	}

	@Override
	public List<Cliente> findByLocalidad(String localidad) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Cliente c WHERE c.localidad LIKE :localidad";
			Query<Cliente> query = session.createQuery(hql, Cliente.class);
			query.setParameter("localidad", "%" + localidad + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar clientes por localidad.", e);
		}
	}

	@Override
	public List<Cliente> findByFechaAlta(LocalDate fechaInicio, LocalDate fechaFin) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Cliente c WHERE c.fechaAlta BETWEEN :inicio AND :fin";
			Query<Cliente> query = session.createQuery(hql, Cliente.class);
			Date inicio = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date fin = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
			query.setParameter("inicio", inicio);
			query.setParameter("fin", fin);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar clientes por fecha de alta.", e);
		}
	}

	@Override
	public boolean existsCifNif(String cifNif) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT count(c) FROM Cliente c WHERE c.cifNif = :cifNif";
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
			String hql = "SELECT count(c) FROM Cliente c WHERE c.email = :email";
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("email", email);
			return query.uniqueResult() > 0;
		} catch (Exception e) {
			throw new DAOException("Error al comprobar existencia de email.", e);
		}
	}

	@Override
	public List<Cliente> findByTelefono(String telefono) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Cliente c WHERE c.telefono = :telefono";
			Query<Cliente> query = session.createQuery(hql, Cliente.class);
			query.setParameter("telefono", telefono);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar clientes por teléfono.", e);
		}
	}
}
