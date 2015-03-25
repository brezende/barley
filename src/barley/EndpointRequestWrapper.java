package barley;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.eclipse.jetty.http.HttpMethod;

import barley.BarleyApp.Endpoint;

public class EndpointRequestWrapper implements Request {

	private RawRequest request;
	private Endpoint endpoint;
	private String target;
	private Map<String, String> pathParams;
	
	public EndpointRequestWrapper(Endpoint endpoint, String target, RawRequest request) {
		this.request = request;
		this.target = target;
		this.endpoint = endpoint;
	}
	
	@Override
	public HttpSession getSession() {
		return this.request.getSession();
	}

	@Override
	public Map<String, String[]> getQueryParams() {
		return this.request.getQueryParams();
	}

	@Override
	public String getUri() {
		return this.request.getUri();
	}

	@Override
	public String getBody() {
		return this.request.getBody();
	}

	@Override
	public HttpMethod getMethod() {
		return this.request.getMethod();
	}

	@Override
	public Map<String, String> getPathParams() {
		if (this.pathParams == null) {
			this.pathParams = this.endpoint.path.match(this.target);
		}
		return this.pathParams;
	}

}
