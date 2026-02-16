/**
 * A task that occurs at a specific time range (from-to).
 *
 * Extends {@link Task} to represent event-type tasks with a start and end time.
 *
 * Times can be specified in "yyyy-MM-dd HHmm" format (e.g., "2026-02-04 1400")
 * or time-only "HHmm" format (e.g., "1400"), which assumes the same date as /from.
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

import kiwi.helper.KiwiException;

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
     * @throws KiwiException if date/time format is invalid or end time is before start time
     */
    public Event(String description, String from, String to) throws KiwiException {
        super(description);
        try {
            this.fromTime = parseDateTime(from.trim());
            this.toTime = parseEventToTime(to.trim(), this.fromTime);

            if (this.toTime.isBefore(this.fromTime)) {
                throw new KiwiException("End time cannot be before start time!!");
            }
        } catch (IllegalArgumentException e) {
            throw new KiwiException(e.getMessage());
        }
    }

    /**
     * Parses date/time string in either "yyyy-MM-dd HHmm" or "HHmm" format.
     *
     * @param dateTimeStr the date/time string to parse
     * @return parsed LocalDateTime
     * @throws KiwiException if format is invalid
     */
    private static LocalDateTime parseDateTime(String dateTimeStr) throws KiwiException {
        try {
            // Try time-only format (HHmm)
            if (dateTimeStr.length() == 4 && dateTimeStr.matches("\\d{4}")) {
                String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return LocalDateTime.parse(todayStr + " " + dateTimeStr, DATE_TIME_FORMATTER);
            }

            // Try full datetime format (yyyy-MM-dd HHmm)
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new KiwiException("Invalid event start time: '" + dateTimeStr + "'\n"
                + "Use: yyyy-MM-dd HHmm (e.g., 2026-02-15 1400)\n"
                + "Or:  HHmm (e.g., 1400)");
        }
    }

    /**
     * Parses the /to time, using the /from date if /to is time-only.
     *
     * This allows: event meeting /from 2026-03-07 1030 /to 1600
     * The 1600 will be interpreted as 2026-03-07 1600 (same date as /from).
     *
     * @param toTimeStr the /to time string
     * @param fromTime the parsed /from time (used to extract date for time-only /to)
     * @return parsed LocalDateTime for the end time
     * @throws IllegalArgumentException if format is invalid
     */
    private static LocalDateTime parseEventToTime(String toTimeStr, LocalDateTime fromTime) {
        try {
            // Try time-only format (HHmm)
            if (toTimeStr.length() == 4 && toTimeStr.matches("\\d{4}")) {
                String fromDateStr = fromTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return LocalDateTime.parse(fromDateStr + " " + toTimeStr, DATE_TIME_FORMATTER);
            }

            // Try full datetime format (yyyy-MM-dd HHmm)
            return LocalDateTime.parse(toTimeStr, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid event end time: '" + toTimeStr + "'\n"
                + "Use: yyyy-MM-dd HHmm (e.g., 2026-02-15 1600)\n"
                + "Or:  HHmm (e.g., 1600)");
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
