package kiwi.helper;
import java.util.ArrayList;

import kiwi.build.Task;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) {
        return tasks.remove(index - 1);
    }

    public Task mark(int index) {
        Task t = tasks.get(index - 1);
        t.markTask();
        return t;
    }

    public Task unmark(int index) {
        Task t = tasks.get(index - 1);
        t.unmarkTask();
        return t;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public int size() {
        return tasks.size();
    }
}
