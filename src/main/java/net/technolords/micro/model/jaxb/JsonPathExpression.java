package net.technolords.micro.model.jaxb;

import javax.xml.bind.annotation.XmlValue;

public class JsonPathExpression {
    private String expression;

    @XmlValue
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
