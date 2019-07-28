package hu.hordosikrisztian.lrs.util;

import static hu.hordosikrisztian.lrs.dao.ListingDao.MarketplaceName.AMAZON;
import static hu.hordosikrisztian.lrs.dao.ListingDao.MarketplaceName.EBAY;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import hu.hordosikrisztian.lrs.dao.ListingDao;
import hu.hordosikrisztian.lrs.exception.JsonReportCreationException;

public class ReportUtils {
	
	public static void createJsonReport() {
		Long totalListingCount = ListingDao.getTotalListingCount("totalListingCount");
		
		Long totalListingCountForEbay = ListingDao.getTotalListingCountForMarketplace("totalListingCountForMarketplace", EBAY);
		BigDecimal totalListingPriceForEbay = ListingDao.getTotalListingPriceForMarketplace("totalListingPriceForMarketplace", EBAY);
		Double averageListingPriceForEbay = ListingDao.getAverageListingPriceForMarketplace("averageListingPriceForMarketplace", EBAY);
		
		Long totalListingCountForAmazon = ListingDao.getTotalListingCountForMarketplace("totalListingCountForMarketplace", AMAZON);
		BigDecimal totalListingPriceForAmazon = ListingDao.getTotalListingPriceForMarketplace("totalListingPriceForMarketplace", AMAZON);
		Double averageListingPriceForAmazon = ListingDao.getAverageListingPriceForMarketplace("averageListingPriceForMarketplace", AMAZON);
		
		Map<String, String> bestListerEmailAddress = ListingDao.getBestListerEmailAddress(false);
		
		try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File("listingReport.json")))) {
			Map<String, Boolean> prettyPrintingProperty = Map.of(JsonGenerator.PRETTY_PRINTING, true);
			
			JsonGeneratorFactory generatorFactory = Json.createGeneratorFactory(prettyPrintingProperty);
			JsonGenerator generator = generatorFactory.createGenerator(out);
			
			generator
			.writeStartArray()
				.writeStartObject()
					.write("totalListingCount", totalListingCount)
				.writeEnd()
					 	
				.writeStartObject()
					.write("totalListingCountForEbay", totalListingCountForEbay)
					.write("totalListingPriceForEbay", totalListingPriceForEbay)
					.write("averageListingPriceForEbay", averageListingPriceForEbay)
				.writeEnd()
				
				.writeStartObject()
					.write("totalListingCountForAmazon", totalListingCountForAmazon)
					.write("totalListingPriceForAmazon", totalListingPriceForAmazon)
					.write("averageListingPriceForAmazon", averageListingPriceForAmazon)
				.writeEnd()
				
				.writeStartObject()
					.write("bestListerEmailAddress", bestListerEmailAddress.entrySet().iterator().next().getKey())
					.write("listingCount", bestListerEmailAddress.entrySet().iterator().next().getValue())
				.writeEnd()
			.writeEnd().close();
			
			// TODO Figure out how to loop through monthly Maps, and include them in the JSON report.
		} catch (IOException e) {
			throw new JsonReportCreationException("An exception occurred during creating listingReport.json: ", e);
		}
	}

}
