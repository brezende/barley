package barley;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.core.report.ProcessingReport;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

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
		app.addEndpoint(path, schema, handler);
		//Spark.get(path, (req, res) -> {
		//	Map<String, String> params = req.params();
		//	JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		//	ObjectNode inputJson = nodeFactory.objectNode();
		//	for(Entry<String, String> param: params.entrySet()) {
		//		inputJson.put(param.getKey(), param.getValue());
		//	}
		//	ProcessingReport report = schema.validate(inputJson);
		//	if(!report.isSuccess()) {
		//		System.out.println(report);
		//		return report;
		//	}
		//	return handler.handle(req, res);
		//});
	}
}
