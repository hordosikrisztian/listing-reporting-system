package hu.hordosikrisztian.lrs.main;

import java.net.HttpURLConnection;
import java.util.List;
import org.hibernate.cfg.Configuration;
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
	
	private static final String HIBERNATE_PROPERTIES_FILE = "hibernate.properties";

	public static void main(String[] args) {
		processAndSaveData(RestApiEndpoints.LOCATION, RequestMethodType.GET, Location.class);
		processAndSaveData(RestApiEndpoints.LISTING_STATUS, RequestMethodType.GET, ListingStatus.class);
		processAndSaveData(RestApiEndpoints.MARKETPLACE, RequestMethodType.GET, Marketplace.class);
		processAndSaveData(RestApiEndpoints.LISTING, RequestMethodType.GET, Listing.class);
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

}
