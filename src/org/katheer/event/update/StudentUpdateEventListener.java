package org.katheer.event.update;

import org.springframework.context.ApplicationListener;

public class StudentUpdateEventListener implements ApplicationListener<StudentUpdateEvent> {
    @Override
    public void onApplicationEvent(StudentUpdateEvent updateEvent) {
        updateEvent.generateLog();
    }
}
