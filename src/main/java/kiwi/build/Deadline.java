/**
 * Deadline class. 
 * Child class of Event for deadline events.
 * Initializes template format for an Deadline object with due date.
 */

package kiwi.build;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    
    public Deadline(String description, String dateTime) {
        super(description);
        this.dateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern
                                                       ("yyyy-MM-dd HHmm"));
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + dateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy HHmm")) + ")";
    }

}
