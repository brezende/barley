package barley;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Conf {
	public static final Config CONFIG;
	
	static {
		Config config = null;
		String[] resources = new String[] {"resources",};
		for(String resourceName : resources) {
			Config resourceConfig= ConfigFactory.load(resourceName);
			if (config == null) {
				config = resourceConfig;
			} else {
				config = config.withFallback(resourceConfig);
			}
		}
		CONFIG = config;
	}
}
