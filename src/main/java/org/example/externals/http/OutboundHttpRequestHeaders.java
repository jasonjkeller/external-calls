package org.example.externals.http;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.OutboundHeaders;
import org.apache.http.client.methods.HttpUriRequest;

public class OutboundHttpRequestHeaders implements OutboundHeaders {
    // This example wraps the Apache HTTP client request object
    private final HttpUriRequest request;

    // OutboundHeaders is implemented by delegating to the library's request object
    public OutboundHttpRequestHeaders(HttpUriRequest request) {
        this.request = request;
    }

    // New Relic CAT specifies different header names for HTTP and MESSAGE
    @Override
    public HeaderType getHeaderType() {
        return HeaderType.HTTP;
    }

    // This allows the agent to add the correct headers to the HTTP request
    @Override
    public void setHeader(String name, String value) {
        request.addHeader(name, value);
    }

}

