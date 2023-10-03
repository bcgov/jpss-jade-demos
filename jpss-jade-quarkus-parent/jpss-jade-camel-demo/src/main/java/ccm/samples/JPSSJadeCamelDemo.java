package ccm.samples;

import org.apache.camel.builder.RouteBuilder;

public class JPSSJadeCamelDemo extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		/**
		 * @author mcostell
		 * FIXME outside of a poc/example appropriate types of dead letter channels might be a dead letter queue on a message broker, etc. 
		 */
		errorHandler(deadLetterChannel("log:info").maximumRedeliveries(3).redeliveryDelay(1000).backOffMultiplier(1.5));
		
		/**
		 * @author mcostell
		 * a set of timer routes 
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
		 
		 from("timer:badtick?period=5s")
		 .routeId("badtick-timer-route")
		 .circuitBreaker()
		 	.inheritErrorHandler(Boolean.TRUE)
		 	.faultToleranceConfiguration()
		 		.delay(50000) //TODO mcostell default is 5 seconds 
		 		.timeoutEnabled(Boolean.FALSE)
		 		.timeoutPoolSize(5) //TODO mcostell default is 10 
		 		.bulkheadEnabled(Boolean.TRUE)
		 		.bulkheadMaxConcurrentCalls(2) //TODO mcostell the default is 10
		 		.bulkheadWaitingTaskQueue(2) //TODO mcostell default is 10 
		 		.failureRatio(50) //TODO mcostell
		 		.requestVolumeThreshold(5) //TODO mcostell default is 20 
		 		.successThreshold(2) //TODO mcostell default is 1
		 	.end()
	     	.to("http://localhost:8080/fubar")
	     .onFallback()
	     	.log("** hitting fallback **")
	     	.throwException(new Exception("bad stuff"))
		 .log("received ${body}"); 
				 
	    

	}

}
