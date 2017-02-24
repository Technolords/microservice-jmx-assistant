package net.technolords.micro.camel.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.processor.ErrorProcessor;
import net.technolords.micro.camel.processor.JolokiaProcessor;
import net.technolords.micro.registry.JolokiaRegistry;

public class TimerRoute extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String DEFAULT_PERIOD = "10s";
    public static final String ROUTE_ID_TIMER = "RouteTimer";
    public static final String ROUTE_ID_MAIN = "RouteMain";
    public static final String ROUTE_ID_JOLOKIA = "RouteJolokia";
    private static final String DIRECT_MAIN = "direct:main";
    private static final String DIRECT_JOLOKIA = "direct:jolokia";
    private static final String TIMER_MAIN = "timer://harvester";
    private static final String QUESTION_SIGN = "?";
    private static final String AND_SIGN = "&";
    private static final String EQUAL_SIGN = "=";
    private static final String TRUE_VALUE = "true";
    private String period = null;

    public TimerRoute() {
        this.period = JolokiaRegistry.findConfiguredPeriod();
        if (this.period == null || this.period.isEmpty()) {
            this.period = DEFAULT_PERIOD;
        }
        LOGGER.info("Using period: {}", this.period);
    }

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .handled(true)
                .process(new ErrorProcessor());

        // TODO: register event listener
        // validate queries and throw Veto when not valid
        // else log query pattern:
        // parent query:
        //   x
        // child query:
        //   y (filter: a)
        //   z (filter: b)

        from(generateTimerEndpoint())
                .routeId(ROUTE_ID_TIMER)
                .id(ROUTE_ID_TIMER)
                .setExchangePattern(ExchangePattern.InOut)
                .to(DIRECT_MAIN);

        from(DIRECT_MAIN)
                .routeId(ROUTE_ID_MAIN)
                .id(ROUTE_ID_MAIN)
                .log(LoggingLevel.INFO, LOGGER, "Got time event...")
                .to(DIRECT_JOLOKIA);

        from(DIRECT_JOLOKIA)
                .routeId(ROUTE_ID_JOLOKIA)
                .id(ROUTE_ID_JOLOKIA)
                .process(new JolokiaProcessor())
                .to(OutputRoute.ROUTE_ENDPOINT);

    }

    protected String generateTimerEndpoint() {
        StringBuilder buffer = new StringBuilder();
        // timer://harvest?fixedRate=true&period=10s
        buffer.append(TIMER_MAIN);
        buffer.append(QUESTION_SIGN).append("fixedRate").append(EQUAL_SIGN).append(TRUE_VALUE);
        buffer.append(AND_SIGN).append("period").append(EQUAL_SIGN).append(this.period);
        return buffer.toString();
    }
}
