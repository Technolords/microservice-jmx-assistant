package net.technolords.micro.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.info("Todo");
    }
}
