package ccm.samples;

import org.apache.camel.builder.RouteBuilder;

public class JPSSJadeCamelDemo extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		/**
		 * @author mcostell
		 * a timer route 
		 */
		 from("timer:tick?period=2s")
		 	.routeId("timer-route")
	        .log("sending to hello world rest service")
	        .to("http://localhost:8080/bcgov/hello/french")
	        .log("received ${body}"); 
		 from("timer:eltick?period=3s")
		    .routeId("el-timer-route")
	        .to("http://localhost:8080/bcgov/hello/spanish")
	        .log("received ${body}"); 
	    

	}

}
