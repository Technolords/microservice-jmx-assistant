package net.technolords.micro.camel.route;

import org.apache.camel.builder.AdviceWithRouteBuilder;

/**
 * This class substitutes for fragments like:
 *
 *      routeDefinition.adviceWith(super.context(), new AdviceWithRouteBuilder() {
 *          @Override
 *          public void configure() throws Exception {
 *              super.weaveById(OutputRoute.MARKER_FOR_REDIS).replace().to(MOCK_REDIS_OUTPUT);
 *          }
 *      });
 */
public class ReplacedEndpointAdviceRoute extends AdviceWithRouteBuilder {
    private String adviceReferenceId;
    private String replaceEndpointWith;

    public ReplacedEndpointAdviceRoute(String adviceReferenceId, String replaceEndpointWith) {
        this.adviceReferenceId = adviceReferenceId;
        this.replaceEndpointWith = replaceEndpointWith;
    }

    @Override
    public void configure() throws Exception {
        super.weaveById(this.adviceReferenceId)
                .replace()
                .to(this.replaceEndpointWith);
    }
}
