package net.technolords.micro.camel;

import javax.management.MalformedObjectNameException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jolokia.client.BasicAuthenticator;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JolokiaProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    // https://jolokia.org/reference/html/clients.html#client-java
    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.info("About to invoke a jolokia query...");
        String invoker = exchange.getProperty(Exchange.TIMER_NAME, String.class);
        long period = exchange.getProperty(Exchange.TIMER_PERIOD, Long.class);
        LOGGER.info("Timer data -> invoker: {}, period: {} ms", invoker, period);
        this.executeJolokiaQuery();
    }

    private void executeJolokiaQuery() throws MalformedObjectNameException, J4pException {
        J4pClient client = J4pClient
                .url("http://localhost:8185/jolokia")
                .user("admin")
                .password("admin")
                .authenticator(new BasicAuthenticator().preemptive())
                .connectionTimeout(3000)
                .build();
        J4pReadRequest request =
                new J4pReadRequest("java.lang:type=Memory","HeapMemoryUsage");
        request.setPath("used");
        J4pReadResponse response = client.execute(request);
        LOGGER.info("Response: {}", response.asJSONObject().toJSONString());
    }
}
