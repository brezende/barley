package barley;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jetty.http.HttpMethod;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.google.gson.Gson;

public class BarleyApp {
	private static final Gson GSON = new Gson();
	private List<Endpoint> endpoints;

	public static class Endpoint {
		public Endpoint(HttpMethod method, String path, JsonSchema schema, IEndpointHandler handler) {
			this.method = method;
			this.path = new UriTemplate(path);
			this.schema = schema;
			this.handler = handler;
		}
		public HttpMethod method;
		public UriTemplate path;
		public JsonSchema schema;
		public IEndpointHandler handler;
	}

	public BarleyApp() {
		endpoints = new ArrayList<Endpoint>();
	}

	public void addEndpoint(HttpMethod method, String path, JsonSchema schema, IEndpointHandler handler) {
		Endpoint endpoint = new Endpoint(method, path, schema, handler);
		addEndpoint(endpoint);
	}

	public synchronized void addEndpoint(Endpoint endpoint) {
		endpoints.add(endpoint);
	}

	public void handle(String target,
			RawRequest rawRequest,
			Response response) {
		HttpMethod method = rawRequest.getMethod();
		Endpoint endpoint = getMatchingEndpoint(method, target);
		if(endpoint == null) {
			//XXX return a 404 or something
			return;
		}

		Request request = new EndpointRequestWrapper(endpoint, target, rawRequest);
		
		if (endpoint.schema != null) {
			ProcessingReport report = validate(request, endpoint.schema);
			if(!report.isSuccess()) {
				renderResponse(report, response);
				return;
			}
		}
		response.setStatus(200);
		Object handlerResponse = endpoint.handler.handle(request, response);
		renderResponse(handlerResponse, response);
	}

	protected Endpoint getMatchingEndpoint(HttpMethod method, String target) {
		for(Endpoint endpoint: endpoints) {
			//XXX Debug
			System.out.println("trying to match " + endpoint.path + " with " + target);
			if(endpoint.method.equals(method) && endpoint.path.matches(target)) {
				//XXX Debug
				System.out.println("match " + target);
				return endpoint;
			}
		}
		//XXX Debug
		System.out.println("no match " + target);
		return null;
	}

	protected ProcessingReport validate(Request request, JsonSchema schema) {
		try {
			Map<String, String[]> params = request.getQueryParams();
			JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
			ObjectNode inputJson = nodeFactory.objectNode();
			for(Entry<String, String[]> param: params.entrySet()) {
				inputJson.put(param.getKey(), param.getValue()[0]);
			}
			return schema.validate(inputJson);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void renderResponse(Object handlerResponse, Response response) {
		String handlerResponseAsStr = null;
		if(handlerResponse != null) {
			handlerResponseAsStr = handlerResponse instanceof String ? (String) handlerResponse
					: GSON.toJson(handlerResponse);
		}
		if(handlerResponseAsStr != null) {
			try {
				response.setBody(handlerResponseAsStr);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
