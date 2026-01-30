package kiwi.build;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    
    protected String description;
    protected boolean isDone;
    protected LocalDateTime dateTime;

    // constructor
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    // display task status
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    // mark as done 
    public void markTask() {
        this.isDone = true;
    }

    // mark as not done 
    public void unmarkTask() {
        this.isDone = false;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public String getDateTimeString() {
        if (dateTime == null) return "";
        return dateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy HHmm"));
    }
    
    // display description
    @Override
    public String toString() {
        String fullDesc = "[" + getStatusIcon() + "] " + description.toString();
        return fullDesc;
    }
}
