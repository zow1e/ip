package kiwi.helper;
import java.util.ArrayList;

import kiwi.build.Task;

public class Ui {

    public void showWelcome() {
        System.out.println("Hello! I'm Kiwi");
        System.out.println("What can i do for you?");
    }

    public void showTasks(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks yet!");
        } else {
            System.out.println("Here are your tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                Task currItem = tasks.get(i);
                System.out.println((i + 1 )+ ". " + currItem.toString());
            }
        }
        System.out.println("\n");
    }

    public void showAddTask(Task task, int total) {
        System.out.println("Added: " + task.toString());
        System.out.println("There are now " + total + " tasks in the list\n");
    }

    public void showMarked(Task task) {
        System.out.println("Great! Marked this as done!!\n"+task.toString()+"\n");
    }

    public void showUnmarked(Task task) {
        System.out.println("ok... marked this as not done...\n"+task.toString()+"\n");
    }

    public void showDeleted(Task task, int size) {
        System.out.println("Deletion done for:\n"+task.toString()+"\n");
        System.out.println("There are now "+size+" tasks in the list");
    }

    public void showBye() {
        System.out.println("\nByebye. Hope to see you again soon!");
    }
    
}
