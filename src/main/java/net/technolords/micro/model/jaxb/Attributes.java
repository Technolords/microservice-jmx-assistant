package net.technolords.micro.model.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Attributes {
    private List<Attribute> attributes;

    @XmlElement(name = "attribute")
    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }
}
