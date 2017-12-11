package com.redhat.ukiservices.jenkins.amq.steps;

import hudson.Extension;
import hudson.model.TaskListener;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.workflow.steps.*;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class AMQPublisherStep extends Step {

    private static Logger log = Logger.getLogger(AMQPublisherStep.class.getName());

    private final String topic;

    private final Map<String, String> payload;

    @DataBoundConstructor
    public AMQPublisherStep(String topic, Map<String, String> payload) {
        this.payload = payload;
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    @Override
    public StepExecution start(StepContext stepContext) throws Exception {
        return new Execution(topic, payload, stepContext);
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
            return "AMQ Publisher";
        }
    }

    public static class Execution extends SynchronousStepExecution<Void> {

        private static final long serialVersionUID = 1L;
        private transient final String topic;
        private transient final Map<String, String> payload;

        Execution(String topic, Map<String, String> payload, StepContext context) {
            super(context);

            this.topic = topic;
            this.payload = payload;
        }

        @Override
        protected Void run() throws Exception {

            JSONObject publishedContent = new JSONObject();
            publishedContent.putAll(payload);

            return null;
        }

    }
}
