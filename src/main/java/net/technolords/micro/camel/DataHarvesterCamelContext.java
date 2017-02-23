package net.technolords.micro.camel;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import net.technolords.micro.registry.DataHarvestRegistry;

public class DataHarvesterCamelContext extends Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataHarvesterCamelContext.class);

    public DataHarvesterCamelContext() {
        DataHarvestRegistry.registerPropertiesInRegistry(this);
    }

    @Override
    public void beforeStart() throws JAXBException, IOException, SAXException {
        LOGGER.info("Before start called...");
        DataHarvestRegistry.registerBeansInRegistryBeforeStart();
        super.addRouteBuilder(new TimerRoute());
    }

    @Override
    public void afterStart() {
        LOGGER.info("After start called...");
        DataHarvestRegistry.registerBeansInRegistryAfterStart();
        LOGGER.info("Jolokia client service started ({}), use CTRL-C to terminate JVM", DataHarvestRegistry.findBuildMetaData());
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
        DataHarvesterCamelContext dataHarvesterCamelContext = new DataHarvesterCamelContext();
        dataHarvesterCamelContext.startService();
    }
}
