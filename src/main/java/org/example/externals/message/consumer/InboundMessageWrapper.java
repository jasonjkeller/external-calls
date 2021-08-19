package org.example.externals.message.consumer;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.InboundHeaders;

import javax.jms.JMSException;
import javax.jms.Message;

public class InboundMessageWrapper implements InboundHeaders {
    // This example wraps the JMS Message object
    private final Message message;

    // InboundMessageWrapper is implemented by delegating to the library's message object
    public InboundMessageWrapper(Message message) {
        this.message = message;
    }

    @Override
    public HeaderType getHeaderType() {
        return HeaderType.MESSAGE;
    }

    @Override
    public String getHeader(String name) {
        String stringProperty = "";
        try {
            stringProperty = message.getStringProperty(name);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return stringProperty;
    }
}
