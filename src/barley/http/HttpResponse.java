package barley.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import barley.Response;

public class HttpResponse implements Response {

	private HttpServletResponse response;
	
	public HttpResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public void setStatus(int statusCode) {
		this.response.setStatus(statusCode);
	}

	public void setType(String contentType) {
		this.response.setContentType(contentType);
	}

	public void setBody(String body) {
	}

	public void setHeader(String header, String value) {
		this.response.setHeader(header, value);
	}

	public void setCookie(String path, String name, String value, int maxAge,
			boolean secured) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		cookie.setSecure(secured);
		this.response.addCookie(cookie);
	}
	
}
