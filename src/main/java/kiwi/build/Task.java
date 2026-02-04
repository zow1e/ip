/**
 * Represents a generic task managed by the Kiwi task manager.
 *
 * @author zow1e
 * @see Deadline
 * @see Event
 * @see ToDo
 */
package kiwi.build;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generic task class with description and completion status.
 */
public class Task {

    /** The description of the task. */
    protected String description;

    /** The completion status of the task. */
    protected boolean isDone;

    /** The date/time associated with the task (nullable). */
    protected LocalDateTime dateTime;

    /**
     * Constructs a Task with the given description.
     *
     * @param description the task description (non-empty string)
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return the task description string
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the status icon representing the task's completion status.
     *
     * @return "X" for done tasks, " " for undone tasks
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as done.
     */
    public void markTask() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void unmarkTask() {
        this.isDone = false;
    }

    /**
     * Returns the date/time associated with this task.
     *
     * @return the LocalDateTime of the task, or null if none
     */
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    /**
     * Returns a formatted string representation of the task's date/time.
     *
     * @return formatted date/time string, or empty string if no date/time
     */
    public String getDateTimeString() {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy HHmm"));
    }

    /**
     * Returns a string representation of this task for display.
     *
     * @return formatted display string of the task
     */
    @Override
    public String toString() {
        String fullDesc = "[" + getStatusIcon() + "] " + description;
        return fullDesc;
    }
}
