package hu.hordosikrisztian.lrs.entity;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "list_rep", name = "listing_statuses")
public class ListingStatus extends AbstractEntity {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "status_name")
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

}
