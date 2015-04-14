package barley.test.utils;

import java.util.Map;

import barley.resources.IResource;

public class TestResource implements IResource {

	private Map<String, String> params;
	
	@Override
	public void initResource(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, String> getParams() {
		return this.params;
	}
	
	@Override
	public void destroyResource() {
	}

}
