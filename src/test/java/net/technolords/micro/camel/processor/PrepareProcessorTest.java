package net.technolords.micro.camel.processor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;

public class PrepareProcessorTest {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Test
    public void testWithJaywayLibrary() throws IOException {
        LOGGER.info("About to test with Jayway");
        final String pathToJsonFile = "src/test/resources/json/fuse-containers.json";
        Path path = FileSystems.getDefault().getPath(pathToJsonFile);
        LOGGER.info("File exist: {}", Files.exists(path));
        final String jsonPath = "$.value[*].jolokiaUrl";
        List<String> jolokiaUrls = JsonPath.read(Files.newInputStream(path, StandardOpenOption.READ), jsonPath);
        LOGGER.info("Found matches: {}", jolokiaUrls.size());
        for (String match : jolokiaUrls) {
            LOGGER.info("Match: {}", match);
        }
    }
}