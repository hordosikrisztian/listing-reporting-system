package hu.hordosikrisztian.lrs.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

@Entity
@Table(schema = "list_rep", name = "listings")
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

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE,
			  			  CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "location_id")
	@NotNull(message = "Location ID required.")
	private Location location;
	
	@Transient
	@JsonbProperty("location_id")
	private int locationId;
	
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

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE,
						  CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "listing_status_id")
	@NotNull(message = "Listing status ID required.")
	private ListingStatus listingStatus;

	@Transient
	@JsonbProperty("listing_status")
	private int listingStatusId;

	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE,
			  			  CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "marketplace_id")
	@NotNull(message = "Marketplace ID required.")
	private Marketplace marketplace;

	@Transient
	@JsonbProperty("marketplace")
	private int marketplaceId;

	@Column(name = "upload_time")
	@JsonbProperty("upload_time")
	@JsonbDateFormat("M/d/yyyy")
	private LocalDate uploadTime;

	@Column(name = "owner_email_address")
	@NotNull(message = "Owner e-mail address required.")
	@Email(regexp = "^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$",
		   message = "Invalid e-mail address.")
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

	public Marketplace getMarketplace() {
		return marketplace;
	}

	public void setMarketplace(Marketplace marketplace) {
		this.marketplace = marketplace;
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

	@Override
	public String toString() {
		return "Listing [id=" + id + ", title=" + title + ", description=" + description + ", location=" + location
				+ ", listingPrice=" + listingPrice + ", currency=" + currency + ", quantity=" + quantity
				+ ", listingStatus=" + listingStatus + ", marketplace=" + marketplace + ", uploadTime="
				+ uploadTime + ", ownerEmailAddress=" + ownerEmailAddress + "]";
	}

}
