# New Relic Java agent external call API usage

This project demonstrates usage of all the New Relic Java agent external call APIs.
* Datastore
* Generic
* HTTP
* Message Producer
* Message Consumer

# Requirements

* Java 11
* Download the latest [New Relic Java agent jar and yaml config file](http://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/)

# Usage

The `ExternalCallExamples` class will run a `while` loop that repeatedly makes external calls that are reported to New Relic
via the Java agent APIs when the Java agent is attached. If the Java agent is not attached then the APIs will simply no-op.

Attach the Java agent with the `-javaagent:/full/path/to/newrelic.jar` JVM option. This can be done via your IDE run 
configuration or by building the jar `./gradlew clean build` and passing the agent option via the command line: 

```shell
java -javaagent:/full/path/to/newrelic.jar -jar build/libs/external-calls-1.0-SNAPSHOT.jar
```

Also, make sure that the Java agent yaml config file sits alongside the agent jar and is correctly configured with an `app_name` and `license_key`.