package net.technolords.micro.camel.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.processor.PrepareQueryContextProcessor;

/**
 * The responsibility of this route is to create a query context. In order to determine
 * this context it might (optional) require execution of a call, this call is a
 * query to determine whether multiple JVM's are in scope. In the end, the context
 * contains a map with JVM's and a list of queries to be executed (later).
 */
public class PrepareRoute extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public static final String ROUTE_ID = "routePrepare";
    public static final String ROUTE_ENDPOINT = "direct:prepare";
    public static final String MARKER_FOR_CONTEXT_ROUTE = "markerForContextRoute";
    private Processor prepareQueryContextProcessor = new PrepareQueryContextProcessor();

    @Override
    public void configure() throws Exception {
        from(ROUTE_ENDPOINT)
                .routeId(ROUTE_ID)
                .id(ROUTE_ID)
                .log(LoggingLevel.INFO, LOGGER, "Preparing context...")
                .process(this.prepareQueryContextProcessor)
                .to(ContextRoute.ROUTE_ENDPOINT)
                .id(MARKER_FOR_CONTEXT_ROUTE);
    }
}
