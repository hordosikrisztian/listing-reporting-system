package hu.hordosikrisztian.lrs.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import hu.hordosikrisztian.lrs.exception.InputJsonParsingException;

public class JsonParsingUtils {

	public static <T> List<T> storeInputJsonDataInEntityList(HttpURLConnection conn, Class<T> clazz) {
		List<T> entityList = new ArrayList<>();

		try (InputStream is = conn.getInputStream()) {
			Jsonb jsonb = JsonbBuilder.create();

			JsonArray jsonArray = jsonb.fromJson(is, JsonArray.class);

			for (JsonValue jsonValue : jsonArray) {
				T entity = jsonb.fromJson(jsonValue.toString(), clazz);

				entityList.add(entity);
			}
		} catch (IOException e) {
			throw new InputJsonParsingException("Exception while parsing input JSON: ", e);
		} finally {
			conn.disconnect();
		}

		return entityList;
	}

}
