package barley.validation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import barley.Request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.util.AsJson;
import com.github.fge.jsonschema.main.JsonSchema;
import com.google.common.base.CaseFormat;

public class ValidationUtils {
	protected static JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
	private static final Logger logger = LoggerFactory
			.getLogger(ValidationUtils.class);

	public static ProcessingReport validate(Request request, JsonSchema schema) throws Exception {
		JsonNode inputJson = buildInputJson(request);
		ProcessingReport validationReport = schema.validate(inputJson, true);
		return validationReport;
	}
	
	protected static JsonNode buildInputJson(Request request) throws IOException {
	    ObjectNode topNode = new ObjectNode(nodeFactory);
		topNode.put("body", getBodyNode(request));
		topNode.put("query_string", getQueryNode(request));
		topNode.put("path", getPathNode(request));
		return topNode;
	}

	protected static JsonNode getBodyNode(Request request) throws IOException {
		String bodyContent = request.getBody();
		if(bodyContent.trim().isEmpty()) {
			return new ObjectNode(nodeFactory);
		}
		JsonNode bodyNode = JsonLoader.fromString(bodyContent);
		return bodyNode;
	}

	protected static JsonNode getQueryNode(Request request) {
		ObjectNode queryNode = new ObjectNode(nodeFactory);
		Map<String, String[]> queryParams = request.getQueryParams();
		if(queryParams.isEmpty()) {
			return queryNode;
		}
		
		for (Entry<String, String[]> queryParam : queryParams.entrySet()) {
			String name = queryParam.getKey();
			String[] values = queryParam.getValue();
			if (values == null) {
				continue;
			}
			if (values.length > 1) {
				ArrayNode arrayNode = new ArrayNode(nodeFactory);
				for (String val : values) arrayNode.add(val);
				queryNode.put(name, arrayNode);
			} else if(values.length > 0) {
				queryNode.put(name, values[0]);
			}
		}
		return queryNode;
	}

	protected static JsonNode getPathNode(Request request) {
		ObjectNode pathNode = new ObjectNode(nodeFactory);
		Map<String, String> pathParams = request.getPathParams();
		if (pathParams.isEmpty()) {
			return pathNode;
		}
		
		for (Entry<String, String> pathParam : pathParams.entrySet()) {
			String name = pathParam.getKey();
			if(name.startsWith(":")) {
				name = name.replaceFirst(":", "");
			}
			String value = pathParam.getValue();
			pathNode.put(name, value);
		}
		return pathNode;
	}

	private static final Map<String, String> arrayKeys;
	static {
		arrayKeys = new HashMap<>();
		arrayKeys.put("required", "missing");
		arrayKeys.put("additionalProperties", "unwanted");
	}

	public static ValidationError toValidationError(ProcessingReport pr) {
		JsonNode fullNode = ((AsJson) pr).asJson();
		logger.debug(fullNode.toString());
		ValidationError validationError = new ValidationError();
		for (JsonNode node : fullNode) {
			String keyword = node.path("keyword").textValue();
			String pointer = getPointer(node, keyword);
			if (arrayKeys.get(keyword) != null) {
				addErrorsArray(node, pointer, keyword, arrayKeys.get(keyword),
						validationError);
			} else {
				validationError.addError(pointer,
						pointer.substring(pointer.lastIndexOf("/") + 1),
						getCode(keyword));
			}
		}
		return validationError;
	}

	private static void addErrorsArray(JsonNode node, String pointer,
			String keyword, String arrayKey, ValidationError validationError) {
		String code = getCode(keyword);
		JsonNode nodeArray = node.path(arrayKey);
		for (final JsonNode propNode : nodeArray) {
			validationError.addError(pointer, propNode.textValue(), code);
		}
	}

	private static String getCode(String keyword) {
		switch (keyword) {
		case "required":
			return "MISSING";
		case "not":
			return "NOT_CONDITION_FAILED"; // ugly
		case "additionalProperties":
			return "UNKNOWN_FIELD";
		default:
			return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE,
					keyword);
		}
	}

	private static String getPointer(JsonNode node, String keyword) {
		String pointer = node.path("instance").path("pointer").textValue();
		String realProperty = null;
		if ("dependencies".equals(keyword)) {
			realProperty = node.path("property").textValue();
		}
		pointer = (realProperty != null) ? String.format("%s/%s", pointer,
				realProperty) : pointer;
		return pointer;
	}
}