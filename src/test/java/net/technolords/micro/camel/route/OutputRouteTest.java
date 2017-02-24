package net.technolords.micro.camel.route;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.testng.CamelTestSupport;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import net.technolords.micro.camel.JolokiaMain;
import net.technolords.micro.model.jaxb.Output;
import net.technolords.micro.model.jaxb.output.FileOutput;
import net.technolords.micro.model.jaxb.output.LogOutput;
import net.technolords.micro.model.jaxb.output.RedisOutput;

public class OutputRouteTest extends CamelTestSupport {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String MOCK_FILE_OUTPUT = "mock:fileOutput";
    private static final String MOCK_REDIS_OUTPUT = "mock:redisOutput";
    private static final String MOCK_LOG_OUTPUT = "mock:logOutput";

    @EndpointInject(uri = MOCK_FILE_OUTPUT)
    private MockEndpoint mockFile;

    @EndpointInject(uri = MOCK_REDIS_OUTPUT)
    private MockEndpoint mockRedis;

    @EndpointInject(uri = MOCK_LOG_OUTPUT)
    private MockEndpoint mockLog;

    @Test
    public void testSetup() {
        Assert.assertNotNull(this.mockFile);
        Assert.assertNotNull(this.mockRedis);
        Assert.assertNotNull(this.mockLog);
    }

    @Test
    public void testRouteForFile() throws Exception {
        LOGGER.info("About to test route for file output...");
        // Prepare
        RouteDefinition routeDefinition = super.context().getRouteDefinition(OutputRoute.ROUTE_ID);
        LOGGER.debug("RouteDefinition: {}", routeDefinition);
        Assert.assertNotNull(routeDefinition);
        routeDefinition.adviceWith(super.context(), new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                super.weaveById(OutputRoute.MARKER_FOR_FILE).replace().to(MOCK_FILE_OUTPUT);
            }
        });
        this.setExpectedMessageCount(1, 0, 0);
        // Test
        ProducerTemplate producerTemplate = super.context().createProducerTemplate();
        producerTemplate.request(OutputRoute.ROUTE_ENDPOINT, exchange -> {
            Output output = new Output();
            output.setFileOutput(new FileOutput());
            exchange.setProperty(JolokiaMain.PROPERTY_OUTPUT, output);
        });
        // Validate
        this.assertOnMockedEndpoints();
    }

    @Test
    public void testRouteForRedis() throws Exception {
        LOGGER.info("About to test route for redis output...");
        // Prepare
        RouteDefinition routeDefinition = super.context().getRouteDefinition(OutputRoute.ROUTE_ID);
        LOGGER.debug("RouteDefinition: {}", routeDefinition);
        Assert.assertNotNull(routeDefinition);
        routeDefinition.adviceWith(super.context(), new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                super.weaveById(OutputRoute.MARKER_FOR_REDIS).replace().to(MOCK_REDIS_OUTPUT);
            }
        });
        this.setExpectedMessageCount(0, 1, 0);
        // Test
        ProducerTemplate producerTemplate = super.context().createProducerTemplate();
        producerTemplate.request(OutputRoute.ROUTE_ENDPOINT, exchange -> {
            Output output = new Output();
            output.setRedisOutput(new RedisOutput());
            exchange.setProperty(JolokiaMain.PROPERTY_OUTPUT, output);
        });
        // Validate
        this.assertOnMockedEndpoints();
    }

    @Test
    public void testRouteForLog() throws Exception {
        LOGGER.info("About to test route for log output...");
        // Prepare
        RouteDefinition routeDefinition = super.context().getRouteDefinition(OutputRoute.ROUTE_ID);
        LOGGER.debug("RouteDefinition: {}", routeDefinition);
        Assert.assertNotNull(routeDefinition);
        routeDefinition.adviceWith(super.context(), new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                super.weaveById(OutputRoute.MARKER_FOR_LOG).replace().to(MOCK_LOG_OUTPUT);
            }
        });
        this.setExpectedMessageCount(0, 0, 1);
        // Test
        ProducerTemplate producerTemplate = super.context().createProducerTemplate();
        producerTemplate.request(OutputRoute.ROUTE_ENDPOINT, exchange -> {
            Output output = new Output();
            output.setLogOutput(new LogOutput());
            exchange.setProperty(JolokiaMain.PROPERTY_OUTPUT, output);
        });
        // Validate
        this.assertOnMockedEndpoints();
    }

    @Test
    public void testRouteForDefault() throws Exception {
        LOGGER.info("About to test route for default output...");
        // Prepare
        RouteDefinition routeDefinition = super.context().getRouteDefinition(OutputRoute.ROUTE_ID);
        LOGGER.debug("RouteDefinition: {}", routeDefinition);
        Assert.assertNotNull(routeDefinition);
        routeDefinition.adviceWith(super.context(), new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                super.weaveById(OutputRoute.MARKER_FOR_LOG).replace().to(MOCK_LOG_OUTPUT);
            }
        });
        this.setExpectedMessageCount(0, 0, 1);
        // Test
        ProducerTemplate producerTemplate = super.context().createProducerTemplate();
        producerTemplate.request(OutputRoute.ROUTE_ENDPOINT, exchange -> {
            // Nothing to set, as it is default
        });
        // Validate
        this.assertOnMockedEndpoints();
    }

    private void setExpectedMessageCount(int mockFileCount, int mockRedisCount, int mockLogCount) {
        this.mockFile.expectedMessageCount(mockFileCount);
        this.mockRedis.expectedMessageCount(mockRedisCount);
        this.mockLog.expectedMessageCount(mockLogCount);
    }

    private void assertOnMockedEndpoints() throws InterruptedException {
        this.mockFile.assertIsSatisfied();
        this.mockFile.reset();
        this.mockRedis.assertIsSatisfied();
        this.mockRedis.reset();
        this.mockLog.assertIsSatisfied();
        this.mockLog.reset();
    }

    @Override
    public RouteBuilder[] createRouteBuilders() {
        OutputRoute outputRoute = new OutputRoute();
        return new RouteBuilder[] {
                outputRoute,
        };
    }
}