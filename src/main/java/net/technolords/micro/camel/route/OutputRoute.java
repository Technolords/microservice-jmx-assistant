package net.technolords.micro.camel.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.predicate.OutputFilePredicate;
import net.technolords.micro.camel.predicate.OutputRedisPredicate;
import net.technolords.micro.camel.processor.LogProcessor;

public class OutputRoute extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String ROUTE_ID = "routeOutput";
    public static final String MARKER_FOR_FILE = "outputForFile";
    public static final String MARKER_FOR_REDIS = "outputForRedis";
    public static final String MARKER_FOR_LOG = "outputForLog";
    public static final String ROUTE_ENDPOINT = "direct:output";
    private Predicate filePredicate = new OutputFilePredicate();
    private Predicate redisPredicate = new OutputRedisPredicate();
    private Processor logProcessor = new LogProcessor();

    @Override
    public void configure() throws Exception {
        from(ROUTE_ENDPOINT)
            .routeId(ROUTE_ID)
            .id(ROUTE_ID)
            .choice()
                .when(this.filePredicate)
                    .log(LoggingLevel.INFO, LOGGER, "Direct output to file...")
                    .to("mock:fileOutput")
                    .id(MARKER_FOR_FILE)
                .when(this.redisPredicate)
                    .log(LoggingLevel.INFO, LOGGER, "Direct output to redis...")
                    .to("mock:redisOutput")
                    .id(MARKER_FOR_REDIS)
                .otherwise()
                    .log(LoggingLevel.INFO, LOGGER, "Direct output to log..")
                    .process(this.logProcessor)
                    .id(MARKER_FOR_LOG);
    }
}
