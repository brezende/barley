package barley.test.utils

import barley.EndpointHandler;
import barley.Request;
import barley.Response;

public class EchoEndpoint extends EndpointHandler {

	@Override
	public Object handle(Request request, Response response) {
		[
			"uri": request.uri,
			"method": request.method,
			"body": request.body,
			"queryParams": request.queryParams?:[:],
			"pathParams": request.pathParams?:[:],
			"headers": request.headers?:[:],
			"cookies": request.cookies?:[:]
		]
	}

}
