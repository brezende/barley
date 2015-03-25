package barley.http;

import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

import barley.BarleyApp;

public class JettyServer {
	private Server server;

	public JettyServer(BarleyApp app) {
    	try {
			Resource confXML = Resource.newSystemResource("jetty.xml");
			XmlConfiguration configuration = new XmlConfiguration(
					confXML.getInputStream());
			Map<String, Object> idMap = configuration.getIdMap();
			idMap.put("barleyApp", app);
			server = (Server) configuration.configure();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void start() throws Exception {
		this.server.start();
		this.server.join();
	}

}
