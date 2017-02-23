package net.technolords.micro.model.jaxb;

import javax.xml.bind.annotation.XmlValue;

public class Operation {
    private String operation;

    @XmlValue
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
