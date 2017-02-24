package net.technolords.micro.model.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "jolokia", namespace = "http://xsd.technolords.net")
public class JolokiaConfiguration {
    private Output output;
    private List<JolokiaQuery> jolokiaQueries;

    @XmlElement (name = "output")
    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    @XmlElement (name = "query")
    public List<JolokiaQuery> getJolokiaQueries() {
        return jolokiaQueries;
    }

    public void setJolokiaQueries(List<JolokiaQuery> jolokiaQueries) {
        this.jolokiaQueries = jolokiaQueries;
    }
}
