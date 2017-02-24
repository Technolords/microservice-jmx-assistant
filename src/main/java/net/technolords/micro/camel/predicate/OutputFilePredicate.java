package net.technolords.micro.camel.predicate;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.model.jaxb.Output;

public class OutputFilePredicate implements Predicate {

    @Override
    public boolean matches(Exchange exchange) {
        Output output = exchange.getProperty(JolokiaMain.PROPERTY_OUTPUT, Output.class);
        if (output != null) {
            if (output.getFileOutput() != null) {
                return true;
            }
        }
        return false;
    }
}
