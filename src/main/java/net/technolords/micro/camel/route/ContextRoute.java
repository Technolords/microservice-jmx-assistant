package net.technolords.micro.camel.route;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.expression.HostSplitter;

public class ContextRoute extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public static final String ROUTE_ID = "routeContext";
    public static final String ROUTE_ENDPOINT = "direct:context";
    public static final String MARKER_FOR_HOST_ROUTE = "markerForHostRoute";
    private HostSplitter hostSplitter = new HostSplitter();
    // TODO: parameter for parallel hosts + thread pool
    // TODO: parameter for parallel queries + thread pool

    @Override
    public void configure() throws Exception {
        from(ROUTE_ENDPOINT)
                .routeId(ROUTE_ID)
                .id(ROUTE_ID)
                .log(LoggingLevel.INFO, LOGGER, "Executing context...")
//                .split(this.hostSplitter).parallelProcessing().executorService(null)
                .split().method(hostSplitter)
                .to("direct:splitHosts")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        LOGGER.info("Inside splitHosts, headers: {} -> body: {}", exchange.getIn().getHeaders(), exchange.getIn().getBody());
                    }
                })
                .id(MARKER_FOR_HOST_ROUTE);

        from("direct:splitHosts")
                .to("mock:host");
    }

}
