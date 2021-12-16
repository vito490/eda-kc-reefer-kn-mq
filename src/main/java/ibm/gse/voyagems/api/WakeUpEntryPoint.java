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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class WakeUpEntryPoint {

    static final Logger logger = LoggerFactory.getLogger(WakeUpEntryPoint.class);

    @Inject
    FreezerRequestListener freezerRequestListener;

    private ExecutorService scheduler;

    public WakeUpEntryPoint() { }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Request to create an order", description = "")
    @APIResponses(value = {
            @APIResponse(responseCode = "500", description = "Function could not wake up", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "200", description = "Function waked up", content = @Content(mediaType = "application/json")) })
    public Response createShippingOrder() {

        try {
            if(scheduler != null) {
                logger.info("Consumer already scheduled");
                return Response.ok().build();
            }
            scheduler = Executors.newSingleThreadExecutor();
            scheduler.submit(freezerRequestListener);
            logger.info("Refeer consumer scehduled..");
            return Response.ok().build();
        } catch (Exception e) {
            logger.error("Could not schedule consumer..", e);
            return Response.serverError().build();
        }

    }
}
