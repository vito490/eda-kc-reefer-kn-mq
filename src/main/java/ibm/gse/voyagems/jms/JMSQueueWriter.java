package ibm.gse.voyagems.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Session;

/**
 A bean that sends generic serializable payload to JMS queue using qpid APIs
 */

@ApplicationScoped
public class JMSQueueWriter<T> {

    @Inject
    ConnectionFactory connectionFactory;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(JMSQueueWriter.class);

    public boolean sendMessage(final T message, final String queueName) throws Exception {
        try (JMSContext context = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            context.createProducer().send(
                    context.createQueue(queueName), mapper.writeValueAsString(message));
            logger.info("Message sent on queue " + queueName);
            return true;
        } catch (Exception e) {
            logger.error("MANAGE ME", e);
            return false;
        }
    }
}
