package net.technolords.micro.model;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import net.technolords.micro.model.jaxb.JolokiaConfiguration;

public class ModelManagerTest {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String DATA_SET_FOR_CONFIGURATIONS = "dataSetForConfigurations";

    @DataProvider(name = DATA_SET_FOR_CONFIGURATIONS)
    public Object[][] dataSetMock() {
        return new Object[][]{
                {"src/test/resources/xml/config-1.xml"},
                {"src/test/resources/xml/config-2.xml"},
        };
    }

    @Test (dataProvider = DATA_SET_FOR_CONFIGURATIONS)
    public void testValidationOfDifferentScenarios(final String path) throws JAXBException, IOException, SAXException {
        LOGGER.info("About to validate scenario with file: {}", path);
        ModelManager modelManager = new ModelManager(path);
        Assert.assertNotNull(modelManager);
        JolokiaConfiguration jolokia = modelManager.getJolokiaConfiguration();
        Assert.assertNotNull(jolokia);
    }

}