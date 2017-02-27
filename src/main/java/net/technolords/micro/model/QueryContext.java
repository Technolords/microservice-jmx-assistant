package net.technolords.micro.model;

import java.util.List;

import net.technolords.micro.model.jaxb.Host;
import net.technolords.micro.model.jaxb.JolokiaQuery;

public class QueryContext {
    private List<Host> hosts;
    private List<JolokiaQuery> queries;

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }

    public List<JolokiaQuery> getQueries() {
        return queries;
    }

    public void setQueries(List<JolokiaQuery> queries) {
        this.queries = queries;
    }
}
