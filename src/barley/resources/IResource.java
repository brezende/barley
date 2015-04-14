package barley.resources;

import java.util.Map;

public interface IResource {
	void initResource(Map<String, String> params);
	void destroyResource();
}
