package hu.hordosikrisztian.lrs.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import hu.hordosikrisztian.lrs.exception.HibernatePropertiesLoadingException;

public class HibernateUtils {

	public static Configuration storeHibernateConfiguration(String propertiesFileName, Class<?>... entityClasses) {
		Properties hibernateProperties = new Properties();

		try {
			hibernateProperties.load(ClassLoader.getSystemResourceAsStream(propertiesFileName));
		} catch (IOException e) {
			throw new HibernatePropertiesLoadingException("Error loading Hibernate properties file: " + propertiesFileName, e);
		}

		Configuration hibernateConf = new Configuration();

		hibernateConf.setProperties(hibernateProperties);
		
		for (Class<?> entityClass : entityClasses) {
			hibernateConf.addAnnotatedClass(entityClass);
		}

		return hibernateConf;
	}

	public static <T> void saveToDatabase(Configuration hibernateConf, List<T> entityList) {
		SessionFactory sessionFactory = hibernateConf.buildSessionFactory();
		Session session = sessionFactory.getCurrentSession();

		Transaction transaction = session.beginTransaction();

		for (T entityElement : entityList) {
			session.save(entityElement);
		}

		transaction.commit();

		session.close();
		sessionFactory.close();
	}

}
