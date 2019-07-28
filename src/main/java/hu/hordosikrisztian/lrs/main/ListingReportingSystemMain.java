package hu.hordosikrisztian.lrs.main;

import static hu.hordosikrisztian.lrs.dao.ListingDao.MarketplaceName.AMAZON;
import static hu.hordosikrisztian.lrs.dao.ListingDao.MarketplaceName.EBAY;

import java.net.HttpURLConnection;
import java.util.List;

import org.hibernate.cfg.Configuration;

import hu.hordosikrisztian.lrs.dao.ListingDao;
import hu.hordosikrisztian.lrs.entity.AbstractEntity;
import hu.hordosikrisztian.lrs.entity.Listing;
import hu.hordosikrisztian.lrs.entity.ListingStatus;
import hu.hordosikrisztian.lrs.entity.Location;
import hu.hordosikrisztian.lrs.entity.Marketplace;
import hu.hordosikrisztian.lrs.rest.endpoints.RequestMethodType;
import hu.hordosikrisztian.lrs.rest.endpoints.RestApiEndpoints;
import hu.hordosikrisztian.lrs.util.HibernateUtils;
import hu.hordosikrisztian.lrs.util.JsonParsingUtils;
import hu.hordosikrisztian.lrs.util.RestApiConnectionUtils;

public class ListingReportingSystemMain {
	
	private static final RestApiEndpoints[] REST_API_ENDPOINTS = {
		RestApiEndpoints.LOCATION,
		RestApiEndpoints.LISTING_STATUS,
		RestApiEndpoints.MARKETPLACE,
		RestApiEndpoints.LISTING
	};
	
	private static final List<Class<? extends AbstractEntity>> ENTITY_CLASSES = List.of(Location.class, ListingStatus.class, Marketplace.class, Listing.class);
	
	private static final String HIBERNATE_PROPERTIES_FILE = "hibernate.properties";

	public static void main(String[] args) {		
		processAndSaveDataForAllEntityClasses(REST_API_ENDPOINTS, RequestMethodType.GET, ENTITY_CLASSES);
		
		System.out.println(ListingDao.getTotalListingCount("totalListingCount"));
		
		System.out.println(ListingDao.getTotalListingCountForMarketplace("totalListingCountForMarketplace", EBAY));
		System.out.println(ListingDao.getTotalListingPriceForMarketplace("totalListingPriceForMarketplace", EBAY));
		System.out.println(ListingDao.getAverageListingPriceForMarketplace("averageListingPriceForMarketplace", EBAY));
		
		System.out.println(ListingDao.getTotalListingCountForMarketplace("totalListingCountForMarketplace", AMAZON));
		System.out.println(ListingDao.getTotalListingPriceForMarketplace("totalListingPriceForMarketplace", AMAZON));
		System.out.println(ListingDao.getAverageListingPriceForMarketplace("averageListingPriceForMarketplace", AMAZON));
		System.out.println(ListingDao.getBestListerEmailAddress(false));
		
		System.out.println(ListingDao.getMonthlyResult("totalListingCountPerMonthForMarketplace", EBAY));
		System.out.println(ListingDao.getMonthlyResult("totalListingPricePerMonthForMarketplace", EBAY));
		System.out.println(ListingDao.getMonthlyResult("averageListingPricePerMonthForMarketplace", EBAY));
		
		System.out.println(ListingDao.getMonthlyResult("totalListingCountPerMonthForMarketplace", AMAZON));
		System.out.println(ListingDao.getMonthlyResult("totalListingPricePerMonthForMarketplace", AMAZON));
		System.out.println(ListingDao.getMonthlyResult("averageListingPricePerMonthForMarketplace", AMAZON));
		
		System.out.println(ListingDao.getBestListerEmailAddress(true));
	}

	private static void processAndSaveData(RestApiEndpoints restApiEndpoint, RequestMethodType requestMethodType, Class<? extends AbstractEntity> entityClass) {
		HttpURLConnection conn = RestApiConnectionUtils.connectToUrl(restApiEndpoint, requestMethodType);

		List<? extends AbstractEntity> entityList = JsonParsingUtils.storeInputJsonDataInEntityList(conn, entityClass);
		
		Configuration hibernateConf = null;
		
		if (entityClass != Listing.class) {
			hibernateConf = HibernateUtils.storeHibernateConfiguration(HIBERNATE_PROPERTIES_FILE, entityClass);
		} else {
			hibernateConf = HibernateUtils.storeHibernateConfiguration(HIBERNATE_PROPERTIES_FILE, Location.class, ListingStatus.class, Marketplace.class, Listing.class);
		}

		HibernateUtils.validateAndSaveToDatabase(hibernateConf, entityList);
	}
	
	private static void processAndSaveDataForAllEntityClasses(RestApiEndpoints[] restApiEndpoints, RequestMethodType requestMethodType, List<Class<? extends AbstractEntity>> entityClasses) {
		for (int i = 0; i < restApiEndpoints.length; i++) {
			processAndSaveData(restApiEndpoints[i], requestMethodType, entityClasses.get(i));
		}
	}

}
