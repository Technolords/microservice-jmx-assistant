package net.technolords.micro.camel.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.processor.QueryProcessor;

/**
 * The responsibility of this route is to create a Jolokia query based on the provided
 * input parameters (host, and configured query), execute the query and delegate the
 * response to the output route. Note that in case of multiple queries this route is
 * called multiple times. So the scope is simple, deal with a single query.
 */
public class QueryRoute extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public static final String ROUTE_ID = "routeQuery";
    public static final String ROUTE_ENDPOINT = "direct:query";
    private Processor queryProcessor = new QueryProcessor();

    @Override
    public void configure() throws Exception {
        from(ROUTE_ENDPOINT)
                .routeId(ROUTE_ID)
                .id(ROUTE_ID)
                .log(LoggingLevel.INFO, LOGGER, "Executing query...")
                .process(this.queryProcessor)
                .to(OutputRoute.ROUTE_ENDPOINT);
    }
}
