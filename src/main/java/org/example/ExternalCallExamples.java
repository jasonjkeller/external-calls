package org.example;

import org.example.externals.datastore.DatastoreExternal;
import org.example.externals.generic.GenericExternal;
import org.example.externals.http.HttpExternal;
import org.example.externals.message.consumer.MessageConsumerExternal;
import org.example.externals.message.producer.MessageProducerExternal;

public class ExternalCallExamples {
    public static void main(String[] args) {
        while (true) {
            // Message producer externals
            MessageProducerExternal.sendMessageTraced();
            MessageProducerExternal.sendMessageUntraced();

            // Message consumer externals
            MessageConsumerExternal.receiveMessageTraced();
            MessageConsumerExternal.receiveMessageUntraced();

            // Datastore externals
            DatastoreExternal.reportDatastoreQueryTraced();
            DatastoreExternal.reportDatastoreQueryUntraced();

            // Datastore slow query externals
            DatastoreExternal.reportDatastoreSlowQueryTraced();
            DatastoreExternal.reportDatastoreSlowQueryUntraced();

            // Generic externals
            GenericExternal.reportGenericExternalTraced();
            GenericExternal.reportGenericExternalUntraced();

            // HTTP externals
            HttpExternal.reportExternalTraced();
            HttpExternal.reportExternalUntraced();
        }
    }
}
