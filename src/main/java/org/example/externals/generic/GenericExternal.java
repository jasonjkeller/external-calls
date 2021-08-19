package org.example.externals.generic;

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.GenericParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

import java.net.URI;

public class GenericExternal {
    public static final String LIBRARY = "generic-library";
    public static final String EXTERNAL_URI = "http://example.org/";
    public static final String PROCEDURE = "GET";

    // start a transaction when this method is called
    @Trace(dispatcher = true)
    public static void reportGenericExternalTraced() {
        NewRelic.setTransactionName("Custom", "GenericExternal/reportGenericExternalTraced");
        reportGenericExternal(EXTERNAL_URI);
    }

    // Do not start a transaction when this method is called
    public static void reportGenericExternalUntraced() {
        reportGenericExternal(EXTERNAL_URI);
    }

    @Trace
    private static void reportGenericExternal(String uri) {
        ExternalParameters genericParameters = GenericParameters
                .library(LIBRARY)
                .uri(URI.create(uri))
                .procedure(PROCEDURE)
                .build();

        NewRelic.getAgent().getTracedMethod().reportAsExternal(genericParameters);
    }

}
