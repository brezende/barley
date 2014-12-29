package barley;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class JettyHandler extends AbstractHandler {
	private BarleyApp app;

	public JettyHandler(BarleyApp app) {
		this.app = app;
	}

	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
		app.handle(target, baseRequest, request, response);
		baseRequest.setHandled(true);
	}
}
