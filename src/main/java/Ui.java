import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private Scanner scanner = new Scanner(System.in);

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

    
}
