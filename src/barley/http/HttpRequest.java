package barley.http;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.http.HttpMethod;

import barley.RawRequest;

public class HttpRequest implements RawRequest {

	private HttpServletRequest httpRequest;
	private HttpMethod method;
	private Map<String, String> pathParams;
	
	public HttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public HttpSession getSession() {
		return this.httpRequest.getSession();
	}

	public Map<String, String[]> getQueryParams() {
		return this.httpRequest.getParameterMap();
	}

	public String getUri() {
		return this.httpRequest.getRequestURI();
	}

	public String getBody() {
		return null;
	}

	public HttpMethod getMethod() {
		if(this.method == null) {
			String method = this.httpRequest.getMethod();
			this.method = HttpMethod.fromString(method.toUpperCase());
		}
		return this.method;
	}

	public void setPathParams(Map<String, String> pathParams) {
		this.pathParams = pathParams;
	}
	
	public Map<String, String> getPathParams() {
		return this.pathParams;
	}
}
