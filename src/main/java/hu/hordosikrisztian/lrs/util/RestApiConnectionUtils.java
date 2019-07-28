package hu.hordosikrisztian.lrs.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import hu.hordosikrisztian.lrs.exception.HttpURLConnectionException;
import hu.hordosikrisztian.lrs.restendpoints.RequestMethodType;
import hu.hordosikrisztian.lrs.restendpoints.RestApiEndpoints;

public class RestApiConnectionUtils {

	public static HttpURLConnection connectToUrl(RestApiEndpoints endpoint, RequestMethodType requestMethodType) {
		HttpURLConnection conn = null;

		try {
			URL url = new URL(endpoint.getUrl());

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(requestMethodType.getRequestMethod());

			conn.connect();
		} catch (IOException e) {
			throw new HttpURLConnectionException("An error occurred during connecting to URL: " + conn.getURL(), e);
		}

		return conn;
	}

}
