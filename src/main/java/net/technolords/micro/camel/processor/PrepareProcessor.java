package net.technolords.micro.camel.processor;

import java.util.ArrayList;
import java.util.List;

import javax.management.MalformedObjectNameException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pRequest;
import org.jolokia.client.request.J4pResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.jolokia.JolokiaClientFactory;
import net.technolords.micro.jolokia.JolokiaQueryFactory;
import net.technolords.micro.model.ModelManager;
import net.technolords.micro.model.QueryContext;
import net.technolords.micro.model.jaxb.Host;
import net.technolords.micro.model.jaxb.JolokiaConfiguration;
import net.technolords.micro.model.jaxb.JolokiaQuery;
import net.technolords.micro.model.jaxb.JsonParentQuery;
import net.technolords.micro.model.jaxb.JsonPath;
import net.technolords.micro.model.jaxb.Output;
import net.technolords.micro.registry.JolokiaRegistry;

public class PrepareProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private ModelManager modelManager;

    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.debug("PrepareProcessor called...");
        if (this.modelManager == null) {
            this.modelManager = JolokiaRegistry.findModelManager();
        }
        exchange.setProperty(JolokiaMain.PROPERTY_OUTPUT, this.findOutput());
        QueryContext queryContext = this.createQueryContext(this.findHosts());
        exchange.getIn().setHeader(JolokiaMain.HEADER_QUERY_CONTEXT, queryContext);
        LOGGER.info("Preparation done -> Total hosts: {}, total queries: {}", queryContext.getHosts().size(), queryContext.getQueries().size());
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
        return this.modelManager.getJolokiaConfiguration().getOutput();
    }

    private List<Host> findHosts() {
        List<Host> hosts = new ArrayList<>();
        JsonParentQuery jsonParentQuery = this.hasParentQuery();
        if (jsonParentQuery != null) {
            JolokiaQuery jolokiaQuery = jsonParentQuery.getJolokiaQuery();
            try {
                // Execute query
                Host parentHost = jolokiaQuery.getHost();
                J4pClient client = JolokiaClientFactory.findJolokiaClient(parentHost);
                J4pRequest request = JolokiaQueryFactory.findJolokiaRequest(jolokiaQuery);
                J4pResponse response = client.execute(request);
                JsonPath jsonPath = jsonParentQuery.getJsonPath();
                String json = response.asJSONObject().toJSONString();
                if (jsonPath != null) {
                    // Refine answer, i.e. parse JsonPath
                    List<String> matches = com.jayway.jsonpath.JsonPath.read(json, jsonPath.getJsonPathExpression().getExpression());
                    for (String match : matches) {
                        // TODO: implement filter
                        hosts.add(this.createHost(match, parentHost.getUsername(), parentHost.getPassword()));
                    }
                } else {
                    // Answer is host
                    hosts.add(this.createHost(json, parentHost.getUsername(), parentHost.getPassword()));
                }
            } catch (MalformedObjectNameException | J4pException e) {
                LOGGER.error("Unable to execute parent query", e);
            }
        } else {
            Host host = this.findAssociatedHost();
            if (host != null) {
                LOGGER.trace("Host found: {}", host.getHost());
                hosts.add(host);
            }
        }
        LOGGER.debug("Total hosts: {}", hosts.size());
        return hosts;
    }

    private Host createHost(String host, String username, String password) {
        Host newHost = new Host();
        newHost.setHost(host);
        newHost.setUsername(username);
        newHost.setPassword(password);
        return newHost;
    }

    private List<JolokiaQuery> findQueries() {
        List<JolokiaQuery> queries = this.modelManager.getJolokiaConfiguration().getJolokiaQueries();
        LOGGER.debug("Total queries: {}", queries.size());
        return queries;
    }

    private JsonParentQuery hasParentQuery() {
        return this.modelManager.getJolokiaConfiguration().getJsonParentQuery();
    }

    private Host findAssociatedHost() {
        if (modelManager != null) {
            JolokiaConfiguration jolokiaConfiguration = modelManager.getJolokiaConfiguration();
            return jolokiaConfiguration
                    .getJolokiaQueries()
                    .stream()
                    .findFirst()
                    .map(JolokiaQuery::getHost).orElseGet(null);
        }
        return null;
    }

}
