package hu.hordosikrisztian.lrs.entity;

import java.util.UUID;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(schema = "list_rep", name = "locations")
public class Location extends AbstractEntity {

	@Id
	@Column(name = "id")
	@Type(type = "pg-uuid")
	private UUID id;

	@Column(name = "manager_name")
	@JsonbProperty("manager_name")
	private String managerName;

	@Column(name = "phone")
	private String phone;

	@Column(name = "address_primary")
	@JsonbProperty("address_primary")
	private String addressPrimary;

	@Column(name = "address_secondary")
	@JsonbProperty("address_secondary")
	private String addressSecondary;

	@Column(name = "country")
	private String country;

	@Column(name = "town")
	private String town;

	@Column(name = "postal_code")
	@JsonbProperty("postal_code")
	private String postalCode;

	public Location() {

	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddressPrimary() {
		return addressPrimary;
	}

	public void setAddressPrimary(String addressPrimary) {
		this.addressPrimary = addressPrimary;
	}

	public String getAddressSecondary() {
		return addressSecondary;
	}

	public void setAddressSecondary(String addressSecondary) {
		this.addressSecondary = addressSecondary;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

}
