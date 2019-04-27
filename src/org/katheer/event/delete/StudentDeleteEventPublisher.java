package org.katheer.event.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public class StudentDeleteEventPublisher {
    @Autowired
    ApplicationEventPublisher publisher;

    public void publish(String message) {
        StudentDeleteEvent deleteEvent = new StudentDeleteEvent(this, message);
        publisher.publishEvent(deleteEvent);
    }
}
