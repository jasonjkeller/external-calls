package org.example.externals.message.producer;

import javax.jms.Destination;

public class FakeDestination implements Destination {
    private String name;

    public String getDestinationName() {
        return name;
    }

    public void setDestinationName(String name) {
        this.name = name;
    }
}
