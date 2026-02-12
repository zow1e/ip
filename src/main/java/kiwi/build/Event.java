/**
 * A task that occurs at a specific time range (from-to).
 *
 * Extends {@link Task} to represent event-type tasks with a start and end time.
 * 
 * Times can be specified in "yyyy-MM-dd HHmm" format (e.g., "2026-02-04 1400")
 * or time-only "HHmm" format (e.g., "1400"), which assumes today's date.
 *
 * @author zow1e
 * @see Task
 * @see Deadline
 * @see ToDo
 */
package kiwi.build;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an event task with a specific time range.
 */
public class Event extends Task {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /** The start time of the event. */
    private LocalDateTime fromTime;

    /** The end time of the event. */
    private LocalDateTime toTime;


    /**
     * Constructs an Event task with the given description and time range.
     *
     * @param description the event description (non-empty)
     * @param from the start time in "yyyy-MM-dd HHmm" format or "HHmm" format
     *             (e.g., "2026-02-04 1400" or "1400")
     * @param to the end time in "yyyy-MM-dd HHmm" format or "HHmm" format
     *           (e.g., "2026-02-04 1600" or "1600")
     * @throws IllegalArgumentException if date/time format is invalid or end time is before start time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.fromTime = parseDateTime(from.trim(), "event /from");
        this.toTime = parseDateTime(to.trim(), "event /to");

        if (this.toTime.isBefore(this.fromTime)) {
            throw new IllegalArgumentException("End time cannot be before start time!");
        }
    }

    /**
     * Parses date/time string in either "yyyy-MM-dd HHmm" or "HHmm" format.
     *
     * @param dateTimeStr the date/time string to parse
     * @param context the context for error messages (e.g., "event /from")
     * @return parsed LocalDateTime
     * @throws IllegalArgumentException if format is invalid
     */
    private static LocalDateTime parseDateTime(String dateTimeStr, String context) {
        try {
            // Try time-only format (HHmm)
            if (dateTimeStr.length() == 4 && dateTimeStr.matches("\\d{4}")) {
                String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return LocalDateTime.parse(todayStr + " " + dateTimeStr, DATE_TIME_FORMATTER);
            }

            // Try full datetime format (yyyy-MM-dd HHmm)
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid " + context + ": " + dateTimeStr
                + "\nUse: yyyy-MM-dd HHmm (e.g., 2026-02-15 1400)\n"
                + "Or:  HHmm (e.g., 1400)");
        }
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
        String date = fromTime.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        String fromStr = fromTime.format(DateTimeFormatter.ofPattern("HHmm"));
        String toStr = toTime.format(DateTimeFormatter.ofPattern("HHmm"));
        return "[E]" + super.toString() + " (at: " + date + " " + fromStr + " - " + toStr + ")";
    }
}
