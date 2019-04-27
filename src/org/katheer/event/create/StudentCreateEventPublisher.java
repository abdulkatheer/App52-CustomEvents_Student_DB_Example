package org.katheer.event.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public class StudentCreateEventPublisher {
    @Autowired
    public ApplicationEventPublisher publisher;

    public void publish(String message) {
        StudentCreateEvent createEvent = new StudentCreateEvent(this, message);
        publisher.publishEvent(createEvent);
    }
}
