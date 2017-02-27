package net.technolords.micro.camel.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.processor.ErrorProcessor;
import net.technolords.micro.registry.JolokiaRegistry;

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

    /*
        String invoker = exchange.getProperty(Exchange.TIMER_NAME, String.class);
        long period = exchange.getProperty(Exchange.TIMER_PERIOD, Long.class);
     */

    // TODO: experiment with short interval and sleep, abort timer?

    protected String generateTimerEndpoint() {
        StringBuilder buffer = new StringBuilder();
        // timer://queryTimer?fixedRate=true&period=10s
        buffer.append(TIMER_MAIN);
        buffer.append(QUESTION_SIGN).append("fixedRate").append(EQUAL_SIGN).append(TRUE_VALUE);
        buffer.append(AND_SIGN).append("period").append(EQUAL_SIGN).append(this.period);
        return buffer.toString();
    }
}
