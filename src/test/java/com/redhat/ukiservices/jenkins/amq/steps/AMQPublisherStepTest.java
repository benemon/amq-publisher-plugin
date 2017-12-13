/*
 * Copyright 2017, Ben Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.ukiservices.jenkins.amq.steps;

import com.redhat.ukiservices.jenkins.amq.configuration.AMQPublisherPluginConfig;
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.model.Statement;
import org.jvnet.hudson.test.RestartableJenkinsRule;
import org.jvnet.hudson.test.recipes.LocalData;

public class AMQPublisherStepTest {

    @Rule
    public RestartableJenkinsRule story = new RestartableJenkinsRule();

    @Rule
    public EmbeddedActiveMQResource artemis = new EmbeddedActiveMQResource("activemq/default/broker.xml");

    @Test
    @LocalData("default")
    public void testBasicProducer() throws Exception {

        story.addStep(new Statement() {
            @Override
            public void evaluate() throws Throwable {

                WorkflowJob job = story.j.jenkins.createProject(WorkflowJob.class, "testJob" + System.currentTimeMillis());

                job.setDefinition(new CpsFlowDefinition("amqPublisher(message: 'Hello Queue!')", false));

                WorkflowRun build = story.j.assertBuildStatusSuccess(job.scheduleBuild2(0));

                story.j.assertBuildStatusSuccess(build);
            }
        });
    }

    public void testCustomDestinationProducer() throws Exception {

        story.addStep(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                WorkflowJob job = story.j.jenkins.createProject(WorkflowJob.class, "testJob" + System.currentTimeMillis());

                job.setDefinition(new CpsFlowDefinition("amqPublisher(destination: 'topic/jenkinsTopic', message: 'Hello Topic!')", false));

                WorkflowRun build = story.j.assertBuildStatusSuccess(job.scheduleBuild2(0));

                story.j.assertBuildStatusSuccess(build);
            }
        });

    }

}
