package org.katheer.event.delete;

import org.springframework.context.ApplicationEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class StudentDeleteEvent extends ApplicationEvent {
    private String message;

    public StudentDeleteEvent(Object source, String message) {
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
                    new Date().toString() + " : DELETE -- " + this.message +
                            "\n";
            byte[] log = logMessage.getBytes();
            fos.write(log);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
