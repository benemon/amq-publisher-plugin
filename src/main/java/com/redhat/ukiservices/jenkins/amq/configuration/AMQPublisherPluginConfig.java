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

package com.redhat.ukiservices.jenkins.amq.configuration;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public class AMQPublisherPluginConfig extends GlobalConfiguration {

    /**
     * A comma separated list of AMQ hosts (hostname:port)
     */
    private String hosts;

    /**
     * A string for the default destination for published payloads
     */
    private String defaultDestination;

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getDefaultDestination() {
        return defaultDestination;
    }

    public void setDefaultDestination(String defaultDestination) {
        this.defaultDestination = defaultDestination;
    }

    /**
     * Default Constructor
     */
    public AMQPublisherPluginConfig() {
        load();
    }

    /**
     * Get the current Kafka Global Configuration
     *
     * @return the Kafka Configuration, or {@code null} if Jenkins has been shut down
     */
    public static AMQPublisherPluginConfig get() {
        Jenkins j = Jenkins.getInstance();
        AMQPublisherPluginConfig conf = j.getDescriptorByType(AMQPublisherPluginConfig.class);
        if (conf != null) {
            return conf;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this, json);
        save();
        return true;
    }
}
