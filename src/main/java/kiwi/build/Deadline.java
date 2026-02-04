/**
 * A task with a specific deadline.
 *
 * Extends {@link Task} to represent deadline-type tasks that have a specific due date.
 *
 * The deadline is stored as a {@link LocalDateTime} parsed from input in the format
 * "yyyy-MM-dd HHmm" (e.g., "2026-02-04 2359").
 *
 * @author zow1e
 * @see Task
 * @see Event
 * @see ToDo
 */
package kiwi.build;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task with a specific due date.
 */
public class Deadline extends Task {

    /**
     * Constructs a Deadline task with the given description and due date string.
     *
     * @param description the task description (non-empty)
     * @param dateTime the due date in "yyyy-MM-dd HHmm" format (e.g., "2026-02-04 2359")
     */
    public Deadline(String description, String dateTime) {
        super(description);
        this.dateTime = LocalDateTime.parse(dateTime,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
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
     * @return formatted string of the deadline task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + dateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy HHmm")) + ")";
    }
}
