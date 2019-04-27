package org.katheer.event.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public class StudentUpdateEventPublisher {
    @Autowired
    ApplicationEventPublisher publisher;

    public void publish(String message) {
        StudentUpdateEvent updateEvent = new StudentUpdateEvent(this, message);
        publisher.publishEvent(updateEvent);
    }
}
