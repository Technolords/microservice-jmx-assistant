package net.technolords.micro.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jolokia.client.J4pClient;
import org.jolokia.client.request.J4pRequest;
import org.jolokia.client.request.J4pResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.jolokia.JolokiaClientFactory;
import net.technolords.micro.jolokia.JolokiaQueryFactory;
import net.technolords.micro.model.jaxb.Host;
import net.technolords.micro.model.jaxb.JolokiaQuery;

public class QueryProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.debug("QueryProcessor called...");
        Host host = exchange.getIn().getHeader(JolokiaMain.HEADER_HOST, Host.class);
        JolokiaQuery jolokiaQuery = exchange.getIn().getBody(JolokiaQuery.class);
        if (host != null) {
            if (jolokiaQuery != null) {
                try {
                    J4pClient client = JolokiaClientFactory.findJolokiaClient(host);
                    J4pRequest request = JolokiaQueryFactory.findJolokiaRequest(jolokiaQuery);
                    J4pResponse response = client.execute(request);
                    exchange.getIn().setHeader(JolokiaMain.HEADER_RESPONSE, response);
                } catch (Exception e) {
                    LOGGER.error("Unable to execute query", e);
                }
            } else {
                LOGGER.error("Unable to create query: no model");
            }
        } else {
            LOGGER.error("Unable to create query: no host");
        }
    }
}
