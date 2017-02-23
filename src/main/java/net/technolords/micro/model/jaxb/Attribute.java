package net.technolords.micro.model.jaxb;

import javax.xml.bind.annotation.XmlValue;

public class Attribute {
    private String attribute;

    @XmlValue
    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
