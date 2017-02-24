package net.technolords.micro.camel;

import java.util.List;

import javax.management.MalformedObjectNameException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pExecRequest;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pRequest;
import org.jolokia.client.request.J4pResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.jolokia.JolokiaClientFactory;
import net.technolords.micro.model.ModelManager;
import net.technolords.micro.model.jaxb.Attribute;
import net.technolords.micro.model.jaxb.Attributes;
import net.technolords.micro.model.jaxb.JolokiaConfiguration;
import net.technolords.micro.model.jaxb.JolokiaQuery;
import net.technolords.micro.model.jaxb.ObjectName;
import net.technolords.micro.model.jaxb.Operation;
import net.technolords.micro.registry.DataHarvestRegistry;

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
        this.executeJolokiaQueries();
    }

    private void executeJolokiaQueries() throws MalformedObjectNameException, J4pException {
        if (this.modelManager == null) {
            this.modelManager = DataHarvestRegistry.findModelManager();
        }
        JolokiaConfiguration jolokiaConfiguration = this.modelManager.getJolokiaConfiguration();
        List<JolokiaQuery> jolokiaQueries = jolokiaConfiguration.getJolokiaQueries();
        for (JolokiaQuery jolokiaQuery : jolokiaQueries) {
            LOGGER.info("About to execute query id: {}", jolokiaQuery.getId());
            // Find client
            J4pClient client = JolokiaClientFactory.findJolokiaClient(jolokiaQuery);
            J4pRequest request;
            // Create or reuse query
            if (jolokiaQuery.getOperation() != null) {
                request = this.createExecRequest(jolokiaQuery);
//                J4pExecResponse response = client.execute(request);
//                LOGGER.info("Response: {}", response.asJSONObject().toJSONString());
            } else {
                request = this.createReadRequest(jolokiaQuery);
//                J4pReadResponse response = client.execute(request);
//                LOGGER.info("Response: {}", response.asJSONObject().toJSONString());
            }
            J4pResponse response = client.execute(request);
            LOGGER.info("Response: {}", response.asJSONObject().toJSONString());
        }
    }

    private J4pRequest createReadRequest(JolokiaQuery jolokiaQuery) throws MalformedObjectNameException {
        ObjectName objectName = jolokiaQuery.getObjectName();
        Attribute attribute = this.getAttribute(jolokiaQuery.getAttributes());
        J4pReadRequest readRequest = new J4pReadRequest(objectName.getObjectName(),attribute.getAttribute());
        return readRequest;
    }

    private J4pRequest createExecRequest(JolokiaQuery jolokiaQuery) throws MalformedObjectNameException {
        ObjectName objectName = jolokiaQuery.getObjectName();
        Operation operation = jolokiaQuery.getOperation();
        J4pExecRequest request = new J4pExecRequest(objectName.getObjectName(), operation.getOperation());
        return request;
    }

    private Attribute getAttribute(Attributes attributes) {
        for (Attribute attribute : attributes.getAttributes()) {
            return attribute;
        }
        return null;
    }

}
