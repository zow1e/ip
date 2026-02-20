/**
 * Handles all user interface output for the Kiwi task manager.
 *
 * Displays welcome messages, task lists, operation feedback, and errors using
 * consistent formatting with 1-based task numbering.
 *
 * @author zow1e
 * @see Task
 * @see TaskList
 */
package kiwi.helper;

import java.util.ArrayList;
import java.util.stream.IntStream;

import kiwi.build.Task;

/**
 * User interface handler for console output.
 */
public class Ui {

    /**
     * Displays the welcome message when Kiwi starts.
     *
     * Output:
     * Hello! I'm Kiwi
     * What can i do for you?
     */
    public void showWelcome() {
        System.out.println("Hello! I'm Kiwi");
        System.out.println("What can i do for you?");
    }

    /**
     * Displays all tasks in the given list with 1-based numbering.
     *
     * Shows "No tasks yet!" if list is empty.
     *
     * @param tasks list of tasks to display
     */
    public void showTasks(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks yet!");
        } else {
            System.out.println("Here are your tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                Task currItem = tasks.get(i);
                System.out.println((i + 1) + ". " + currItem.toString());
            }
        }
        System.out.println("\n");
    }

    /**
     * Shows confirmation when a task is added, including new total count.
     *
     * @param task the newly added task
     * @param total current total number of tasks after adding
     */
    public void showAddTask(Task task, int total) {
        System.out.println("Added: " + task.toString());
        System.out.println("There are now " + total + " tasks in the list\n");
    }

    /**
     * Shows confirmation when a task is marked as done.
     *
     * @param task the marked task
     */
    public void showMarked(Task task) {
        System.out.println("Great! Marked this as done!!\n" + task.toString() + "\n");
    }

    /**
     * Shows confirmation when a task is unmarked (set as not done).
     *
     * @param task the unmarked task
     */
    public void showUnmarked(Task task) {
        System.out.println("ok... marked this as not done...\n" + task.toString() + "\n");
    }

    /**
     * Shows confirmation when a task is deleted, including new total count.
     *
     * @param task the deleted task
     * @param size current total number of tasks after deletion
     */
    public void showDeleted(Task task, int size) {
        System.out.println("Deletion done for:\n" + task.toString() + "\n");
        System.out.println("There are now " + size + " tasks in the list");
    }

    /**
     * Displays the goodbye message when Kiwi exits.
     *
     * Output:
     * Byebye. Hope to see you again soon!
     */
    public void showBye() {
        System.out.println("\nByebye. Hope to see you again soon!");
    }

    /**
     * Displays tasks matching a search keyword with 1-based numbering.
     *
     * Shows "No matching tasks found." if no matches.
     *
     * @param matches list of matching tasks from search
     */
    public void showMatchingTasks(ArrayList<Task> matches) {
        System.out.println("Here are the matching tasks in your list:");
        if (matches.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            IntStream.range(0, matches.size())
                .mapToObj(index -> (index + 1) + ". " + matches.get(index))
                .forEach(System.out::println);
        }
        System.out.println();
    }

    /**
     * Formats help message for GUI display.
     *
     * @return formatted help message
     */
    public static String formatHelpMessage() {
        StringBuilder help = new StringBuilder();
        help.append("COMMAND FORMAT:\n\n");
        help.append("todo <description>\n");
        help.append("  Example: todo read book\n\n");
        help.append("deadline <description> /by yyyy-MM-dd HHmm\n");
        help.append("  Example: deadline CS2103T /by 2026-02-15 2359\n\n");
        help.append("event <description> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm\n");
        help.append("  Example: event meeting /from 2026-02-12 1400 /to 2026-02-12 1600\n\n");
        help.append("mark <task number>     unmark <task number>\n");
        help.append("delete <task number>   find <keyword>\n");
        help.append("list                   clear                  help\n");
        return help.toString();
    }
}
