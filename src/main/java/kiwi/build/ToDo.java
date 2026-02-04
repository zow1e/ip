/**
 * A task without any specific date or time associated with it.
 *
 * Extends {@link.Task} to represent to-do type tasks (simplest task type).
 *
 * Does not have date/time information, unlike {@link.Deadline} and {@link.Event}.
 *
 * Example usage:
 * ToDo todo = new ToDo("buy groceries");
 * todo.markTask();
 * System.out.println(todo);  // [T][X] buy groceries
 *
 * @author [zow1e]
 * @see Task
 * @see Deadline
 * @see Event
 */
package kiwi.build;

public class ToDo extends Task {

    /**
     * Constructs a ToDo task with the given description.
     *
     * Initializes as not done with no date/time (dateTime remains null).
     *
     * @param description the task description (non-empty string)
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of this ToDo task.
     *
     * Format: [T][status] description
     *
     * Example: [T][ ] buy groceries  or  [T][X] buy groceries
     *
     * @return formatted display string prefixed with "[T]"
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
