package net.technolords.micro.camel.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.camel.expression.HostSplitter;

public class ContextRoute extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public static final String ROUTE_ID = "routeContext";
    public static final String ROUTE_ENDPOINT = "direct:context";
    public static final String MARKER_FOR_HOST_ROUTE = "markerForHostRoute";
    private HostSplitter hostSplitter = new HostSplitter();

    // TODO: parameter for parallel hosts + thread pool

    @Override
    public void configure() throws Exception {
        from(ROUTE_ENDPOINT)
                .routeId(ROUTE_ID)
                .id(ROUTE_ID)
                .log(LoggingLevel.DEBUG, LOGGER, "Executing context (splitting by host)...")
                .split()
                    .method(hostSplitter)
                    .setHeader(JolokiaMain.HEADER_HOST, body())
                    .to(HostRoute.ROUTE_ENDPOINT)
                    .id(MARKER_FOR_HOST_ROUTE);

    }

}
