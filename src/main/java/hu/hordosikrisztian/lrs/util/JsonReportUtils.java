package hu.hordosikrisztian.lrs.util;

import static hu.hordosikrisztian.lrs.dao.ListingDao.MarketplaceName.AMAZON;
import static hu.hordosikrisztian.lrs.dao.ListingDao.MarketplaceName.EBAY;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import hu.hordosikrisztian.lrs.dao.ListingDao;
import hu.hordosikrisztian.lrs.exception.JsonReportCreationException;

public class JsonReportUtils {
	
	private static Long totalListingCount;
	
	private static Long totalListingCountForEbay;
	private static BigDecimal totalListingPriceForEbay;
	private static Double averageListingPriceForEbay;
	
	private static Long totalListingCountForAmazon;
	private static BigDecimal totalListingPriceForAmazon;
	private static Double averageListingPriceForAmazon;
	
	private static Map<String, String> bestListerEmailAddress;
	
	private static Map<String, String> totalListingCountPerMonthForEbay;
	private static Map<String, String> totalListingPricePerMonthForEbay;
	private static Map<String, String> averageListingPricePerMonthForEbay;
	
	private static Map<String, String> totalListingCountPerMonthForAmazon;
	private static Map<String, String> totalListingPricePerMonthForAmazon;
	private static Map<String, String> averageListingPricePerMonthForAmazon;
	
	private static Map<String, String> bestListerEmailAddressMonthly;
	
	static {
		// Queries providing the data through the DAO from the database.
		totalListingCount = ListingDao.getTotalListingCount("totalListingCount");
		
		totalListingCountForEbay = ListingDao.getTotalListingCountForMarketplace("totalListingCountForMarketplace", EBAY);
		totalListingPriceForEbay = ListingDao.getTotalListingPriceForMarketplace("totalListingPriceForMarketplace", EBAY);
		averageListingPriceForEbay = ListingDao.getAverageListingPriceForMarketplace("averageListingPriceForMarketplace", EBAY);
		
		totalListingCountForAmazon = ListingDao.getTotalListingCountForMarketplace("totalListingCountForMarketplace", AMAZON);
		totalListingPriceForAmazon = ListingDao.getTotalListingPriceForMarketplace("totalListingPriceForMarketplace", AMAZON);
		averageListingPriceForAmazon = ListingDao.getAverageListingPriceForMarketplace("averageListingPriceForMarketplace", AMAZON);
		
		bestListerEmailAddress = ListingDao.getBestListerEmailAddress(false);
		
		totalListingCountPerMonthForEbay = ListingDao.getMonthlyResult("totalListingCountPerMonthForMarketplace", EBAY);
		totalListingPricePerMonthForEbay = ListingDao.getMonthlyResult("totalListingPricePerMonthForMarketplace", EBAY);
		averageListingPricePerMonthForEbay = ListingDao.getMonthlyResult("averageListingPricePerMonthForMarketplace", EBAY);
		
		totalListingCountPerMonthForAmazon = ListingDao.getMonthlyResult("totalListingCountPerMonthForMarketplace", AMAZON);
		totalListingPricePerMonthForAmazon = ListingDao.getMonthlyResult("totalListingPricePerMonthForMarketplace", AMAZON);
		averageListingPricePerMonthForAmazon = ListingDao.getMonthlyResult("averageListingPricePerMonthForMarketplace", AMAZON);
		
		bestListerEmailAddressMonthly = ListingDao.getBestListerEmailAddress(true);
	}
	
	public static void createJsonReport() {		
		// Generate and write out JSON.
		try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File("listingReport.json")))) {
			Map<String, Boolean> prettyPrintingProperty = Map.of(JsonGenerator.PRETTY_PRINTING, true);
			
			JsonGeneratorFactory generatorFactory = Json.createGeneratorFactory(prettyPrintingProperty);
			JsonGenerator generator = generatorFactory.createGenerator(out);
			
			generateJsonBasedOnQueryResults(generator);
		} catch (IOException e) {
			throw new JsonReportCreationException("An exception occurred during creating listingReport.json: ", e);
		}
	}
	
	private static void generateJsonBasedOnQueryResults(JsonGenerator generator) {
		// Generate the actual JSON string which will constitute the report contents.
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
			
			.writeStartObject()
				.writeStartArray("monthlyReports")
					.writeStartObject()
						.writeStartObject("totalListingCountPerMonthForEbay")
							.write("monthOfYear", totalListingCountPerMonthForEbay.keySet().stream().collect(Collectors.joining(", ")))
							.write("totalListingCount", totalListingCountPerMonthForEbay.values().stream().collect(Collectors.joining(", ")))
						.writeEnd()
					.writeEnd()
					
					.writeStartObject()
						.writeStartObject("totalListingPricePerMonthForEbay")
							.write("monthOfYear", totalListingPricePerMonthForEbay.keySet().stream().collect(Collectors.joining(", ")))
							.write("totalListingPrice", totalListingPricePerMonthForEbay.values().stream().collect(Collectors.joining(", ")))
						.writeEnd()
					.writeEnd()
					
					.writeStartObject()
						.writeStartObject("averageListingPricePerMonthForEbay")
							.write("monthOfYear", averageListingPricePerMonthForEbay.keySet().stream().collect(Collectors.joining(", ")))
							.write("averageListingPrice", averageListingPricePerMonthForEbay.values().stream().collect(Collectors.joining(", ")))
						.writeEnd()
					.writeEnd()
					
					.writeStartObject()
						.writeStartObject("totalListingCountPerMonthForAmazon")
							.write("monthOfYear", totalListingCountPerMonthForAmazon.keySet().stream().collect(Collectors.joining(", ")))
							.write("totalListingCount", totalListingCountPerMonthForAmazon.values().stream().collect(Collectors.joining(", ")))
						.writeEnd()
					.writeEnd()
					
					.writeStartObject()
						.writeStartObject("totalListingPricePerMonthForAmazon")
							.write("monthOfYear", totalListingPricePerMonthForAmazon.keySet().stream().collect(Collectors.joining(", ")))
							.write("totalListingPrice", totalListingPricePerMonthForAmazon.values().stream().collect(Collectors.joining(", ")))
						.writeEnd()
					.writeEnd()
					
					.writeStartObject()
						.writeStartObject("averageListingPricePerMonthForAmazon")
							.write("monthOfYear", averageListingPricePerMonthForAmazon.keySet().stream().collect(Collectors.joining(", ")))
							.write("averageListingPrice", averageListingPricePerMonthForAmazon.values().stream().collect(Collectors.joining(", ")))
						.writeEnd()
					.writeEnd()
					
					.writeStartObject()
						.writeStartObject("bestListerEmailAddressMonthly")
							.write("monthOfYear", bestListerEmailAddressMonthly.keySet().stream().collect(Collectors.joining(", ")))
							.write("bestListerEmailAddress", bestListerEmailAddressMonthly.values().stream().collect(Collectors.joining(", ")))
						.writeEnd()
					.writeEnd()						
				.writeEnd()
			.writeEnd()
		.writeEnd().close();
	}

}
