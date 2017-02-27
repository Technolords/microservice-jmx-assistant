package net.technolords.micro.camel.processor;

import java.util.List;

import javax.management.MalformedObjectNameException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pRequest;
import org.jolokia.client.request.J4pResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.jolokia.JolokiaClientFactory;
import net.technolords.micro.jolokia.JolokiaQueryFactory;
import net.technolords.micro.model.ModelManager;
import net.technolords.micro.model.jaxb.JolokiaConfiguration;
import net.technolords.micro.model.jaxb.JolokiaQuery;
import net.technolords.micro.registry.JolokiaRegistry;

@Deprecated
public class JolokiaProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private ModelManager modelManager;

    // https://jolokia.org/reference/html/clients.html#client-java
    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.info("About to invoke a jolokia query...");
        String invoker = exchange.getProperty(Exchange.TIMER_NAME, String.class);
        long period = exchange.getProperty(Exchange.TIMER_PERIOD, Long.class);
        LOGGER.info("Timer data -> invoker: {}, period: {} ms", invoker, period);
        this.executeJolokiaQueries(exchange);
    }

    private void executeJolokiaQueries(Exchange exchange) {
        if (this.modelManager == null) {
            this.modelManager = JolokiaRegistry.findModelManager();
        }
        JolokiaConfiguration jolokiaConfiguration = this.modelManager.getJolokiaConfiguration();
        List<JolokiaQuery> jolokiaQueries = jolokiaConfiguration.getJolokiaQueries();
        for (JolokiaQuery jolokiaQuery : jolokiaQueries) {
            LOGGER.info("About to execute query id: {}", jolokiaQuery.getId());
            try {
                J4pClient client = JolokiaClientFactory.findJolokiaClient(null);
                J4pRequest request = JolokiaQueryFactory.findJolokiaRequest(jolokiaQuery);
                J4pResponse response = client.execute(request);
                exchange.getIn().setHeader(JolokiaMain.HEADER_RESPONSE, response);
//                LOGGER.info("Response: {}", response.asJSONObject().toJSONString());
            } catch (MalformedObjectNameException | J4pException e) {
                LOGGER.error("Unable to execute query", e);
            }
        }
    }

}
