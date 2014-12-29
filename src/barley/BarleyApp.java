package barley;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.core.report.ProcessingReport;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import org.springframework.web.util.UriTemplate;

public class BarleyApp {
	private List<Endpoint> endpoints;

	public static class Endpoint {
		public Endpoint(String path, JsonSchema schema, IEndpointHandler handler) {
			this.path = new UriTemplate(path);
			this.schema = schema;
			this.handler = handler;
		}
		public UriTemplate path;
		public JsonSchema schema;
		public IEndpointHandler handler;
	}

	public BarleyApp() {
		endpoints = new ArrayList<Endpoint>();
	}

	public void addEndpoint(String path, JsonSchema schema, IEndpointHandler handler) {
		Endpoint endpoint = new Endpoint(path, schema, handler);
		addEndpoint(endpoint);
	}

	public synchronized void addEndpoint(Endpoint endpoint) {
		endpoints.add(endpoint);
	}

	public void handle(String target,
			Request baseRequest,
			HttpServletRequest request,
			HttpServletResponse response) {
		Endpoint endpoint = getMatchingEndpoint(target);
		if(endpoint == null) {
			//XXX return a 404 or something
			return;
		}

		ProcessingReport report = validate(request, endpoint.schema);
		if(!report.isSuccess()) {
			renderResponse(report, response);
			return;
		}

		Object handlerResponse = endpoint.handler.handle(request, response);
		renderResponse(handlerResponse, response);
	}

	protected Endpoint getMatchingEndpoint(String target) {
		for(Endpoint endpoint: endpoints) {
			//XXX Debug
			System.out.println("trying to match " + endpoint.path + " with " + target);
			if(endpoint.path.matches(target)) {
				//XXX Debug
				System.out.println("match " + target);
				return endpoint;
			}
		}
		//XXX Debug
		System.out.println("no match " + target);
		return null;
	}

	protected ProcessingReport validate(HttpServletRequest request, JsonSchema schema) {
		try {
			Map<String, String[]> params = request.getParameterMap();
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

	protected void renderResponse(Object handlerResponse, HttpServletResponse response) {
		String handlerResponseAsStr = null;
		if(handlerResponse != null) {
			handlerResponseAsStr = handlerResponse.toString();
		}
		if(handlerResponseAsStr != null) {
			try {
				response.getOutputStream().write(handlerResponseAsStr.getBytes("utf-8"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
