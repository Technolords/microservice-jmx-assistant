package net.technolords.micro.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.info("Processing error...");
        Exception exception = exchange.getException();
        if(exception == null) {
            Object caught = exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
            if (caught instanceof Exception) {
                exception = (Exception) caught;
            } else {
                // Not an exception but a throwable
                exception = new Exception((Throwable) caught);
            }
            LOGGER.debug("About to create an error context with exception: {}", exception.getMessage());
        }
    }
}
