package barley;

import barley.resources.ResourceLoader;


public abstract class EndpointHandler {
	
	private ResourceLoader resources;

	public ResourceLoader getResources() {
		return resources;
	}

	public void setResources(ResourceLoader resources) {
		this.resources = resources;
	}

	public Object getResource(String name) {
		return resources.getResource(name);
	}

	public Object getResource(String name, Class clazz) {
		return resources.getResource(name, clazz);
	}

	public abstract Object handle(Request request, Response response) throws Exception;
}
