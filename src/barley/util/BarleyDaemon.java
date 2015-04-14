package barley.util;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import barley.BarleyApp;
import barley.http.JettyServer;

public abstract class BarleyDaemon implements Daemon{

	private static final Logger logger = LoggerFactory.getLogger(BarleyDaemon.class);
	private static BarleyApp app;
	private static JettyServer server ;

	@Override
	public void init(DaemonContext arg0) throws DaemonInitException, Exception {
		//ResourceLoader resources = initResources();
		app = new BarleyApp();
		server = new JettyServer(app);
	}

//	public ResourceLoader initResources() {
//		ResourceLoader resources = ResourceLoader.loadResources();
//		return resources;
//	}
//
//	public static OLXSpark initEndpoints(ResourceLoader resources) {
//		OLXSpark spark = new OLXSpark();
//		spark.setResourceLoader(resources);
//		spark.get("/account/:id", new AccountGetRoute(spark, "/account_get.json"));
//		spark.post("/account/", new AccountPostRoute(spark, "/account_post.json"));
//		spark.delete("/account/:id", new AccountDeleteRoute(spark, "/account_delete.json"));
//		return spark;
//	}

	@Override
	public void start() throws Exception {
		logger.info("ACCOUNT STARTED");
		server.start();
	}

	@Override
	public void stop() throws Exception {
		logger.info("ACCOUNT STOPPED");
		server.stop();
	}

	public BarleyApp getApp() {
		return BarleyDaemon.app;
	}
	
	@Override
	public void destroy() {
		//ResourceLoader resourceLoader = spark.getResourceLoader();
		//resourceLoader.destroyResources();
	}

}
