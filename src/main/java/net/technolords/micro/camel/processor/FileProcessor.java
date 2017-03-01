package net.technolords.micro.camel.processor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jolokia.client.request.J4pResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.model.jaxb.Output;
import net.technolords.micro.model.jaxb.output.FileOutput;

/**
 * The processor keeps state when the last timer occurred. Reason is that
 * when the option to create a new file it means all the data for the
 * entire 'run' needs to be collected. If we do not keep state, every
 * response will be written in a new file and as result we would loose
 * data.
 */
public class FileProcessor implements Processor {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String APPEND = "append";
    private Long lastCounter;

    @Override
    public void process(Exchange exchange) throws Exception {
        Output output = exchange.getProperty(JolokiaMain.PROPERTY_OUTPUT, Output.class);
        J4pResponse response = exchange.getIn().getHeader(JolokiaMain.HEADER_RESPONSE, J4pResponse.class);
        this.writeToFile(output.getFileOutput(), response, exchange);
    }

    private void writeToFile(FileOutput fileOutput, J4pResponse response, Exchange exchange) throws IOException {
        if (response != null) {
            Path path = FileSystems.getDefault().getPath(fileOutput.getFile());
            String output = response.asJSONObject().toJSONString() + "\n";
            LOGGER.info("Got output, about to write to file: {} -> exist: {}", path.toString(), Files.exists(path));
            if (APPEND.equals(fileOutput.getMode())) {
                // Append
                Files.write(path, output.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                // New file, but only when it is a new 'session'. The latter is determined by checking the
                // Exchange.TIMER_COUNTER
                long currentCounter = exchange.getProperty(Exchange.TIMER_COUNTER, Long.class);
                if (this.lastCounter == null || currentCounter > lastCounter.longValue()) {
                    this.lastCounter = currentCounter;
                    Files.write(path, output.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    LOGGER.info("New file (current counter: {}) -> yes", this.lastCounter);
                } else {
                    Files.write(path, output.getBytes(), StandardOpenOption.APPEND);
                    LOGGER.info("New file (current counter: {}) -> no", this.lastCounter);
                }
            }
        }
    }

}
