package barley;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IEndpointHandler {
	public Object handle(HttpServletRequest request, HttpServletResponse response);
}
