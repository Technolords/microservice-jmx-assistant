package net.technolords.micro.camel;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataHarvesterCamelContext extends Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataHarvesterCamelContext.class);

    @Override
    public void beforeStart() {
        LOGGER.info("Before start called...");
    }

    @Override
    public void afterStart() {
        LOGGER.info("After start called...");
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
        LOGGER.info("About to start the JMX assistant...");
        DataHarvesterCamelContext dataHarvesterCamelContext = new DataHarvesterCamelContext();
        dataHarvesterCamelContext.startService();
    }
}
