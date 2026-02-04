/**
 * A task with a specific deadline.
 *
 * Extends {@link.Task} to represent deadline-type tasks that have a specific due date.
 *
 * The deadline is stored as a {@link.LocalDateTime} parsed from input in the format
 * "yyyy-MM-dd HHmm" (e.g., "2026-02-04 2359").
 *
 * Example usage:
 * Deadline dl = new Deadline("submit report", "2026-02-04 2359");
 * System.out.println(dl);  // [D][ ] submit report (by: Feb 4 2026 2359)
 *
 * @author [zow1e]
 * @see Task
 * @see Event
 * @see ToDo
 */
package kiwi.build;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


public class Deadline extends Task {

    /**
     * Constructs a Deadline task with the given description and due date string.
     *
     * Parses the dateTime string using the pattern "yyyy-MM-dd HHmm" and stores
     * it as a LocalDateTime object. Throws a DateTimeParseException if the format
     * is invalid.
     *
     * @param description the task description (non-empty)
     * @param dateTime the due date in "yyyy-MM-dd HHmm" format (e.g., "2026-02-04 2359")
     * @throws java.time.format.DateTimeParseException if dateTime format is invalid
     */
    public Deadline(String description, String dateTime) {
        super(description);
        this.dateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }

    /**
     * Returns the due date of this Deadline task.
     *
     * @return the LocalDateTime representing the due date
     */
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    /**
     * Returns a string representation of this Deadline task.
     *
     * Format: [D][status] description (by: MMM d yyyy HHmm)
     *
     * Example: [D][X] submit report (by: Feb 4 2026 2359)
     *
     * @return formatted string of the deadline task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " +
               dateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy HHmm")) + ")";
    }
}
