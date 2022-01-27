package ibm.gse.voyagems.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import ibm.gse.voyagems.domain.model.EventBase;
import ibm.gse.voyagems.domain.model.freezer.FreezerAllocatedEvent;
import ibm.gse.voyagems.domain.model.freezer.FreezerAllocatedPayload;
import ibm.gse.voyagems.domain.model.freezer.FreezerNotFoundEvent;
import ibm.gse.voyagems.domain.model.freezer.FreezerNotFoundPayload;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.core.json.JsonObject;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A bean consuming prices from the JMS queue.
 */
@ApplicationScoped
public class FreezerRequestListener implements Runnable {

    @Inject
    ConnectionFactory connectionFactory;

    @Inject
    JMSQueueWriter<EventBase> jmsQueueWriter;


    private final ExecutorService scheduler = Executors.newSingleThreadExecutor();

    private static final Logger log = Logger.getLogger(FreezerRequestListener.class);

    void onStart(@Observes StartupEvent ev) {
        scheduler.submit(this);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        log.info("Connecting to message queue " + System.getenv("FREEZER_REQUEST_QUEUE"));
        try (JMSContext context = connectionFactory.createContext(Session.CLIENT_ACKNOWLEDGE)) {
            javax.jms.JMSConsumer consumer = context.createConsumer(
                    context.createQueue(System.getenv("FREEZER_REQUEST_QUEUE")));
            while (true) {
                Message message = consumer.receive();
                if (message == null) {
                    return;
                }
                log.info("received message from queue... " + message.getBody(String.class));
                EventBase responseEvent = processMessage(message.getBody(String.class));
                boolean messageSent = false;
                try {
                    messageSent = jmsQueueWriter.sendMessage(responseEvent, System.getenv("FREEZER_RESPONSE_QUEUE"));
                } catch (Exception e) {
                    log.error("Could not send response message, rolling back...", e);
                }
                if(messageSent) {
                    message.acknowledge();
                }
            }
        } catch (Exception e) {
            log.error("error parsing message..", e);
        }
    }

    public EventBase processMessage(String rawMessageBody) throws InterruptedException {

        try {

            ObjectMapper mapper = new ObjectMapper();
            log.debug("received message from queue... " + rawMessageBody);
            JsonObject rawEvent = new JsonObject(rawMessageBody);

            String productId = null;
            if((productId = rawEvent.getJsonObject("payload").getString("productId")) != null) {
                if(productId.equals("REFEER_FAILS")) {

                    Thread.sleep(3000);

                    String orederId = rawEvent.getJsonObject("payload").getString("orderID");

                    FreezerNotFoundPayload freezerNotFoundPayload = new FreezerNotFoundPayload(orederId,
                            "Container not found");
                    return new FreezerNotFoundEvent(System.currentTimeMillis(), "1.0",
                            orederId, freezerNotFoundPayload);
                }
            }

            if (rawEvent.getString("type").equals(EventBase.TYPE_VOYAGE_ASSIGNED)) {

                Thread.sleep(3000);

                UUID freezerId = UUID.randomUUID();
                log.debug("Generated new voyage ID: " + freezerId);
                String orederId = rawEvent.getJsonObject("payload").getString("orderID");
                FreezerAllocatedPayload freezerAllocatedPayload =
                        new FreezerAllocatedPayload(orederId, freezerId.toString());
                return new FreezerAllocatedEvent(System.currentTimeMillis(), "1.0", orederId,
                        freezerAllocatedPayload);

            } else if(rawEvent.getString("type").equals(EventBase.TYPE_VOYAGE_NOT_FOUND)) {

                Thread.sleep(3000);

                String orederId = rawEvent.getJsonObject("payload").getString("orderID");

                FreezerNotFoundPayload freezerNotFoundPayload = new FreezerNotFoundPayload(orederId,
                        "Voyage not found, rolling back container schedule");
                return new FreezerNotFoundEvent(System.currentTimeMillis(), "1.0",
                        orederId, freezerNotFoundPayload);

            } else {
                throw new RuntimeException("Could not verify payload type");
            }
        } catch (Exception e) {
            log.error("error processing message...", e);
            throw e;
        }
    }
}
