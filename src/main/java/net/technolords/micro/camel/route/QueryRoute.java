package net.technolords.micro.camel.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.camel.processor.QueryProcessor;
import net.technolords.micro.model.jaxb.Attribute;
import net.technolords.micro.model.jaxb.Attributes;
import net.technolords.micro.model.jaxb.Host;
import net.technolords.micro.model.jaxb.JolokiaQuery;
import net.technolords.micro.model.jaxb.ObjectName;
import net.technolords.micro.model.jaxb.Output;
import net.technolords.micro.model.jaxb.output.LogOutput;

/**
 * The responsibility of this route is to create a Jolokia query based on the provided
 * input parameters (host, and configured query), execute the query and delegate the
 * response to the output route. Note that in case of multiple queries this route is
 * called multiple times. So the scope is simple, deal with a single query.
 */
public class QueryRoute extends RouteBuilder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public static final String ROUTE_ID = "routeQuery";
    public static final String ROUTE_ENDPOINT = "direct:query";
    private Processor queryProcessor = new QueryProcessor();

    @Override
    public void configure() throws Exception {
        from(ROUTE_ENDPOINT)
                .routeId(ROUTE_ID)
                .id(ROUTE_ID)
//                .process(new Processor() {
//                    @Override
//                    public void process(Exchange exchange) throws Exception {
//                        LOGGER.info("Creating meta data for query...");
//                        // Set headers
//                        Host host = new Host();
//                        host.setUsername("admin");
//                        host.setPassword("admin");
//                        host.setHost("http://localhost:8181/jolokia");
//                        exchange.getIn().setHeader(JolokiaMain.HEADER_HOST, host);
//                        ObjectName objectName = new ObjectName();
//                        objectName.setObjectName("java.lang:type=Memory");
//                        Attribute attribute = new Attribute();
//                        attribute.setAttribute("HeapMemoryUsage");
//                        Attributes attributes = new Attributes();
//                        attributes.getAttributes().add(attribute);
//                        JolokiaQuery jolokiaQuery = new JolokiaQuery();
//                        jolokiaQuery.setObjectName(objectName);
//                        jolokiaQuery.setAttributes(attributes);
//                        exchange.getIn().setHeader(JolokiaMain.HEADER_QUERY, jolokiaQuery);
//                        // Set property
//                        LogOutput logOutput = new LogOutput();
//                        Output output = new Output();
//                        output.setLogOutput(logOutput);
//                        exchange.setProperty(JolokiaMain.PROPERTY_OUTPUT, logOutput);
//                    }
//                })
                .process(this.queryProcessor)
                .to(OutputRoute.ROUTE_ENDPOINT);
    }
}
