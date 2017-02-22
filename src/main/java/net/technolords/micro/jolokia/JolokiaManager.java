package net.technolords.micro.jolokia;

import org.apache.camel.Handler;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
    This manager should keep clients peristent in a mapping:

    key: jvm-name
    value: jolokia-url
 */
public class JolokiaManager {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public static final String GENERATE_RECIPIENT_LIST = "generateRecipientList";

    @Handler
    public String executeQuery(@Header(GENERATE_RECIPIENT_LIST) boolean generateRecipientList) {
        LOGGER.info("executeQuery called, with generateRecipientList value: {}", generateRecipientList);
        return "";
    }
}
