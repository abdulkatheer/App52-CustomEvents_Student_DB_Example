package org.katheer.event.update;

import org.springframework.context.ApplicationEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class StudentUpdateEvent extends ApplicationEvent {
    private String message;

    public StudentUpdateEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public void generateLog() {
        try {
            File file = new File("F:\\logs\\applicationLogs.txt");
            file.getParentFile().mkdir();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);

            String logMessage =
                    new Date().toString() + " : UPDATE -- " + this.message +
                            "\n";
            byte[] log = logMessage.getBytes();
            fos.write(log);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
