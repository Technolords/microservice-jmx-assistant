package net.technolords.micro.model.jaxb;

import javax.xml.bind.annotation.XmlElement;

public class JsonParentQuery {
    private JolokiaQuery jolokiaQuery;
    private JsonPath jsonPath;

    @XmlElement (name = "query")
    public JolokiaQuery getJolokiaQuery() {
        return jolokiaQuery;
    }

    public void setJolokiaQuery(JolokiaQuery jolokiaQuery) {
        this.jolokiaQuery = jolokiaQuery;
    }

    @XmlElement (name = "json-path")
    public JsonPath getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(JsonPath jsonPath) {
        this.jsonPath = jsonPath;
    }
}
