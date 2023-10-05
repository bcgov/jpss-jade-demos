package ccm.samples;

import org.apache.camel.builder.RouteBuilder;

import jakarta.ws.rs.core.MediaType;

public class HelloWorldRestService extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		restConfiguration()
		.contextPath("{{context-path}}")
		//.apiContextPath("{{api-path}}")
        //.apiProperty("api.title", "{{api-title}}")
        //.apiProperty("api.version", "{{api-version}}")
        //.apiProperty("cors", "true") //FIXME mcostell make this configuratble 
        //.apiContextRouteId("api")
        .port("{{port}}");
	/**
	 * @author mcostell rest implementation 
	 */
	rest("/hello").apiDocs(Boolean.TRUE)
			.get("/{language}")
				.id("rest-hello-world-by-lang")
				.description("Hello World By Language")
				.produces(MediaType.APPLICATION_JSON)
				.to("direct:performTranslateLang")
			.get("/ping/{ping}")
				.id("something")
				.to("direct:pong");
	
	
	from("direct:performTranslateLang")
		.id("route-perform-translate-lang")
		.log("${header.language}")
		.choice()
		 .when(header("language").isEqualTo("spanish"))
		 	.log("Hola")
		 	.setBody(simple("hola"))
		 .when(header("language").isEqualTo("french"))
         	.log("bonjour")
         	.setBody(simple("bonjour"))
         .otherwise()
         	.log("Unsupported language")
			.setBody(simple("Unsupported"))
		.marshal().json(Boolean.TRUE); 
	
	from("direct:pong")
		.log("ping pong")
		.setBody(simple("pong"))
		.marshal().json(Boolean.TRUE);
	
}

}
