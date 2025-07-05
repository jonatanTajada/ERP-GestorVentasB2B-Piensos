package com.gestorventasapp.dao;

import com.gestorventasapp.model.Producto;
import com.gestorventasapp.enums.Estado;
import com.gestorventasapp.util.HibernateUtil;
import com.gestorventasapp.exceptions.DAOException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

	@Override
	public void save(Producto producto) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist(producto);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al guardar el producto.", e);
		}
	}

	@Override
	public void update(Producto producto) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge(producto);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al actualizar el producto.", e);
		}
	}

	@Override
	public void delete(int idProducto) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			Producto producto = session.find(Producto.class, idProducto);
			if (producto != null) {
				producto.setEstado(Estado.inactivo);
				session.merge(producto);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			throw new DAOException("Error al dar de baja lógica el producto.", e);
		}
	}

	@Override
	public Producto findById(int idProducto) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.find(Producto.class, idProducto);
		} catch (Exception e) {
			throw new DAOException("Error al buscar el producto por ID.", e);
		}
	}

	@Override
	public List<Producto> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM Producto", Producto.class).getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar todos los productos.", e);
		}
	}

	@Override
	public List<Producto> findAllActivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.estado = :estado";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar productos activos.", e);
		}
	}

	@Override
	public List<Producto> findAllInactivos() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.estado = :estado";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("estado", Estado.inactivo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar productos inactivos.", e);
		}
	}

	@Override
	public List<Producto> findByEstado(Estado estado) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.estado = :estado";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("estado", estado);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al listar productos por estado.", e);
		}
	}

	@Override
	public List<Producto> findByNombre(String nombre) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.nombre LIKE :nombre";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("nombre", "%" + nombre + "%");
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar productos por nombre.", e);
		}
	}

	@Override
	public List<Producto> findByTipoAnimal(String tipoAnimal) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.tipoAnimal = :tipoAnimal";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("tipoAnimal", tipoAnimal);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar productos por tipo de animal.", e);
		}
	}

	@Override
	public List<Producto> findByMarca(String marca) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.marca = :marca";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("marca", marca);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar productos por marca.", e);
		}
	}

	@Override
	public List<Producto> findByProveedor(int idProveedor) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.proveedor.idProveedor = :idProveedor";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("idProveedor", idProveedor);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar productos por proveedor.", e);
		}
	}

	@Override
	public List<Producto> findByFormato(String formato) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.formato = :formato";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("formato", formato);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar productos por formato.", e);
		}
	}

	@Override
	public List<Producto> findByStockMinimo() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.stock <= p.stockMinimo AND p.estado = :estado";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar productos con stock mínimo.", e);
		}
	}

	@Override
	public List<Producto> findByStockMenorQue(int cantidad) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.stock < :cantidad AND p.estado = :estado";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("cantidad", cantidad);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar productos con stock menor que " + cantidad + ".", e);
		}
	}

	@Override
	public List<Producto> findByStockMayorQue(int cantidad) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.stock > :cantidad AND p.estado = :estado";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("cantidad", cantidad);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar productos con stock mayor que " + cantidad + ".", e);
		}
	}

	@Override
	public boolean existsNombre(String nombre) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT count(p) FROM Producto p WHERE p.nombre = :nombre";
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("nombre", nombre);
			return query.uniqueResult() > 0;
		} catch (Exception e) {
			throw new DAOException("Error al comprobar existencia de producto por nombre.", e);
		}
	}

	@Override
	public boolean existsByMarcaAndFormatoAndProveedor(String marca, String formato, int idProveedor) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "SELECT count(p) FROM Producto p WHERE p.marca = :marca AND p.formato = :formato AND p.proveedor.idProveedor = :idProveedor";
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("marca", marca);
			query.setParameter("formato", formato);
			query.setParameter("idProveedor", idProveedor);
			return query.uniqueResult() > 0;
		} catch (Exception e) {
			throw new DAOException("Error al comprobar existencia de producto por combinación.", e);
		}
	}

	@Override
	public List<Producto> findByPrecioVentaBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.precioVenta BETWEEN :min AND :max AND p.estado = :estado";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar productos por rango de precio de venta.", e);
		}
	}

	@Override
	public List<Producto> findByPrecioCompraBetween(double min, double max) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM Producto p WHERE p.precioCompra BETWEEN :min AND :max AND p.estado = :estado";
			Query<Producto> query = session.createQuery(hql, Producto.class);
			query.setParameter("min", min);
			query.setParameter("max", max);
			query.setParameter("estado", Estado.activo);
			return query.getResultList();
		} catch (Exception e) {
			throw new DAOException("Error al buscar productos por rango de precio de compra.", e);
		}
	}
}
