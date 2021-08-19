package org.example.externals.message.consumer;

import com.newrelic.api.agent.DestinationType;
import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.MessageConsumeParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import org.example.externals.message.producer.FakeDestination;
import org.example.externals.message.producer.FakeMessage;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

public class MessageConsumerExternal {
    public static final String MESSAGE_NAME = "message-consumer-name";
    public static final String MESSAGE_VALUE = "message-consumer-value";

    // start a transaction when this method is called
    @Trace(dispatcher = true)
    public static void receiveMessageTraced() {
        NewRelic.setTransactionName("Custom", "MessageConsumerExternal/receiveMessageTraced");
        receiveMessage(FakeMessage.createMessage(MESSAGE_NAME, MESSAGE_VALUE));
    }

    // Do not start a transaction when this method is called
    public static void receiveMessageUntraced() {
        receiveMessage(FakeMessage.createMessage(MESSAGE_NAME, MESSAGE_VALUE));
    }

    @Trace
    private static void receiveMessage(Message message) {
        Destination jmsDestination;
        String destinationName = null;
        try {
            jmsDestination = message.getJMSDestination();
            destinationName = ((FakeDestination) jmsDestination).getDestinationName();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        ExternalParameters messageConsumeParameters =
                MessageConsumeParameters.library("JMS")
                        .destinationType(DestinationType.NAMED_QUEUE)
                        .destinationName(destinationName)
                        .inboundHeaders(new InboundMessageWrapper(message))
                        .build();

        NewRelic.getAgent().getTracedMethod().reportAsExternal(messageConsumeParameters);
    }
}
