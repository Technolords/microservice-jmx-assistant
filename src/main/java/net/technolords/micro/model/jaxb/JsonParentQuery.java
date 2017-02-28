package net.technolords.micro.model.jaxb;

import javax.xml.bind.annotation.XmlElement;

public class JsonParentQuery {
    private JsonPath jsonPath;

    @XmlElement (name = "json-path")
    public JsonPath getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(JsonPath jsonPath) {
        this.jsonPath = jsonPath;
    }
}
