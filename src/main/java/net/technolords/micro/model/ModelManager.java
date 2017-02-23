package net.technolords.micro.model;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import net.technolords.micro.model.jaxb.Jolokia;

public class ModelManager {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String PATH_TO_SCHEMA_FILE = "xsd/jolokia.xsd";
    private Jolokia jolokiaConfiguration;

    public ModelManager(String pathToJolokiaConfig) throws IOException, JAXBException, SAXException {
        InputStream inputStreamForValidation, inputStreamForConfig; // Streams can be read only once
        LOGGER.info("Using configuration file: {}", pathToJolokiaConfig);
        Path path = FileSystems.getDefault().getPath(pathToJolokiaConfig);
        LOGGER.info("File exist: {}", Files.exists(path));
        // Set input stream to a resource located on file system (read only)
        inputStreamForValidation = Files.newInputStream(path, StandardOpenOption.READ);
        inputStreamForConfig = Files.newInputStream(path, StandardOpenOption.READ);
        // Validate configuration file
        this.validateConfigurationFile(inputStreamForValidation);
        // Initialize configuration
        this.initializeConfiguration(inputStreamForConfig);
    }

    private void validateConfigurationFile(InputStream inputStream) throws IOException, SAXException {
        LOGGER.info("About to validate the configuration...");
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source xsdSource = new StreamSource(this.getClass().getClassLoader().getResourceAsStream(PATH_TO_SCHEMA_FILE));
        Schema schema = schemaFactory.newSchema(xsdSource);
        Validator validator = schema.newValidator();
        Source sourceToConfig = new StreamSource(inputStream);
        validator.validate(sourceToConfig);
        LOGGER.info("... valid, proceeding...");
    }

    protected void initializeConfiguration(InputStream inputStream) throws JAXBException {
        LOGGER.info("About to initialize the configuration...");
        Unmarshaller unmarshaller = JAXBContext.newInstance(Jolokia.class).createUnmarshaller();
        this.jolokiaConfiguration = (Jolokia) unmarshaller.unmarshal(inputStream);
        LOGGER.info("... done");
//        LOGGER.debug("Total loaded resources: {}", this.configurations.getConfigurations().size());
//        LOGGER.info("... done, URL mappings parsed [{} for POST, {} for GET]", this.postConfigurations.size(), this.getConfigurations.size());
    }
}
