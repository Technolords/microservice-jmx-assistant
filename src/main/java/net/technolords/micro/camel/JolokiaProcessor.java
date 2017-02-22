package net.technolords.micro.camel;

import javax.management.MalformedObjectNameException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jolokia.client.BasicAuthenticator;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pExecRequest;
import org.jolokia.client.request.J4pExecResponse;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.jolokia.JolokiaClientFactory;

public class JolokiaProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    // https://jolokia.org/reference/html/clients.html#client-java
    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.info("About to invoke a jolokia query...");
        String invoker = exchange.getProperty(Exchange.TIMER_NAME, String.class);
        long period = exchange.getProperty(Exchange.TIMER_PERIOD, Long.class);
        LOGGER.info("Timer data -> invoker: {}, period: {} ms", invoker, period);
//        this.executeJolokiaReadRequest();
        this.executeJolokiaExecRequest();
    }

    private void executeJolokiaExecRequest() throws MalformedObjectNameException, J4pException {
        J4pClient client = JolokiaClientFactory.createJolokiaClient();
        J4pExecRequest request = new J4pExecRequest("io.fabric8:type=Fabric", "containers()");
        J4pExecResponse response = client.execute(request);
        JSONObject jsonObject = response.asJSONObject();
        LOGGER.info("Response: {}", response.asJSONObject().toJSONString());
    }

    private void executeJolokiaReadRequest() throws MalformedObjectNameException, J4pException {
        J4pClient client = JolokiaClientFactory.createJolokiaClient();
        J4pReadRequest request =
                new J4pReadRequest("java.lang:type=Memory","HeapMemoryUsage");
        request.setPath("used");
        J4pReadResponse response = client.execute(request);
        LOGGER.info("Response: {}", response.asJSONObject().toJSONString());
    }

    /*
        <query
            type="exec"
        >
            <objectName>io.fabric8:type=Fabric</objectName>
            <operation>containers()</operation>
            <assign header="child-containers" type="map">
                <key filter="map-*">
                    <expression>meta-data/containerName</expression>
                </key>
                <value>
                    <expression>jolokiaUrl</expression>
                </value>
            </assign>
        </query>
     */

    /*
        logging:
            LOGGER.info("Response: {}", response.getValue().toString());
        response:
            2017-02-21 11:21:40,244 [INFO] [Camel (camel-1) thread #0 - timer://harvester] [org.apache.camel.util.CamelLogger] Got time event...
            2017-02-21 11:21:40,244 [INFO] [Camel (camel-1) thread #0 - timer://harvester] [net.technolords.micro.camel.JolokiaProcessor] About to invoke a jolokia query...
            2017-02-21 11:21:40,244 [INFO] [Camel (camel-1) thread #0 - timer://harvester] [net.technolords.micro.camel.JolokiaProcessor] Timer data -> invoker: harvester, period: 10000 ms
            2017-02-21 11:21:40,394 [INFO] [Camel (camel-1) thread #0 - timer://harvester] [net.technolords.micro.camel.JolokiaProcessor] Response: 87347896

        logging:
            LOGGER.info("Response: {}", response.asJSONObject().toJSONString());
        response:
            2017-02-21 11:22:41,550 [INFO] [Camel (camel-1) thread #0 - timer://harvester] [org.apache.camel.util.CamelLogger] Got time event...
            2017-02-21 11:22:41,551 [INFO] [Camel (camel-1) thread #0 - timer://harvester] [net.technolords.micro.camel.JolokiaProcessor] About to invoke a jolokia query...
            2017-02-21 11:22:41,551 [INFO] [Camel (camel-1) thread #0 - timer://harvester] [net.technolords.micro.camel.JolokiaProcessor] Timer data -> invoker: harvester, period: 10000 ms
            2017-02-21 11:22:41,699 [INFO] [Camel (camel-1) thread #0 - timer://harvester] [net.technolords.micro.camel.JolokiaProcessor] Response: {"request":{"path":"used","mbean":"java.lang:type=Memory","attribute":"HeapMemoryUsage","type":"read"},"value":88203048,"timestamp":1487672561,"status":200}
     */
}
