/**
 * A task without any specific date or time associated with it.
 *
 * @author zow1e
 * @see Task
 * @see Deadline
 * @see Event
 */
package kiwi.build;

/**
 * ToDo task without date/time.
 */
public class ToDo extends Task {

    /**
     * Constructs a ToDo task with the given description.
     *
     * @param description the task description (non-empty string)
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of this ToDo task.
     *
     * @return formatted display string prefixed with "[T]"
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
