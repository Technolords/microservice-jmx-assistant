package net.technolords.micro.camel;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import net.technolords.micro.camel.listener.JolokiaMainListener;
import net.technolords.micro.camel.route.TimerRoute;
import net.technolords.micro.registry.JolokiaRegistry;

public class JolokiaMain extends Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(JolokiaMain.class);
    public static final String PROPERTY_OUTPUT = "output";

    public JolokiaMain() {
        JolokiaRegistry.registerPropertiesInRegistry(this);
    }

    @Override
    public void beforeStart() throws JAXBException, IOException, SAXException {
        LOGGER.info("Before start called...");
        JolokiaRegistry.registerBeansInRegistryBeforeStart();
        super.addMainListener(new JolokiaMainListener());
        super.addRouteBuilder(new TimerRoute());
    }

    @Override
    public void afterStart() {
        LOGGER.info("After start called...");
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
