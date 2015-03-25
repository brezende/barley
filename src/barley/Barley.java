package barley;

import org.eclipse.jetty.http.HttpMethod;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class Barley {

	public static final BarleyApp app = new BarleyApp();

	public static void get(String path, JsonNode validator, IEndpointHandler handler) {
		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		final JsonSchema schema;
		try {
			schema = factory.getJsonSchema(validator);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		app.addEndpoint(HttpMethod.GET, path, schema, handler);
	}
	public static void get(String path, String validatorResource, IEndpointHandler handler) {
		JsonNode validator = Util.loadJson(validatorResource);
		get(path, validator, handler);
	}

	public static void get(String path, IEndpointHandler handler) {
		get(path, (JsonNode) null, handler);
	}

}