package net.technolords.micro.camel.expression;

import java.util.List;

import org.apache.camel.Handler;
import org.apache.camel.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.model.QueryContext;
import net.technolords.micro.model.jaxb.JolokiaQuery;

public class QuerySplitter {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Handler
    public List<JolokiaQuery> splitByQuery(@Header(value = JolokiaMain.HEADER_QUERY_CONTEXT) QueryContext queryContext) {
        LOGGER.debug("Splitter by JolokiaQuery called, header reference: {}", queryContext);
        return queryContext.getQueries();
    }
}
