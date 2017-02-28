package net.technolords.micro.model.jaxb;

import javax.xml.bind.annotation.XmlValue;

public class Filter {
    private String filter;

    @XmlValue
    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
