package barley;

import java.util.Map;

public interface Request extends RawRequest {

	Map<String, String> getPathParams();
}
