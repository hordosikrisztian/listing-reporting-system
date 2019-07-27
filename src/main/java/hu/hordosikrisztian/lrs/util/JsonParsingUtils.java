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
import javax.json.bind.JsonbConfig;

import hu.hordosikrisztian.lrs.exception.InputJsonParsingException;

public class JsonParsingUtils {

	public static <T> List<T> storeInputJsonDataInEntityList(HttpURLConnection conn, Class<T> clazz) {
		List<T> entities = new ArrayList<>();

		try (InputStream is = conn.getInputStream()) {
			JsonbConfig jsonbConfig = new JsonbConfig().withFormatting(true);
			Jsonb jsonb = JsonbBuilder.create(jsonbConfig);

			JsonArray jsonArray = jsonb.fromJson(is, JsonArray.class);

			for (JsonValue jsonValue : jsonArray) {
				T entity = jsonb.fromJson(jsonValue.toString(), clazz);

				entities.add(entity);

				// TODO Only for testing purposes - remove.
				// System.out.println(jsonb.toJson(listing));
			}
		} catch (IOException e) {
			throw new InputJsonParsingException("Exception while parsing input JSON: ", e);
		} finally {
			conn.disconnect();
		}

		return entities;
	}

}
