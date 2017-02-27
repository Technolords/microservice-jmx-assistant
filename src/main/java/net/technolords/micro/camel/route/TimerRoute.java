package net.technolords.micro.camel.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.processor.ErrorProcessor;
import net.technolords.micro.registry.JolokiaRegistry;

/**
 * The responsibility of this route is to generate 'events' based on a timer.
 */
public class TimerRoute extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String DEFAULT_PERIOD = "10s";
    public static final String ROUTE_ID_TIMER = "routeTimer";
    private static final String TIMER_MAIN = "timer://queryTimer";
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

        from(generateTimerEndpoint())
                .routeId(ROUTE_ID_TIMER)
                .id(ROUTE_ID_TIMER)
                .setExchangePattern(ExchangePattern.InOnly)
                .log(LoggingLevel.DEBUG, LOGGER, "Got time event...")
                .to(PrepareRoute.ROUTE_ENDPOINT);

    }

    /**
     * Auxiliary method to generate a Camel endpoint representing a Timer.
     * The expected format:
     *
     *  timer://queryTimer?fixedRate=true&period=10s
     *
     * @return
     *  A String representing a Camel Timer endpoint
     */
    protected String generateTimerEndpoint() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(TIMER_MAIN);
        buffer.append(QUESTION_SIGN).append("fixedRate").append(EQUAL_SIGN).append(TRUE_VALUE);
        buffer.append(AND_SIGN).append("period").append(EQUAL_SIGN).append(this.period);
        return buffer.toString();
    }
}
