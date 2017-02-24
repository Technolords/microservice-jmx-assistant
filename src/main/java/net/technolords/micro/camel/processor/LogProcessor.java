package net.technolords.micro.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jolokia.client.request.J4pResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.JolokiaMain;

public class LogProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public void process(Exchange exchange) throws Exception {
        J4pResponse response = exchange.getOut().getHeader(JolokiaMain.HEADER_RESPONSE, J4pResponse.class);
        // In case of errors, there is no response and thus nothing to 'log'.
        if (response != null) {
            LOGGER.info("Response: {}", response.asJSONObject().toJSONString());
        }
    }
}
