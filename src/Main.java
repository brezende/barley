import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import barley.Barley;
import barley.Util;

public class Main {

	public static final Queue<String> eventQueue = new ConcurrentLinkedQueue<String>();
	public static final Writer output;

	public static void main(String[] args) {

		Barley.get("/hello",
			Util.loadJson("/test.json"),
			(req, res) -> {
				eventQueue.add("hello");
				return "ok";
			});

		new Thread(() -> {
			while(true) {
				String event = eventQueue.poll();
				if(event != null) {
					try {
						output.write(event);
						output.flush();
						System.out.println(event);
					} catch(Throwable t) {
						t.printStackTrace();
					}
				}
				//Thread.sleep(1);
			}
		}).run();
	}

	static {
		Writer outputWriter = null;
		try {
			File file = new File("/tmp/a");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			outputWriter = new BufferedWriter(fw, 10);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		output = outputWriter;
	}
}
