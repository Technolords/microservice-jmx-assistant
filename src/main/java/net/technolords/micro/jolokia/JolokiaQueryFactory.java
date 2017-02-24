package net.technolords.micro.jolokia;

import javax.management.MalformedObjectNameException;

import org.jolokia.client.request.J4pExecRequest;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pRequest;

import net.technolords.micro.model.jaxb.Attribute;
import net.technolords.micro.model.jaxb.Attributes;
import net.technolords.micro.model.jaxb.JolokiaQuery;
import net.technolords.micro.model.jaxb.ObjectName;
import net.technolords.micro.model.jaxb.Operation;

public class JolokiaQueryFactory {

    public static J4pRequest findJolokiaRequest(JolokiaQuery jolokiaQuery) throws MalformedObjectNameException {
        J4pRequest request;
        if (jolokiaQuery.getOperation() != null) {
            request = createExecRequest(jolokiaQuery);
        } else {
            request = createReadRequest(jolokiaQuery);
        }
        return request;
    }

    private static J4pRequest createReadRequest(JolokiaQuery jolokiaQuery) throws MalformedObjectNameException {
        ObjectName objectName = jolokiaQuery.getObjectName();
        Attribute attribute = getAttribute(jolokiaQuery.getAttributes());
        J4pReadRequest readRequest = new J4pReadRequest(objectName.getObjectName(),attribute.getAttribute());
        return readRequest;
    }

    private static J4pRequest createExecRequest(JolokiaQuery jolokiaQuery) throws MalformedObjectNameException {
        ObjectName objectName = jolokiaQuery.getObjectName();
        Operation operation = jolokiaQuery.getOperation();
        J4pExecRequest request = new J4pExecRequest(objectName.getObjectName(), operation.getOperation());
        return request;
    }

    private static Attribute getAttribute(Attributes attributes) {
        for (Attribute attribute : attributes.getAttributes()) {
            return attribute;
        }
        return null;
    }
}
