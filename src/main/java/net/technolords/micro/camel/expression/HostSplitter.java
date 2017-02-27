package net.technolords.micro.camel.expression;

import java.util.List;

import org.apache.camel.Handler;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.model.QueryContext;
import net.technolords.micro.model.jaxb.Host;

public class HostSplitter {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Handler
    public List<Host> splitByHost(@Header(value = JolokiaMain.HEADER_QUERY_CONTEXT) QueryContext queryContext) {
        LOGGER.debug("Splitter by Host called, header reference: {}", queryContext);
        return queryContext.getHosts();
    }

}
