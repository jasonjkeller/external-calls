package org.example.externals.message.producer;

import com.newrelic.api.agent.DestinationType;
import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.MessageProduceParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

public class MessageProducerExternal {
    public static final String MESSAGE_NAME = "message-producer-name";
    public static final String MESSAGE_VALUE = "message-producer-value";

    // start a transaction when this method is called
    @Trace(dispatcher = true)
    public static void sendMessageTraced() {
        NewRelic.setTransactionName("Custom", "MessageProducerExternal/sendMessageTraced");
        sendMessage(FakeMessage.createMessage(MESSAGE_NAME, MESSAGE_VALUE));
    }

    // Do not start a transaction when this method is called
    public static void sendMessageUntraced() {
        sendMessage(FakeMessage.createMessage(MESSAGE_NAME, MESSAGE_VALUE));
    }

    // instrument the method that puts messages on a queue, this will be linked to a transaction if one exists
    @Trace
    private static void sendMessage(Message message) {
        try {
            Destination jmsDestination = message.getJMSDestination();
            String destinationName = ((FakeDestination) jmsDestination).getDestinationName();
            ExternalParameters messageProduceParameters =
                    MessageProduceParameters.library("JMS")
                            .destinationType(DestinationType.NAMED_QUEUE)
                            .destinationName(destinationName)
                            .outboundHeaders(new OutboundMessageWrapper(message))
                            .build();

            NewRelic.getAgent().getTracedMethod().reportAsExternal(messageProduceParameters);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
