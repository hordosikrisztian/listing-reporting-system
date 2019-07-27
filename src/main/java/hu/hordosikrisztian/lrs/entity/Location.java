package hu.hordosikrisztian.lrs.entity;

import java.util.UUID;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

@Entity
@Table(schema = "list_rep", name = "locations")
public class Location extends AbstractEntity {

	@Id
	@NotNull(message = "Location ID required.")
	@Type(type = "pg-uuid")
	private UUID id;

	@Column(name = "manager_name")
	@NotNull(message = "Manager name required.")
	@JsonbProperty("manager_name")
	private String managerName;

	@Column(name = "phone")
	@NotNull(message = "Phone number required.")
	private String phone;

	@Column(name = "address_primary")
	@NotNull(message = "Primary address required.")
	@JsonbProperty("address_primary")
	private String addressPrimary;

	@Column(name = "address_secondary", nullable = true)
	@JsonbProperty("address_secondary")
	private String addressSecondary;

	@Column(name = "country")
	@NotNull(message = "Country required.")
	private String country;

	@Column(name = "town")
	@NotNull(message = "Town required.")
	private String town;

	@Column(name = "postal_code")
	@NotNull(message = "Postal code required.")
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

	@Override
	public String toString() {
		return "Location [id=" + id + ", managerName=" + managerName + ", phone=" + phone + ", addressPrimary="
				+ addressPrimary + ", addressSecondary=" + addressSecondary + ", country=" + country + ", town=" + town
				+ ", postalCode=" + postalCode + "]";
	}

}
