package barley;

import spark.Route;

import com.github.fge.jsonschema.main.JsonSchema;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

public class BarleyApp {
	private List<Endpoint> endpoints;

	public static class Endpoint {
		public Endpoint(String path, JsonSchema schema, Route handler) {
			this.path = path;
			this.schema = schema;
			this.handler = handler;
		}
		public String path;
		public JsonSchema schema;
		public Route handler;
	}

	public BarleyApp() {
		endpoints = new ArrayList<Endpoint>();
	}

	public void addEndpoint(String path, JsonSchema schema, Route handler) {
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
		System.out.println(target);
	}
}
