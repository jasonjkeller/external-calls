package org.example.externals.message.producer;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.OutboundHeaders;

import javax.jms.JMSException;
import javax.jms.Message;

public class OutboundMessageWrapper implements OutboundHeaders {
    // This example wraps the JMS Message object
    private final Message message;

    // OutboundHeaders is implemented by delegating to the library's message object
    public OutboundMessageWrapper(Message message) {
        this.message = message;
    }

    // This allows the agent to add the correct headers to the HTTP request
    @Override
    public void setHeader(String name, String value) {
        try {
            message.setStringProperty(name, value);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    // New Relic CAT specifies different header names for HTTP and MESSAGE
    @Override
    public HeaderType getHeaderType() {
        return HeaderType.MESSAGE;
    }
}

