package org.example.externals.http;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.InboundHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;

public class InboundHttpResponseHeaders implements InboundHeaders {
    // This example wraps the Apache HTTP client response object
    private final CloseableHttpResponse response;

    // InboundMessageWrapper is implemented by delegating to the library's request object
    public InboundHttpResponseHeaders(CloseableHttpResponse response) {
        this.response = response;
    }

    @Override
    public HeaderType getHeaderType() {
        return HeaderType.HTTP;
    }

    // this allows the agent to read the correct headers from the HTTP response
    @Override
    public String getHeader(String name) {
        return response.getFirstHeader(name).getValue();
    }

}
