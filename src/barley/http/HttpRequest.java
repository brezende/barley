package barley.http;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.jetty.http.HttpMethod;

import barley.RawRequest;

public class HttpRequest implements RawRequest {

	private HttpServletRequest httpRequest;
	private HttpMethod method;
	private Map<String, String> pathParams;
	private Map<String, String> cookies;
	private Map<String, String> headers;
	
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
	
	public Map<String, String> getCookies() {
		if (this.cookies == null) {
			Cookie[] rawCookies = this.httpRequest.getCookies();
			this.cookies = new HashMap<>();
			for (Cookie cookie: rawCookies) {
				String name = cookie.getName();
				String value = cookie.getValue();
				this.cookies.put(name, value);
			}
		}
		return this.cookies;
	}

	public Map<String, String> getHeaders() {
		if (this.headers == null) {
			Enumeration<String> headerNames = this.httpRequest.getHeaderNames();
			this.headers = new HashMap<>();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				String value = this.httpRequest.getHeader(headerName);
				this.headers.put(headerName, value);
			}
		}
		return this.headers;
	}

}
