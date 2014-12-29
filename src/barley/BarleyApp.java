package barley;

import spark.Route;

import com.github.fge.jsonschema.main.JsonSchema;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import org.springframework.web.util.UriTemplate;

public class BarleyApp {
	private List<Endpoint> endpoints;

	public static class Endpoint {
		public Endpoint(String path, JsonSchema schema, Route handler) {
			this.path = new UriTemplate(path);
			this.schema = schema;
			this.handler = handler;
		}
		public UriTemplate path;
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
		for(Endpoint endpoint: endpoints) {
			System.out.println("trying to match " + endpoint.path + " with " + target);
			if(endpoint.path.matches(target)) {
				System.out.println("match " + target);
				return;
			}
		}
		System.out.println("no match " + target);
	}
}
