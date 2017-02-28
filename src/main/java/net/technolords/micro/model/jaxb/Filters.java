package net.technolords.micro.model.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Filters {
    private List<Filter> filters;

    public Filters() {
        this.filters = new ArrayList<>();
    }

    @XmlElement (name = "filter")
    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
