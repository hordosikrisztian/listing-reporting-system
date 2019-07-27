package hu.hordosikrisztian.lrs.rest.endpoints;

public enum RequestMethodType {

	GET("GET");

	private String requestMethod;

	private RequestMethodType(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

}
