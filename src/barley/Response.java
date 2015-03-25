package barley;


public interface Response {

	void setStatus(int statusCode);

	void setType(String contentType);

	void setBody(String body);

	void setHeader(String header, String value);

	void setCookie(String path, String name, String value, int maxAge, boolean secured);
	
}
