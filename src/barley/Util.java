package barley;

import com.github.fge.jackson.JsonLoader;
import com.fasterxml.jackson.databind.JsonNode;

public class Util {
	public static JsonNode loadJson(String path) {
		try {
			return JsonLoader.fromResource(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
