package barley.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import barley.BarleyApp;

public class JettyHandler extends AbstractHandler {
	private BarleyApp app;

	public JettyHandler(BarleyApp app) {
		this.app = app;
	}

	public void handle(String target, Request baseRequest, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
				throws IOException, ServletException {
		HttpRequest request = new HttpRequest(httpRequest);
		HttpResponse response = new HttpResponse(httpResponse);
		app.handle(target, request, response);
		String responseBody = response.getBody();
		if(responseBody != null) {
			httpResponse.getOutputStream().write(responseBody.getBytes("utf-8"));
		}
		baseRequest.setHandled(true);
	}

}
