/**
 * A task that occurs at a specific time range (from-to).
 *
 * Extends {@link.Task} to represent event-type tasks that have a specific time range.
 *
 * The event is stored with {@link.LocalDateTime} objects for both start time (fromTime)
 * and end time (toTime), parsed from input in the format "yyyy-MM-dd HHmm".
 *
 * The dateTime field (inherited from Task) is set to the start time (fromTime) for
 * compatibility with other task types.
 *
 * Example usage:
 * Event meeting = new Event("team meeting", "2026-02-05 1400", "2026-02-05 1530");
 * System.out.println(meeting);  // [E][ ] team meeting (at: Feb 5 2026 1400 - 1530)
 *
 * @author [zow1e]
 * @see Task
 * @see Deadline
 * @see ToDo
 */
package kiwi.build;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    /** The start time of the event. */
    private LocalDateTime fromTime;

    /** The end time of the event. */
    private LocalDateTime toTime;

    /**
     * Constructs an Event task with the given description and time range.
     *
     * Parses both from and to date strings using the pattern "yyyy-MM-dd HHmm".
     * Sets the inherited dateTime field to the start time (fromTime).
     * Throws DateTimeParseException if either time format is invalid.
     *
     * @param description the event description (non-empty)
     * @param from the start time in "yyyy-MM-dd HHmm" format (e.g., "2026-02-05 1400")
     * @param to the end time in "yyyy-MM-dd HHmm" format (e.g., "2026-02-05 1530")
     * @throws java.time.format.DateTimeParseException if from or to format is invalid
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
     * Format: [E][status] description (at: MMM d yyyy HHmm - HHmm)
     *
     * Example: [E][X] team meeting (at: Feb 5 2026 1400 - 1530)
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
