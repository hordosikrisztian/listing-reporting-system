package hu.hordosikrisztian.lrs.restendpoints;

public enum RestApiEndpoints {

	LISTING("https://my.api.mockaroo.com/listing?key=63304c70"),
	LOCATION("https://my.api.mockaroo.com/location?key=63304c70"),
	LISTING_STATUS("https://my.api.mockaroo.com/listingStatus?key=63304c70"),
	MARKETPLACE("https://my.api.mockaroo.com/marketplace?key=63304c70");

	private String url;

	private RestApiEndpoints(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}
