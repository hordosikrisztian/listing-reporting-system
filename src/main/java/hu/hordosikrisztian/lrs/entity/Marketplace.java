package hu.hordosikrisztian.lrs.entity;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(schema = "list_rep", name = "marketplaces")
public class Marketplace extends AbstractEntity {

	@Id
	@NotNull(message = "Marketplace ID required.")
	private int id;

	@Column(name = "marketplace_name")
	@NotNull(message = "Marketplace name required.")
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

	@Override
	public String toString() {
		return "Marketplace [id=" + id + ", marketplaceName=" + marketplaceName + "]";
	}

}
