package hu.hordosikrisztian.lrs.entity;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "list_rep", name = "marketplaces")
public class Marketplace extends AbstractEntity {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "marketplace_name")
	@JsonbProperty("marketplace_name")
	private String marketplaceName;

	public Marketplace() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMarketplaceName() {
		return marketplaceName;
	}

	public void setMarketplaceName(String marketplaceName) {
		this.marketplaceName = marketplaceName;
	}

}
