package barley;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriTemplate;

import barley.validation.ValidationUtils;

import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.google.gson.Gson;

public class BarleyApp {
	private static final Gson GSON = new Gson();
	private List<Endpoint> endpoints;
	private static final Logger logger = LoggerFactory
			.getLogger(BarleyApp.class);

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
		if (endpoint.method == null
			|| endpoint.path == null
			|| endpoint.schema == null
			|| endpoint.handler == null) {
			throw new RuntimeException(
					"Method, path, schema and handler cannot be null...");
		}
		endpoints.add(endpoint);
	}

	public void handle(String target,
			RawRequest rawRequest,
			Response response) {
		try {
			HttpMethod method = rawRequest.getMethod();
			Endpoint endpoint = getMatchingEndpoint(method, target);
			if(endpoint == null) {
				renderResponse(new Error("NOT_FOUND", 404), response);
				return;
			}
	
			Request request = new EndpointRequestWrapper(endpoint, target, rawRequest);
			
			ProcessingReport report = ValidationUtils.validate(request, endpoint.schema);
			if(!report.isSuccess()) {
				renderResponse(ValidationUtils.toValidationError(report), response);
				return;
			}

			response.setStatus(200);
			Object handlerResponse = endpoint.handler.handle(request, response);
			if (handlerResponse != null) {
				renderResponse(handlerResponse, response);
			}
		} catch (Throwable t) {
			renderResponse(new Error("INTERNAL_SERVER_ERROR", 500), response);
		}
	}

	protected Endpoint getMatchingEndpoint(HttpMethod method, String target) {
		for (Endpoint endpoint: endpoints) {
			logger.debug("trying to match %s with %s", endpoint.path, target);
			if (endpoint.method.equals(method) && endpoint.path.matches(target)) {
				logger.debug("match %s", target);
				return endpoint;
			}
		}
		logger.debug("no match %s", target);
		return null;
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
