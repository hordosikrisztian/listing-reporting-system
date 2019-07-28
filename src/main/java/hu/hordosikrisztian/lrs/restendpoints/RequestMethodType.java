package hu.hordosikrisztian.lrs.restendpoints;

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
