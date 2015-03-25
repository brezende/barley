package barley.http;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import barley.BarleyApp;

public class JettyServer {
	private BarleyApp app;
	private Server server;

	public JettyServer(BarleyApp app) {
		Server server = new Server();

		ServerConnector http = new ServerConnector(server);
		http.setPort(8080);
		server.addConnector(http);

		Handler handler = new JettyHandler(app);
		server.setHandler(handler);

		this.app = app;
		this.server = server;
	}

	public void start() throws Exception {
		this.server.start();
		this.server.join();
	}

}
