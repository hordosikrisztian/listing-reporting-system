package hu.hordosikrisztian.lrs.entity;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(schema = "list_rep", name = "listing_statuses")
public class ListingStatus extends AbstractEntity {

	@Id
	@NotNull(message = "Listing status ID required.")
	private int id;

	@Column(name = "status_name")
	@NotNull(message = "Status name required.")
	@JsonbProperty("status_name")
	private String statusName;

	public ListingStatus() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Override
	public String toString() {
		return "ListingStatus [id=" + id + ", statusName=" + statusName + "]";
	}

}
