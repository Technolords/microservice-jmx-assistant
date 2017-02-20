package net.technolords.micro.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesManager.class);
    private static final String PATH_TO_META_DATA_FILE = "build-meta-data.txt";
    public static final String PROP_BUILD_VERSION = "build-version";
    public static final String PROP_BUILD_DATE = "build-timestamp";

    /**
     * Auxiliary method that loads properties from an embedded file. This file is dynamically updated during each
     * build (by Maven) and updates the build version as well as the build timestamp.
     *
     * @return
     *  The properties.
     */
    public static Properties extractMetaData() {
        InputStream inputStreamForMetaDataFile = PropertiesManager.class.getClassLoader().getResourceAsStream(PATH_TO_META_DATA_FILE);
        Properties properties = new Properties();
        try {
            properties.load(inputStreamForMetaDataFile);
        } catch (IOException e) {
            LOGGER.warn("Failed to load meta data properties", e);
        }
        LOGGER.debug("Got meta data properties: {}", properties.size());
        return properties;
    }
}
