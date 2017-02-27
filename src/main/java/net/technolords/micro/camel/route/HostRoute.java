package net.technolords.micro.camel.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostRoute extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public static final String ROUTE_ID = "routeHost";
    public static final String ROUTE_ENDPOINT = "direct:host";
    public static final String MARKER_FOR_QUERY_ROUTE = "markerForQueryRoute";

    // TODO: parameter for parallel queries + thread pool

    @Override
    public void configure() throws Exception {
        from(ROUTE_ENDPOINT)
                .routeId(ROUTE_ID)
                .id(ROUTE_ID)
                .log(LoggingLevel.INFO, LOGGER, "Executing host (splitting by query)...")
                .to("mock:query")
                .id(MARKER_FOR_QUERY_ROUTE);
    }
}
