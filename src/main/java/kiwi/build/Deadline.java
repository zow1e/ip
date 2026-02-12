/**
 * A task with a specific deadline.
 *
 * Extends {@link Task} to represent deadline-type tasks that have a specific due date.
 *
 * The deadline is stored as a {@link LocalDateTime} parsed from input in the format
 * "yyyy-MM-dd HHmm" (e.g., "2026-02-04 2359") or time-only "HHmm" (e.g., "2359").
 *
 * @author zow1e
 * @see Task
 * @see Event
 * @see ToDo
 */
package kiwi.build;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a deadline task with a specific due date.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Constructs a Deadline task with the given description and due date string.
     *
     * @param description the task description (non-empty)
     * @param dateTime the due date in "yyyy-MM-dd HHmm" format or "HHmm" format
     *                 (e.g., "2026-02-04 2359" or "2359")
     * @throws IllegalArgumentException if the date/time format is invalid
     */
    public Deadline(String description, String dateTime) {
        super(description);
        this.dateTime = parseDateTime(dateTime.trim());
    }

    /**
     * Parses date/time string in either "yyyy-MM-dd HHmm" or "HHmm" format.
     *
     * @param dateTimeStr the date/time string to parse
     * @return parsed LocalDateTime
     * @throws IllegalArgumentException if format is invalid
     */
    private static LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            // Try time-only format (HHmm)
            if (dateTimeStr.length() == 4 && dateTimeStr.matches("\\d{4}")) {
                String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return LocalDateTime.parse(todayStr + " " + dateTimeStr, DATE_TIME_FORMATTER);
            }

            // Try full datetime format (yyyy-MM-dd HHmm)
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid deadline format: " + dateTimeStr
                + "\nUse: yyyy-MM-dd HHmm (e.g., 2026-02-15 2359)\n"
                + "Or:  HHmm (e.g., 2359)");
        }
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
