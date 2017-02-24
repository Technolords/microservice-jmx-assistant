package net.technolords.micro.registry;

import static net.technolords.micro.config.PropertiesManager.PROP_BUILD_DATE;
import static net.technolords.micro.config.PropertiesManager.PROP_BUILD_VERSION;

import java.io.IOException;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import net.technolords.micro.config.PropertiesManager;
import net.technolords.micro.model.ModelManager;

public class JolokiaRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(JolokiaRegistry.class);
    private static final String BEAN_PROPERTIES = "props";
    private static final String BEAN_META_DATA_PROPERTIES = "propsMetaData";
    private static final String BEAN_MODEL = "model";
    private static Main main;

    /**
     * Custom constructor with a reference of the main which is used to store the properties.
     *
     * @param mainReference
     *  A reference of the Main object.
     */
    public static void registerPropertiesInRegistry(Main mainReference) {
        main = mainReference;
        main.bind(BEAN_META_DATA_PROPERTIES, PropertiesManager.extractMetaData());
        main.bind(BEAN_PROPERTIES, PropertiesManager.extractProperties());
    }

    public static void registerBeansInRegistryBeforeStart() throws JAXBException, IOException, SAXException {
        main.bind(BEAN_MODEL, new ModelManager(findConfiguredJolokiaConfiguration()));
        LOGGER.info("Beans added to the registry...");
    }

    public static void registerBeansInRegistryAfterStart() {

    }

    public static ModelManager findModelManager() {
        return main.lookup(BEAN_MODEL, ModelManager.class);
    }

    public static Properties findProperties() {
        return main.lookup(BEAN_PROPERTIES, Properties.class);
    }

    public static String findConfiguredUsername() {
        return (String) findProperties().get(PropertiesManager.PROP_USERNAME);
    }

    public static String findConfiguredPassword() {
        return (String) findProperties().get(PropertiesManager.PROP_PASSWORD);
    }

    public static String findConfiguredPeriod() {
        return (String) findProperties().get(PropertiesManager.PROP_PERIOD);
    }

    public static int findConnectionTimeout() {
        int value = -1;
        String connectionTimeout = (String) findProperties().get(PropertiesManager.PROP_CONNECTION_TIMEOUT);
        if (connectionTimeout != null) {
            try {
                value = Integer.parseInt(connectionTimeout);
            } catch (NumberFormatException e) {
                LOGGER.error("Connection timeout is not a number", e);
            }
        }
        return value;
    }

    public static String findConfiguredJolokiaConfiguration() {
        return (String) findProperties().get(PropertiesManager.PROP_JOLOKIA_CONFIG);
    }

    public static String findBuildMetaData() {
        Properties properties = main.lookup(BEAN_META_DATA_PROPERTIES, Properties.class);
        StringBuilder buffer = new StringBuilder();
        buffer.append(properties.get(PROP_BUILD_VERSION));
        buffer.append(" ");
        buffer.append(properties.get(PROP_BUILD_DATE));
        return buffer.toString();
    }
}
