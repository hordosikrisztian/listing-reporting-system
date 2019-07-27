package hu.hordosikrisztian.lrs.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import hu.hordosikrisztian.lrs.entity.Listing;
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

	public static <T> void validateAndSaveToDatabase(Configuration hibernateConf, List<T> entityList) {
		SessionFactory sessionFactory = hibernateConf.buildSessionFactory();
		Session session = sessionFactory.getCurrentSession();

		Transaction transaction = session.beginTransaction();
		
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		
		try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File("importLog.csv")))) {
			String header = "ListingId;MarketplaceName;InvalidField\n";
			out.write(header.getBytes());
			
			List<T> violators = new ArrayList<>();
			
			for (T entityListElement : entityList) {
				if (entityListElement instanceof Listing) {				
					Set<ConstraintViolation<T>> constraintViolations = validator.validate(entityListElement);
					
					if (!constraintViolations.isEmpty()) {
						for (ConstraintViolation<T> violation : constraintViolations) {
							String id = ((Listing) entityListElement).getId().toString();
							String marketplaceName = ((Listing) entityListElement).getMarketplaceId() == 1 ? "EBAY" : "AMAZON";
							
							String invalidField = violation.getMessage();
							String line = id + ";" + marketplaceName + ";" + invalidField + "\n";
							
							out.write(line.getBytes());
						}
						
						violators.add(entityListElement);
					}
				}
			}
			
			entityList.removeAll(violators);
			
			entityList.forEach(e -> session.saveOrUpdate(e));
		} catch (IOException e) {
			// TODO Add custom exception.
			e.printStackTrace();
		}

		transaction.commit();

		session.close();
		sessionFactory.close();
	}

}
