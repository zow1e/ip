/**
 * A task that occurs at a specific time range (from-to).
 *
 * @author zow1e
 * @see Task
 * @see Deadline
 * @see ToDo
 */
package kiwi.build;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with a specific time range.
 */
public class Event extends Task {

    /** The start time of the event. */
    private LocalDateTime fromTime;

    /** The end time of the event. */
    private LocalDateTime toTime;

    /**
     * Constructs an Event task with the given description and time range.
     *
     * @param description the event description (non-empty)
     * @param from the start time in "yyyy-MM-dd HHmm" format
     * @param to the end time in "yyyy-MM-dd HHmm" format
     */
    public Event(String description, String from, String to) {
        super(description);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        this.fromTime = LocalDateTime.parse(from, fmt);
        this.toTime = LocalDateTime.parse(to, fmt);
        this.dateTime = fromTime;
    }

    /**
     * Returns the start time of this Event.
     *
     * @return the LocalDateTime representing the start time
     */
    public LocalDateTime getFrom() {
        return fromTime;
    }

    /**
     * Returns the end time of this Event.
     *
     * @return the LocalDateTime representing the end time
     */
    public LocalDateTime getTo() {
        return toTime;
    }

    /**
     * Returns a string representation of this Event task.
     *
     * @return formatted string of the event task
     */
    @Override
    public String toString() {
        String date = dateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        String fromStr = fromTime.format(DateTimeFormatter.ofPattern("HHmm"));
        String toStr = toTime.format(DateTimeFormatter.ofPattern("HHmm"));
        return "[E]" + super.toString() + " (at: " + date + " " + fromStr + " - " + toStr + ")";
    }
}
