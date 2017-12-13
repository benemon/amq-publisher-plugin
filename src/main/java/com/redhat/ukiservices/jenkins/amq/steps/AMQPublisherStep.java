package com.redhat.ukiservices.jenkins.amq.steps;

import hudson.Extension;
import hudson.model.TaskListener;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.jenkinsci.plugins.workflow.steps.*;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import javax.jms.*;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static com.redhat.ukiservices.jenkins.amq.configuration.AMQPublisherPluginConfig.get;

public class AMQPublisherStep extends Step {

    private static Logger log = Logger.getLogger(AMQPublisherStep.class.getName());

    private final String destination;

    private final Map<String, Object> properties;

    private final String message;

    @DataBoundConstructor
    public AMQPublisherStep(String destination, Map<String, Object> properties, String message) {
        this.destination = destination;
        this.properties = properties;
        this.message = message;
    }

    public String getDestination() {
        return destination;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public StepExecution start(StepContext stepContext) throws Exception {
        return new Execution(destination, properties, message, stepContext);
    }

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return Collections.singleton(TaskListener.class);
        }

        @Override
        public String getFunctionName() {
            return "amqPublisher";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "AMQ Publisher Step";
        }
    }

    public static class Execution extends SynchronousStepExecution<Void> {

        private static final long serialVersionUID = 1L;
        private transient final String destination;
        private transient final Map<String, Object> properties;
        private transient final String message;

        Execution(String destination, Map<String, Object> properties, String message, StepContext context) {
            super(context);

            this.destination = destination;
            this.properties = properties;
            this.message = message;

        }

        @Override
        protected Void run() throws Exception {

            // Create a ConnectionFactory
            ConnectionFactory connectionFactory = new JmsConnectionFactory(get().getConnectionString());

            // try-with-resources for the actual connection
            try (
                    Connection connection = connectionFactory.createConnection();
                    Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            ) {
                Queue queue = session.createQueue(destination != null ? destination : get().getDefaultDestination());
                MessageProducer producer = session.createProducer(queue);
                TextMessage txtMsg = session.createTextMessage(this.message);

                // Convert any properties in map form to message properties
                if (properties != null) {
                    for (String key : properties.keySet()) {
                        Object value = properties.get(key);
                        txtMsg.setObjectProperty(key, value);
                    }
                }

                producer.send(txtMsg);
            }

            return null;
        }

    }
}
