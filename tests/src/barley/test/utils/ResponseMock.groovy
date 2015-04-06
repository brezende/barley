package barley.test.utils

import javax.servlet.http.Cookie;

import barley.Response

public class ResponseMock implements Response {

	int status
	String type
	String body
	Map<String, String> headers = [:]
	Map<String, Cookie> cookies = [:]
	
	@Override
	public void setHeader(String header, String value) {
		headers[header] = value
	}

	@Override
	public void setCookie(String path, String name, String value, int maxAge,
			boolean secured) {
		def cookie = new Cookie(name, value)
		cookie.setMaxAge(maxAge)
		cookie.setPath(path)
		cookie.setSecure(secured)
		cookies[name] = cookie
	}

}
