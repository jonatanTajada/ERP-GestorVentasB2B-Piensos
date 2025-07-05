package com.gestorventasapp.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	static {
		
		try {
			// Cargar la configuracion de Hibernate desde hibernate.cfg.xml
			sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("Error al inicializar Hibernate: " + e);
		}
		
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}
}
