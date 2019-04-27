package org.katheer.event.search;

import org.springframework.context.ApplicationListener;

public class StudentSearchEventListener implements ApplicationListener<StudentSearchEvent> {
    @Override
    public void onApplicationEvent(StudentSearchEvent searchEvent) {
        searchEvent.generateLog();
    }
}
