package barley;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.eclipse.jetty.http.HttpMethod;

public interface RawRequest {

	HttpSession getSession();

	Map<String, String[]> getQueryParams();
	
	String getUri();

	String getBody();
	
	Map<String, String> getHeaders();
	
	Map<String, String> getCookies();
	
	HttpMethod getMethod();
}
