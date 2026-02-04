/**
 * Manages a collection of tasks for the Kiwi task manager.
 * 
 * Encapsulates all task list operations: add, delete, mark/unmark, find, and list.
 * Uses 1-based indexing for user-facing operations (delete 1 = remove first task).
 * 
 * Example usage:
 * <pre>{@code
 * TaskList tasks = new TaskList();
 * tasks.add(new ToDo("buy milk"));
 * Task marked = tasks.mark(1);  // marks first task as done
 * ArrayList&lt;Task&gt; found = tasks.find("milk");  // returns matching tasks
 * }</pre>
 * 
 * @author [zow1e]
 * @see Task
 * @see kiwi.build.ToDo
 * @see kiwi.build.Deadline
 * @see kiwi.build.Event
 */
package kiwi.helper;

import java.util.ArrayList;

import kiwi.build.Task;

public class TaskList {
    /** The underlying ArrayList storing all tasks. */
    private ArrayList<Task> tasks;

    /**
     * Creates an empty TaskList.
     * 
     * Initializes with empty ArrayList for new task collections.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList initialized with existing tasks.
     * 
     * Used when loading tasks from storage.
     * 
     * @param tasks existing ArrayList of tasks to manage
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the end of the task list.
     * 
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes the task at the specified 1-based index.
     * 
     * Index 1 = first task, index 2 = second task, etc.
     * 
     * @param index 1-based index of task to delete
     * @return the deleted Task object
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task delete(int index) {
        return tasks.remove(index - 1);
    }

    /**
     * Marks the task at the specified 1-based index as done.
     * 
     * @param index 1-based index of task to mark
     * @return the marked Task object
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task mark(int index) {
        Task t = tasks.get(index - 1);
        t.markTask();
        return t;
    }

    /**
     * Unmarks the task at the specified 1-based index (sets as not done).
     * 
     * @param index 1-based index of task to unmark
     * @return the unmarked Task object
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Task unmark(int index) {
        Task t = tasks.get(index - 1);
        t.unmarkTask();
        return t;
    }

    /**
     * Returns the complete list of tasks.
     * 
     * @return ArrayList containing all tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns the total number of tasks in the list.
     * 
     * @return number of tasks (0 if empty)
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Finds all tasks whose description contains the given keyword.
     * 
     * Case-insensitive search. Returns empty list if no matches.
     * 
     * @param keyword search keyword (trimmed, case-insensitive)
     * @return ArrayList of matching tasks
     */
    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getDescription().toLowerCase().contains(keyword)) {
                matches.add(t);
            }
        }
        return matches;
    }
}
