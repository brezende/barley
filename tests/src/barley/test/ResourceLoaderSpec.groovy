package barley.test

import groovy.json.JsonSlurper;

import org.eclipse.jetty.http.HttpMethod;

import barley.BarleyApp;
import barley.Util;
import barley.resources.ResourceLoader;
import barley.test.utils.EchoEndpoint;
import barley.test.utils.RequestMock;
import barley.test.utils.ResponseMock;
import barley.test.utils.TestResource;
import spock.lang.Specification;

class ResourceLoaderSpec extends Specification {
		
		def "Check if resources are loaded correctly"() {
			when:
			//Check test/resources/resources.properties to check what should be loaded
			def resourceLoader = ResourceLoader.loadResources()
			
			then:
			def resource1 = resourceLoader.getResource("resource1", TestResource.class)
			resource1 != null
			resource1.getParams() == [
										"param1":"value1",
										"param2":"value"
									]
			def resource2 = resourceLoader.getResource("resource2", TestResource.class)
			resource2 != null
			resource2.getParams() == null
		}
}
