# AMQ Publisher Plugin for Jenkins

### What is it?

A basic step for Jenkins which allows a user to publish bespoke information from a Pipeline to a given AMQ destination.

### Usage

* Requires Minimum Jenkins version of 2.73.x *

`mvn clean package`

Install the resulting package as a plugin in your Jenkins instance.

Configure using Global System Configuration. Options for the following are provided:

- Broker Hosts - Comma separated list of the form host:port
- Default Destination - Where to send the traffic if no explicit destination is configured in the step

To publish, simply add a step with the following syntax:

`amqPublisher ( destination: <queue/topic>, properties: [:], message: <body> )`

The `destination` parameter is optional, and if not provided, will publish payloads to the com.redhat.ukiservices.jenkins.amq.steps.AMQPublisherStep.default destination, if defined.
