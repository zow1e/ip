/**
 * ToDo class. 
 * Child class for Event.
 * Initializes template format for a ToDo object.
 */

package kiwi.build;
public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
