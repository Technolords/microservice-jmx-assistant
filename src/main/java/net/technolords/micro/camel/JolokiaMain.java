package net.technolords.micro.camel;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import net.technolords.micro.camel.listener.JolokiaMainListener;
import net.technolords.micro.camel.route.ContextRoute;
import net.technolords.micro.camel.route.HostRoute;
import net.technolords.micro.camel.route.OutputRoute;
import net.technolords.micro.camel.route.PrepareRoute;
import net.technolords.micro.camel.route.QueryRoute;
import net.technolords.micro.camel.route.TimerRoute;
import net.technolords.micro.registry.JolokiaRegistry;

public class JolokiaMain extends Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(JolokiaMain.class);
    public static final String PROPERTY_OUTPUT = "output";
    public static final String HEADER_HOST = "host";
    public static final String HEADER_QUERY_CONTEXT = "queryContext";
    public static final String HEADER_RESPONSE = "response";

    public JolokiaMain() {
        JolokiaRegistry.registerPropertiesInRegistry(this);
    }

    @Override
    public void beforeStart() throws JAXBException, IOException, SAXException {
        LOGGER.debug("Before start called...");
        JolokiaRegistry.registerBeansInRegistryBeforeStart();
        super.addMainListener(new JolokiaMainListener());
        super.addRouteBuilder(new TimerRoute());
        super.addRouteBuilder(new PrepareRoute());
        super.addRouteBuilder(new ContextRoute());
        super.addRouteBuilder(new HostRoute());
        super.addRouteBuilder(new QueryRoute());
        super.addRouteBuilder(new OutputRoute());
    }

    @Override
    public void afterStart() {
        LOGGER.debug("After start called...");
        JolokiaRegistry.registerBeansInRegistryAfterStart();
        LOGGER.info("Jolokia client as service started ({}), use CTRL-C to terminate JVM", JolokiaRegistry.findBuildMetaData());
    }

    /**
     * Auxiliary method to start the micro service.
     *
     * @throws Exception
     *  When the micro service fails.
     */
    public void startService() throws Exception {
        super.run();
    }

    /**
     * The main executable.
     *
     * @param args
     *  The arguments.
     *
     * @throws Exception
     *  When the program fails.
     */
    public static void main(String[] args) throws Exception {
        LOGGER.info("About to start the Jolokia client...");
        JolokiaMain jolokiaMain = new JolokiaMain();
        jolokiaMain.startService();
    }
}
