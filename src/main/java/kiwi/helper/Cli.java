/**
 * Handles CLI (Command Line Interface) operations for the Kiwi task manager.
 *
 * Manages user interaction in terminal mode, including input processing,
 * task operations display, and user confirmations.
 *
 * @author zow1e
 * @see Kiwi
 * @see Gui
 */
package kiwi.helper;

import java.util.ArrayList;
import java.util.Scanner;

import kiwi.build.Kiwi;
import kiwi.build.Task;

/**
 * CLI operations for Kiwi task manager.
 */
public class Cli {

    private static final String INVALID_IDX_MSG = "Please enter a valid task number\n";

    private Ui ui;
    private TaskList tasks;

    /**
     * Constructs a CLI instance.
     *
     * @param tasks the task list to manage
     */
    public Cli(TaskList tasks) {
        this.tasks = tasks;
        this.ui = new Ui();
    }

    /**
     * Shows the welcome message and initial task list.
     */
    public void showWelcome() {
        ui.showWelcome();
        ui.showTasks(tasks.getTasks());
    }

    /**
     * Shows help message with all command formats.
     */
    public void showHelp() {
        String helpMessage = Ui.formatHelpMessage();
        System.out.println(helpMessage);
    }

    /**
     * Shows all tasks in the list.
     */
    public void listTasks() {
        ui.showTasks(tasks.getTasks());
    }

    /**
     * Shows a newly added task.
     *
     * @param task the task that was added
     */
    public void showAddedTask(Task task) {
        ui.showAddTask(task, tasks.size());
    }

    /**
     * Shows a marked task.
     *
     * @param task the task that was marked
     */
    public void showMarkedTask(Task task) {
        ui.showMarked(task);
    }

    /**
     * Shows an unmarked task.
     *
     * @param task the task that was unmarked
     */
    public void showUnmarkedTask(Task task) {
        ui.showUnmarked(task);
    }

    /**
     * Shows a deleted task.
     *
     * @param task the task that was deleted
     */
    public void showDeletedTask(Task task) {
        ui.showDeleted(task, tasks.size());
    }

    /**
     * Shows matching tasks from a find operation.
     *
     * @param matchingTasks the tasks that matched the search
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        ui.showMatchingTasks(matchingTasks);
    }

    /**
     * Shows the goodbye message.
     */
    public void showBye() {
        ui.showBye();
    }

    /**
     * Shows an error message.
     *
     * @param message the error message
     */
    public void showError(String message) {
        System.err.println(message + "\nPlease retry\n");
    }

    /**
     * Shows an error for invalid index.
     */
    public void showInvalidIndexError() {
        System.err.println(INVALID_IDX_MSG);
    }

    /**
     * Prompts user for confirmation on duplicate task.
     *
     * @param existing the existing task with duplicate description
     * @return true if user wants to replace, false otherwise
     */
    public boolean askReplaceDuplicate(Task existing) {
        System.out.println("!! Duplicate task found: " + existing);
        System.out.print("Replace with new task? [y/n]: ");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim().toLowerCase();
        scanner.close();

        return choice.equals("y");
    }

    /**
     * Prompts user for confirmation on clearing all tasks.
     *
     * @return true if user confirms, false otherwise
     */
    public boolean askConfirmClear() {
        if (tasks.getTasks().isEmpty()) {
            System.out.println("Task list is already empty!\n");
            return false;
        }

        System.out.print("Are you sure you want to delete all tasks? [y/n]: ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim().toLowerCase();
        scanner.close();

        return choice.equals("y");
    }

    /**
     * Shows confirmation that all tasks were cleared.
     */
    public void showTasksCleared() {
        System.out.println("All tasks have been cleared!\n");
    }

    /**
     * Shows message when clear is cancelled.
     */
    public void showClearCancelled() {
        System.out.println("Clear cancelled.\n");
    }
}
