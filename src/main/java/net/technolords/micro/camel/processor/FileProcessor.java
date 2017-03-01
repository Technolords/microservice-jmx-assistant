package net.technolords.micro.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jolokia.client.request.J4pResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.model.jaxb.Output;
import net.technolords.micro.model.jaxb.output.FileOutput;

public class FileProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String APPEND = "append";

    @Override
    public void process(Exchange exchange) throws Exception {
        Output output = exchange.getProperty(JolokiaMain.PROPERTY_OUTPUT, Output.class);
        J4pResponse response = exchange.getIn().getHeader(JolokiaMain.HEADER_RESPONSE, J4pResponse.class);
        this.writeToFile(output.getFileOutput(), response);
    }

    private void writeToFile(FileOutput fileOutput, J4pResponse response) {
        if (response != null) {
            LOGGER.info("Got output, about to write to file...");
//            if (fileOutput)
        }
    }

    /*
        Path path = FileSystems.getDefault().getPath(pathToJolokiaConfig);
        Files.newInputStream(path, StandardOpenOption.READ);
        LOGGER.info("Response: {}", response.asJSONObject().toJSONString());
     */
}
