package hu.hordosikrisztian.lrs.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

@Entity
@Table(schema = "list_rep", name = "listings")

@NamedQueries({
	@NamedQuery(name = "totalListingCount",
				query = "SELECT COUNT(l) "
					  + "FROM Listing l"),
	
	@NamedQuery(name = "totalListingCountForMarketplace",
				query = "SELECT COUNT(l) "
					  + "FROM Listing l "
					  + "INNER JOIN Marketplace m ON l.marketplaceId = m.id "
					  + "WHERE m.marketplaceName = :marketplaceName"),
	
	@NamedQuery(name = "totalListingPriceForMarketplace",
				query = "SELECT SUM(l.listingPrice) "
					  + "FROM Listing l "
					  + "INNER JOIN Marketplace m ON l.marketplaceId = m.id "
					  + "WHERE m.marketplaceName = :marketplaceName"),
	
	@NamedQuery(name = "averageListingPriceForMarketplace",
				query = "SELECT ROUND(AVG(l.listingPrice), 2) "
					  + "FROM Listing l "
					  + "INNER JOIN Marketplace m ON l.marketplaceId = m.id "
					  + "WHERE m.marketplaceName = :marketplaceName")
})

@NamedNativeQueries({
	@NamedNativeQuery(name = "bestListerEmailAddress",
					  query = "SELECT l.owner_email_address AS best_lister_email_address, COUNT(*) AS listings_count "
						    + "FROM list_rep.listings AS l "
						    + "GROUP BY best_lister_email_address "
						    + "ORDER BY listings_count DESC "
						    + "LIMIT 1"),
	
	@NamedNativeQuery(name = "totalListingCountPerMonthForMarketplace",
					  query = "SELECT "
					  			+ "TO_CHAR(l.upload_time, 'MM/YYYY') AS month_of_year, "
					  			+ "COUNT(*) AS total_ebay_listing_count "
					  		+ "FROM list_rep.listings AS l "
					  		+ "INNER JOIN list_rep.marketplaces AS m "
					  		+ "ON l.marketplace_id = m.id "
					  		+ "WHERE m.marketplace_name = ?1 "
					  		+ "GROUP BY month_of_year"),
	
	@NamedNativeQuery(name = "totalListingPricePerMonthForMarketplace",
					  query = "SELECT "
					  			+ "TO_CHAR(l.upload_time, 'MM/YYYY') AS month_of_year, "
					  			+ "SUM(l.listing_price) AS total_ebay_listing_price "
					  		+ "FROM list_rep.listings AS l "
					  		+ "INNER JOIN list_rep.marketplaces AS m "
					  			+ "ON l.marketplace_id = m.id "
					  		+ "WHERE m.marketplace_name = ?1 "
					  		+ "GROUP BY month_of_year"),
	
	@NamedNativeQuery(name = "averageListingPricePerMonthForMarketplace",
					  query = "SELECT "
					  			+ "TO_CHAR(l.upload_time, 'MM/YYYY') AS month_of_year, "
					  			+ "ROUND(AVG(l.listing_price), 2) AS average_ebay_listing_price "
					  		+ "FROM list_rep.listings AS l "
					  		+ "INNER JOIN list_rep.marketplaces AS m "
					  			+ "ON l.marketplace_id = m.id "
					  		+ "WHERE m.marketplace_name = ?1 "
					  		+ "GROUP BY month_of_year"),
	
	@NamedNativeQuery(name = "bestListerEmailAddressOfTheMonth",
					  query = "SELECT "
					  			+ "DISTINCT ON (month_of_year) subquery.month_of_year AS month_of_year, "
					  			+ "subquery.owner_email_address AS best_lister_email_address "
					  		+ "FROM (SELECT "
					  				  + "TO_CHAR(l.upload_time, 'MM/YYYY') AS month_of_year, "
					  				  + "l.owner_email_address AS owner_email_address, "
					  				  + "COUNT(*) AS listings_count "
					  		+ "FROM list_rep.listings AS l "
					  		+ "GROUP BY month_of_year, owner_email_address "
					  		+ "ORDER BY month_of_year ASC, listings_count DESC) AS subquery")
})

public class Listing extends AbstractEntity {

	@Id
	@Column(name = "id")
	@NotNull(message = "Listing ID required.")
	@Type(type = "pg-uuid")
	private UUID id;

	@Column(name = "title")
	@NotNull(message = "Title required.")
	private String title;

	@Column(name = "description")
	@NotNull(message = "Description required.")
	private String description;
	
	@OneToOne
	@JoinColumn(name = "location_id", insertable = false, updatable = false)
	@JsonbTransient
	private Location location;

	@Column(name = "location_id")
	@NotNull(message = "Location ID required.")
	@Type(type = "pg-uuid")
	@JsonbProperty("location_id")
	private UUID locationId;
	
	@Column(name = "listing_price")
	@NotNull(message = "Listing price required.")
	@Positive(message = "Listing price must be greater than 0.")
	@Digits(integer = 1000, fraction = 2)
	@JsonbProperty("listing_price")
	private BigDecimal listingPrice;

	@Column(name = "currency")
	@NotNull(message = "Currency required.")
	@Size(min = 3, max = 3, message = "Currency codes must be 3 characters long.")
	private String currency;

	@Column(name = "quantity")
	@NotNull(message = "Quantity required")
	@Positive(message = "Quantity must be greater than 0.")
	private int quantity;
	
	@OneToOne
	@JoinColumn(name = "listing_status_id", insertable = false, updatable = false)
	@JsonbTransient
	private ListingStatus listingStatus;
	
	@Column(name = "listing_status_id")
	@NotNull(message = "Listing status ID required.")
	@Min(value = 1, message = "Invalid listing status ID.")
	@Max(value = 3, message = "Invalid listing status ID.")
	@JsonbProperty("listing_status")
	private int listingStatusId;
	
	@OneToOne
	@JoinColumn(name = "marketplace_id", insertable = false, updatable = false)
	@JsonbTransient
	private Marketplace marketplace;
	
	@Column(name = "marketplace_id")
	@NotNull(message = "Marketplace ID required.")
	@JsonbProperty("marketplace")
	private int marketplaceId;

	@Column(name = "upload_time")
	@NotNull(message = "Upload time required.")
	@JsonbProperty("upload_time")
	@JsonbDateFormat("M/d/yyyy")
	private LocalDate uploadTime;

	@Column(name = "owner_email_address")
	@NotNull(message = "Owner e-mail address required.")
	@Email(message = "Invalid e-mail address.")
	@JsonbProperty("owner_email_address")
	private String ownerEmailAddress;

	public Listing() {

	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public UUID getLocationId() {
		return locationId;
	}

	public void setLocationId(UUID locationId) {
		this.locationId = locationId;
	}

	public BigDecimal getListingPrice() {
		return listingPrice;
	}

	public void setListingPrice(BigDecimal listingPrice) {
		this.listingPrice = listingPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public ListingStatus getListingStatus() {
		return listingStatus;
	}

	public void setListingStatus(ListingStatus listingStatus) {
		this.listingStatus = listingStatus;
	}
	
	public int getListingStatusId() {
		return listingStatusId;
	}

	public void setListingStatusId(int listingStatusId) {
		this.listingStatusId = listingStatusId;
	}
	
	public Marketplace getMarketplace() {
		return marketplace;
	}

	public void setMarketplace(Marketplace marketplace) {
		this.marketplace = marketplace;
	}
	
	public int getMarketplaceId() {
		return marketplaceId;
	}

	public void setMarketplaceId(int marketplaceId) {
		this.marketplaceId = marketplaceId;
	}

	public LocalDate getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(LocalDate uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getOwnerEmailAddress() {
		return ownerEmailAddress;
	}

	public void setOwnerEmailAddress(String ownerEmailAddress) {
		this.ownerEmailAddress = ownerEmailAddress;
	}

}
