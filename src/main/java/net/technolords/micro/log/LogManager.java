package net.technolords.micro.log;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogManager.class);

    /**
     * Auxiliary method to (re)initialize the logging. This method is typically called from
     * the PropertiesManager when a external log4j2 configuration is provided, while at the
     * same time no CLI parameter was given.
     *
     * Setting the new config location is enough, as it will trigger a reconfigure
     * automatically.
     *
     * @param pathToLogConfiguration
     *  A path to the external log configuration file.
     */
    public static void initializeLogging(String pathToLogConfiguration) {
        Path path = FileSystems.getDefault().getPath(pathToLogConfiguration);
        LOGGER.trace("Path to log configuration: {} -> file exists: {}", pathToLogConfiguration, Files.exists(path));
        LoggerContext loggerContext = LoggerContext.getContext(false);
        loggerContext.setConfigLocation(path.toUri());
    }
}
