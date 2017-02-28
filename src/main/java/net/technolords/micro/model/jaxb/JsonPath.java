package net.technolords.micro.model.jaxb;

import javax.xml.bind.annotation.XmlElement;

public class JsonPath {
    private JsonPathExpression jsonPathExpression;
    private Filters filters;

    @XmlElement (name = "expression")
    public JsonPathExpression getJsonPathExpression() {
        return jsonPathExpression;
    }

    public void setJsonPathExpression(JsonPathExpression jsonPathExpression) {
        this.jsonPathExpression = jsonPathExpression;
    }

    @XmlElement(name = "filters")
    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }
}
