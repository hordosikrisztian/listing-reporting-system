package hu.hordosikrisztian.lrs.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import hu.hordosikrisztian.lrs.entity.Listing;
import hu.hordosikrisztian.lrs.entity.ListingStatus;
import hu.hordosikrisztian.lrs.entity.Location;
import hu.hordosikrisztian.lrs.entity.Marketplace;
import hu.hordosikrisztian.lrs.util.HibernateUtils;

public class ListingDao {
	
	private static final String HIBERNATE_PROPERTIES_FILE = "hibernate.properties";
	
	public static Long getTotalListingCount(String namedQuery) {
		Configuration hibernateConf = configureHibernate();
		Session session = getSession(hibernateConf);
		
		Transaction transaction = session.beginTransaction();
		
		Long count = session.createNamedQuery(namedQuery, Long.class)
						    .getSingleResult();
		
		transaction.commit();
		
		session.close();
		
		return count;
	}
	
	public static Long getTotalListingCountForMarketplace(String namedQuery, MarketplaceName marketplaceName) {
		Configuration hibernateConf = configureHibernate();
		Session session = getSession(hibernateConf);
		
		Transaction transaction = session.beginTransaction();
		
		Long count = session.createNamedQuery(namedQuery, Long.class)
							.setParameter("marketplaceName", marketplaceName.getName())
						    .getSingleResult();
		
		transaction.commit();
		
		session.close();
		
		return count;
	}
	
	public static BigDecimal getTotalListingPriceForMarketplace(String namedQuery, MarketplaceName marketplaceName) {
		Configuration hibernateConf = configureHibernate();
		Session session = getSession(hibernateConf);
		
		Transaction transaction = session.beginTransaction();
		
		BigDecimal totalListingPrice = session.createNamedQuery(namedQuery, BigDecimal.class)
											  .setParameter("marketplaceName", marketplaceName.getName())
											  .getSingleResult();
		
		transaction.commit();
		
		session.close();
		
		return totalListingPrice;
	}
	
	public static Double getAverageListingPriceForMarketplace(String namedQuery, MarketplaceName marketplaceName) {
		Configuration hibernateConf = configureHibernate();
		Session session = getSession(hibernateConf);
		
		Transaction transaction = session.beginTransaction();
		
		Double averageListingPrice = session.createNamedQuery(namedQuery, Double.class)
											.setParameter("marketplaceName", marketplaceName.getName())
											.getSingleResult();
		
		transaction.commit();
		
		session.close();
		
		return averageListingPrice;
	}
	
	public static Map<String, String> getBestListerEmailAddress(boolean monthly) {
		Configuration hibernateConf = configureHibernate();
		Session session = getSession(hibernateConf);
		
		Transaction transaction = session.beginTransaction();
		
		if (!monthly) {
			Object[] results = (Object[]) session.createNamedQuery("bestListerEmailAddress")
												 .getSingleResult();
			
			transaction.commit();
			
			session.close();
			
			return fillMapForNativeQuery(results);
		} else {
			@SuppressWarnings("unchecked")
			List<Object[]> results = session.createNamedQuery("bestListerEmailAddressOfTheMonth")
											.getResultList();
			transaction.commit();
			
			session.close();
			
			return fillMapForNativeQuery(results);
		}
	}
	
	///////////////////////
	// For monthly reports.
	///////////////////////
	
	public static Map<String, String> getMonthlyResult(String namedNativeQuery, MarketplaceName marketplaceName) {
		Configuration hibernateConf = configureHibernate();
		Session session = getSession(hibernateConf);
		
		Transaction transaction = session.beginTransaction();
		
		@SuppressWarnings("unchecked")
		List<Object[]> results = session.createNamedQuery(namedNativeQuery)
										.setParameter(1, marketplaceName.getName())
										.getResultList();
		
		transaction.commit();
		
		session.close();
		
		return fillMapForNativeQuery(results);
	}
	
	private static Configuration configureHibernate() {
		return HibernateUtils.storeHibernateConfiguration(HIBERNATE_PROPERTIES_FILE, Location.class, ListingStatus.class, Marketplace.class, Listing.class);
	}
	
	private static Session getSession(Configuration hibernateConf) {
		SessionFactory sessionFactory = hibernateConf.buildSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		
		return session;
	}
	
	private static Map<String, String> fillMapForNativeQuery(Object[] results) {
		Map<String, String> resultMap = new HashMap<>();
		StringBuilder resultBuilder = new StringBuilder();
		
		for (int i = 0; i < results.length; i++) {
			if (i != results.length - 1) {
				resultBuilder.append(results[i]).append(" ");
			} else {
				resultBuilder.append(results[i]);
			}
		}
		
		String[] keyAndValue = resultBuilder.toString().split(" ");
		
		resultMap.put(keyAndValue[0], keyAndValue[1]);
		
		return resultMap;
	}
	
	private static Map<String, String> fillMapForNativeQuery(List<Object[]> results) {
		Map<String, String> resultMap = new LinkedHashMap<>();
		StringBuilder resultBuilder = new StringBuilder();
		
		for (Object[] result : results) {
			for (Object object : result) {
				resultBuilder.append(object).append(" ");
			}
			
			resultBuilder.append("\n");
		}
		
		String[] keysAndValues = resultBuilder.toString().split("\n");
		
		for (String keyAndValue : keysAndValues) {
			resultMap.put(keyAndValue.substring(0, keyAndValue.indexOf(' ')), keyAndValue.substring(keyAndValue.indexOf(' ') + 1).trim());
		}
		
		return resultMap;
	}
	
	public enum MarketplaceName {
		
		EBAY("EBAY"),
		AMAZON("AMAZON");
		
		private String name;
		
		private MarketplaceName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
	}

}
