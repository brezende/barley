package barley.test.utils

import java.util.Map

import javax.servlet.http.HttpSession;

import org.eclipse.jetty.http.HttpMethod;

import barley.RawRequest;

public class RequestMock implements RawRequest {
	HttpSession session
	Map<String, String[]> queryParams
	String uri
	String body
	Map<String, String> headers
	Map<String, String> cookies
	HttpMethod method
}
