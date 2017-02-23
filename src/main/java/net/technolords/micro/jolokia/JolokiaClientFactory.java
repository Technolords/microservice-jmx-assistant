package net.technolords.micro.jolokia;

import org.jolokia.client.BasicAuthenticator;
import org.jolokia.client.J4pClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.registry.DataHarvestRegistry;

public class JolokiaClientFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(JolokiaClientFactory.class);
    private static final int DEFAULT_CONNECTION_TIMEOUT = 3000;

    public static J4pClient createJolokiaClient(String username, String password, String host) {
        J4pClient client;
        if (username != null) {
            client = J4pClient
                    .url(host)
                    .user(username)
                    .password(password)
                    .authenticator(new BasicAuthenticator().preemptive())
                    .connectionTimeout(getConnectionTimeout())
                    .build();
        } else {
            client = J4pClient.
                    url(host)
                    .connectionTimeout(getConnectionTimeout())
                    .build();
        }
        return client;
    }

    private static int getConnectionTimeout() {
        int connectionTimeout = DataHarvestRegistry.findConnectionTimeout();
        if (connectionTimeout == -1) {
            connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
        }
        LOGGER.info("Using connection timeout: {} ms", connectionTimeout);
        return connectionTimeout;
    }

}
