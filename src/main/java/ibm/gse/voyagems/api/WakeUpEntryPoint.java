package ibm.gse.voyagems.api;

import ibm.gse.voyagems.jms.FreezerRequestListener;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/api/v1/orders")
@Produces(MediaType.APPLICATION_JSON)
public class WakeUpEntryPoint {

    static final Logger logger = LoggerFactory.getLogger(WakeUpEntryPoint.class);

    @Inject
    FreezerRequestListener freezerRequestListener;

    public WakeUpEntryPoint() { }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Request to create an order", description = "")
    @APIResponses(value = {
            @APIResponse(responseCode = "400", description = "Function could not wake up", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "200", description = "Function waked up", content = @Content(mediaType = "application/json")) })
    public Response createShippingOrder(String createOrderParameters) throws Exception {

    }
}
