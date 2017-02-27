package net.technolords.micro.jolokia;

import java.util.HashMap;
import java.util.Map;

import org.jolokia.client.BasicAuthenticator;
import org.jolokia.client.J4pClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.model.jaxb.Host;
import net.technolords.micro.registry.JolokiaRegistry;

public class JolokiaClientFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(JolokiaClientFactory.class);
    private static final int DEFAULT_CONNECTION_TIMEOUT = 3000;
    private static Map<String, J4pClient> clientMap = new HashMap<>();

    public static J4pClient findJolokiaClient(Host host) {
        if (clientMap.containsKey(host.getHost())) {
            LOGGER.debug("Found cached client...");
            return clientMap.get(host.getHost());
        } else {
            J4pClient client = createJolokiaClient(host);
            LOGGER.info("Created client...");
            clientMap.put(host.getHost(), client);
            return clientMap.get(host.getHost());
        }
    }

    private static J4pClient createJolokiaClient(Host host) {
        J4pClient client;
        if (host.getUsername() != null) {
            client = J4pClient
                    .url(host.getHost())
                    .user(host.getUsername())
                    .password(host.getPassword())
                    .authenticator(new BasicAuthenticator().preemptive())
                    .connectionTimeout(getConnectionTimeout())
                    .build();
        } else {
            client = J4pClient.
                    url(host.getHost())
                    .connectionTimeout(getConnectionTimeout())
                    .build();
        }
        return client;
    }

    private static int getConnectionTimeout() {
        int connectionTimeout = JolokiaRegistry.findConnectionTimeout();
        if (connectionTimeout == -1) {
            connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
        }
        LOGGER.info("Using connection timeout: {} ms", connectionTimeout);
        return connectionTimeout;
    }

}
