package barley.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import barley.BarleyApp;
import barley.RawRequest;
import barley.Response;

public class JettyHandler extends AbstractHandler {
	private BarleyApp app;

	public JettyHandler(BarleyApp app) {
		this.app = app;
	}

	public void handle(String target, Request baseRequest, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
				throws IOException, ServletException {
		RawRequest request = new HttpRequest(httpRequest);
		Response response = new HttpResponse(httpResponse);
		app.handle(target, request, response);
		baseRequest.setHandled(true);
	}

}
