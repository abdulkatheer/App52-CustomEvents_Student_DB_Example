package org.katheer.event.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public class StudentSearchEventPublisher {
    @Autowired
    ApplicationEventPublisher publisher;

    public void publish(String message) {
        StudentSearchEvent searchEvent = new StudentSearchEvent(this, message);
        publisher.publishEvent(searchEvent);
    }
}
