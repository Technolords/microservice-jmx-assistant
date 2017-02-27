package net.technolords.micro.camel.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.model.ModelManager;
import net.technolords.micro.model.QueryContext;
import net.technolords.micro.model.jaxb.Host;
import net.technolords.micro.model.jaxb.JolokiaConfiguration;
import net.technolords.micro.model.jaxb.JolokiaQuery;
import net.technolords.micro.model.jaxb.Output;
import net.technolords.micro.registry.JolokiaRegistry;

public class PrepareQueryContextProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private ModelManager modelManager;

    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.debug("PrepareQueryContextProcessor called...");
        if (this.modelManager == null) {
            this.modelManager = JolokiaRegistry.findModelManager();
        }
        exchange.setProperty(JolokiaMain.PROPERTY_OUTPUT, this.findOutput());
        exchange.getIn().setHeader(JolokiaMain.HEADER_QUERY_CONTEXT, this.createQueryContext(this.findHosts()));
    }

    private QueryContext createQueryContext(List<Host> hosts) {
        QueryContext queryContext = null;
        if (hosts != null && hosts.size() > 0) {
            queryContext = new QueryContext();
            queryContext.setHosts(hosts);
            queryContext.setQueries(this.findQueries());
        }
        return queryContext;
    }

    private Output findOutput() {
        Output output = this.modelManager.getJolokiaConfiguration().getOutput();
        return output;
    }

    private List<Host> findHosts() {
        List<Host> hosts = new ArrayList<>();
        JolokiaQuery jolokiaQuery = this.hasParentQuery();
        if (jolokiaQuery != null) {
            // TODO: Execute query
            // TODO: Parse result -> list of hosts
//            hosts.add()
        } else {
            Host host = this.findAssociatedHost();
            LOGGER.trace("Host found: {}", host.getHost());
            hosts.add(host);
        }
        LOGGER.debug("Total hosts: {}", hosts.size());
        return hosts;
    }

    private List<JolokiaQuery> findQueries() {
        List<JolokiaQuery> queries = this.modelManager.getJolokiaConfiguration().getJolokiaQueries();
        LOGGER.debug("Total queries: {}", queries.size());
        return queries;
    }

    private JolokiaQuery hasParentQuery() {
        return null;
    }

    private Host findAssociatedHost() {
        if (modelManager != null) {
            JolokiaConfiguration jolokiaConfiguration = modelManager.getJolokiaConfiguration();
            return jolokiaConfiguration
                    .getJolokiaQueries()
                    .stream()
                    .findFirst()
                    .map(JolokiaQuery::getHost)
                    .get();
        }
        return null;
    }

}
