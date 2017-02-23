package net.technolords.micro.model.jaxb;

import javax.xml.bind.annotation.XmlValue;

public class ObjectName {
    private String objectName;

    @XmlValue
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
