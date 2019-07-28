package hu.hordosikrisztian.lrs.main;

import java.net.HttpURLConnection;
import java.util.List;

import org.hibernate.cfg.Configuration;

import hu.hordosikrisztian.lrs.dao.ListingDao;
import hu.hordosikrisztian.lrs.entity.AbstractEntity;
import hu.hordosikrisztian.lrs.entity.Listing;
import hu.hordosikrisztian.lrs.entity.ListingStatus;
import hu.hordosikrisztian.lrs.entity.Location;
import hu.hordosikrisztian.lrs.entity.Marketplace;
import hu.hordosikrisztian.lrs.restendpoints.RequestMethodType;
import hu.hordosikrisztian.lrs.restendpoints.RestApiEndpoints;
import hu.hordosikrisztian.lrs.util.FtpUtils;
import hu.hordosikrisztian.lrs.util.HibernateUtils;
import hu.hordosikrisztian.lrs.util.JsonParsingUtils;
import hu.hordosikrisztian.lrs.util.ReportUtils;
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
	private static final String REPORT_JSON_FILE = "listingReport.json";

	public static void main(String[] args) {		
		processAndSaveDataForAllEntityClasses(REST_API_ENDPOINTS, RequestMethodType.GET, ENTITY_CLASSES);
		
		ReportUtils.createJsonReport();
		
		FtpUtils.connectAndUpload(REPORT_JSON_FILE, REPORT_JSON_FILE);
		
		System.out.println(ListingDao.getMonthlyResult("totalListingCountPerMonthForMarketplace", ListingDao.MarketplaceName.EBAY));
		System.out.println(ListingDao.getMonthlyResult("totalListingPricePerMonthForMarketplace", ListingDao.MarketplaceName.EBAY));
		System.out.println(ListingDao.getMonthlyResult("averageListingPricePerMonthForMarketplace", ListingDao.MarketplaceName.EBAY));
		
		System.out.println(ListingDao.getMonthlyResult("totalListingCountPerMonthForMarketplace", ListingDao.MarketplaceName.AMAZON));
		System.out.println(ListingDao.getMonthlyResult("totalListingPricePerMonthForMarketplace", ListingDao.MarketplaceName.AMAZON));
		System.out.println(ListingDao.getMonthlyResult("averageListingPricePerMonthForMarketplace", ListingDao.MarketplaceName.AMAZON));
		
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
