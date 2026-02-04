/**
 * Represents a generic task managed by the Kiwi task manager.
 *
 * Provides the base structure and common operations for all task types (ToDo, Deadline, Event).
 * Subclasses extend this class to add type-specific fields and behavior.
 *
 * The task has a description, done status, and optional date/time information.
 *
 * Example usage:
 * Task task = new Task("read book");
 * task.markTask();
 * System.out.println(task);  // [X] read book
 *
 * @author [zow1e]
 * @see Deadline
 * @see Event
 * @see ToDo
 */
package kiwi.build;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
     * Initializes the task as not done (isDone = false) and dateTime as null.
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
     * Returns "X" if the task is done, " " (space) if not done.
     *
     * @return "X" for done tasks, " " for undone tasks
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks this task as done.
     *
     * Sets the isDone field to true.
     */
    public void markTask() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     *
     * Sets the isDone field to false.
     */
    public void unmarkTask() {
        this.isDone = false;
    }

    /**
     * Returns the date/time associated with this task.
     *
     * Returns null for tasks without date/time (e.g., ToDo tasks).
     *
     * @return the LocalDateTime of the task, or null if none
     */
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    /**
     * Returns a formatted string representation of the task's date/time.
     *
     * Returns empty string if no dateTime is set. Uses format "MMM d yyyy HHmm".
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
     * Format: [status] description
     *
     * Example: [ ] read book  or  [X] read book
     *
     * @return formatted display string of the task
     */
    @Override
    public String toString() {
        String fullDesc = "[" + getStatusIcon() + "] " + description;
        return fullDesc;
    }
}
