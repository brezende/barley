package barley.test

import groovy.json.JsonSlurper;

import org.eclipse.jetty.http.HttpMethod;

import barley.BarleyApp;
import barley.test.utils.EchoEndpoint;
import barley.test.utils.RequestMock;
import barley.test.utils.ResponseMock;
import spock.lang.Specification;

class EndpointsSpec extends Specification {
		
		static BarleyApp app;
		static JsonSlurper slurper = new JsonSlurper()
		
		def setupSpec() {
			app = new BarleyApp()
			app.addEndpoint(HttpMethod.GET, "/echo/{param1}/stuff/{param2}/morestuff/{param3}", null, new EchoEndpoint())
		}
	
		def "Send get request with cookies, headers and query string params"() {
			given:
			def param1 = 'abc'
			def param2 = ''
			def param3 = '123'
			def param4 = 'xyz'
			def param5 = '456'
			def body = "My body my rules, so here are some UTF-8 chars: áéãç####"
			def uri = "/echo/${param1}/stuff/${param2}/morestuff/${param3}"
			def method = HttpMethod.GET
			def request = new RequestMock();
			request.uri = uri
			request.method = method
			request.body = body
			request.queryParams = [
				'param4': [param4,param4],
				'param5': [param5]
			]
			def response = new ResponseMock();

			when:
			app.handle(uri, request, response)
			
			then:
			def expected = [
				"body":body,
				"method":method.toString(),
				"uri":uri,
				"queryParams":[
					'param4': [param4,param4],
					'param5': [param5]
				],
				"pathParams":[
					'param1':param1,
					'param2':param2,
					'param3':param3,
				],
				"headers":[:],
				"cookies":[:]
			]
			slurper.parseText(response.body) == expected
			response.status == 200
		}
}
