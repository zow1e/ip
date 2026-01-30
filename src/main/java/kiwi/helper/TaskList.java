/**
 * TaskList class for handling actions on task list array.
 * 
 * Encapsulate Task relevant actions.
 * 
 */

package kiwi.helper;
import java.util.ArrayList;

import kiwi.build.Task;

public class TaskList {
    private ArrayList<Task> tasks;

    // Creates empty list
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    // Initializes with given ArrayList<Task>
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    // Adds task to task list
    public void add(Task task) {
        tasks.add(task);
    }

    // Removes task from task list
    public Task delete(int index) {
        return tasks.remove(index - 1);
    }
    
    // Marks task as done
    public Task mark(int index) {
        Task t = tasks.get(index - 1);
        t.markTask();
        return t;
    }

    // Unmark task as done; marks task as not done
    public Task unmark(int index) {
        Task t = tasks.get(index - 1);
        t.unmarkTask();
        return t;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    // Returns number of items in task list
    public int size() {
        return tasks.size();
    }
}
