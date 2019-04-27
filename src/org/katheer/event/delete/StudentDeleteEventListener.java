package org.katheer.event.delete;

import org.springframework.context.ApplicationListener;

public class StudentDeleteEventListener implements ApplicationListener<StudentDeleteEvent> {
    @Override
    public void onApplicationEvent(StudentDeleteEvent deleteEvent) {
        deleteEvent.generateLog();
    }
}
