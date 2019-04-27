package org.katheer.event.create;

import org.springframework.context.ApplicationListener;

public class StudentCreateEventListener implements ApplicationListener<StudentCreateEvent> {
    @Override
    public void onApplicationEvent(StudentCreateEvent createEvent) {
        createEvent.generateLog();
    }
}