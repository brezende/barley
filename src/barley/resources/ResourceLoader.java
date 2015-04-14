package barley.resources;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import barley.Conf;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;

public class ResourceLoader {
	private Map<String, IResource> resourceMap;

	public ResourceLoader() {
		this.resourceMap = new HashMap<String, IResource>();
	}

	public <T extends Object> T getResource(String name, Class<T> clazz) {
		Object resource = getResource(name);
		if(!clazz.isInstance(resource) ) {
			throw new RuntimeException(String.format("Could not cast resource [%s] to given Class [%s]",
					name,
					clazz.getName()));
		}
		return (T) resource;
	}
	
	public Object getResource(String name) {
		Object resource = this.resourceMap.get(name);
		if(resource == null) {
			throw new RuntimeException(String.format("No registered resource with given name [%s]",
					name));
		}
		return resource;
	}
	
	public void addResource(String name, IResource resource) {
		this.resourceMap.put(name, resource);
	}

	
	////TODO use jedis pool
	//Jedis jedis = new Jedis("localhost", 21447);
	//resources.addResource("redisAccount", jedis);
	//HttpResource authService = new HttpResource();
	//authService.init("http://dev04c6.srv.office:21501");
	//resources.addResource("authSerice", authService);
	public static ResourceLoader loadResources() {
		ResourceLoader resourceLoader = new ResourceLoader();
		Config resources = Conf.CONFIG.getConfig("resources");
		for (String resourceName : resources.root().keySet()) {
			Config resource = resources.getConfig(resourceName);
			try {
				String className = resource.getString("class");
				Class clazz = Class.forName(className);
				IResource resourceObj = (IResource) clazz.newInstance();
				Map<String, String> paramMap = new HashMap<String, String>();
				if (resource.hasPath("params")) {
					ConfigObject params = resource.getConfig("params").root();
					for (String paramName : params.keySet()) {
						String paramValue = "params";
						paramMap.put(paramName, paramValue);
					}
				}
				resourceObj.initResource(paramMap); 
				resourceLoader.addResource(resourceName, resourceObj);
			} catch (Exception e) {
				throw new RuntimeException(String.format("Error creating resource %s", resourceName), e);
			}
		}
		return resourceLoader;
	}
	
	public void destroyResources() {
		Collection<IResource> resources = resourceMap.values();
		for (IResource resource : resources) {
			resource.destroyResource();
		}
	}

}
