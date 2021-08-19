package org.example.externals.http;

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.HttpParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;

public class HttpExternal {
    public static final String LIBRARY = "HTTPClient";
    public static final String EXTERNAL_URI = "http://example.com/";
    public static final String PROCEDURE = "Execute";

    // start a transaction when this method is called
    @Trace(dispatcher = true)
    public static void reportExternalTraced() {
        NewRelic.setTransactionName("Custom", "HttpExternal/reportExternalTraced");
        try {
            reportExternal(EXTERNAL_URI);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Do not start a transaction when this method is called
    public static void reportExternalUntraced() {
        try {
            reportExternal(EXTERNAL_URI);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Trace
    private static int reportExternal(String uri) throws IOException {
        // TODO change this to unsupported HTTP client Java 11 maybe?
        HttpUriRequest request = RequestBuilder.get().setUri(EXTERNAL_URI).build();

        // Wrap the outbound Request object
        OutboundHttpRequestHeaders outboundHeaders = new OutboundHttpRequestHeaders(request);

        // Obtain a reference to the method currently being traced
        TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
        // Add headers for outbound external request
        tracedMethod.addOutboundRequestHeaders(outboundHeaders);

        CloseableHttpClient connection = HttpClientBuilder.create().build();
        CloseableHttpResponse response = connection.execute(request);

        // Wrap the incoming Response object
        InboundHttpResponseHeaders inboundHeaders = new InboundHttpResponseHeaders(response);

        // Create an input parameter object for a call to an external HTTP service
        ExternalParameters params = HttpParameters
                .library(LIBRARY)
                .uri(URI.create(uri))
                .procedure(PROCEDURE)
                .inboundHeaders(inboundHeaders)
                .build();

        // Report a call to an external HTTP service
        tracedMethod.reportAsExternal(params);

        return response.getStatusLine().getStatusCode();
    }

}
