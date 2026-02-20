/**
 * Handles GUI (Graphical User Interface) operations for the Kiwi task manager.
 *
 * Manages response formatting for GUI mode, including text-based output
 * that appears in chat bubbles and application display.
 *
 * @author zow1e
 * @see Kiwi
 * @see Cli
 */
package kiwi.helper;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Platform;
import javafx.stage.Stage;
import kiwi.build.Kiwi;
import kiwi.build.Task;

/**
 * GUI operations for Kiwi task manager.
 */
public class Gui {

    private Stage guiStage;
    private TaskList tasks;

    /**
     * Constructs a GUI instance.
     *
     * @param tasks the task list to manage
     */
    public Gui(TaskList tasks) {
        this.tasks = tasks;
    }

    /**
     * Sets the GUI stage reference for closing the application.
     *
     * @param stage the primary stage of the GUI application
     */
    public void setGuiStage(Stage stage) {
        this.guiStage = stage;
    }

    /**
     * Formats tasks as a response message.
     *
     * @param taskList the tasks to format
     * @return formatted task list response
     */
    public String formatTasks(ArrayList<Task> taskList) {
        if (taskList.isEmpty()) {
            return "No matching tasks found.";
        }
        return IntStream.range(0, taskList.size())
            .mapToObj(index -> (index + 1) + ". " + taskList.get(index))
            .collect(Collectors.joining("\n", "Here are the tasks:\n", ""));
    }

    /**
     * Formats a response for a newly added task.
     *
     * @param task the task that was added
     * @return formatted add response
     */
    public String formatAddedTask(Task task) {
        return "Added: " + task + "\nThere are now " + tasks.size() + " tasks in the list";
    }

    /**
     * Formats a response for a marked task.
     *
     * @param task the task that was marked
     * @return formatted mark response
     */
    public String formatMarkedTask(Task task) {
        return "Nice! I've marked this task as done:\n  " + task;
    }

    /**
     * Formats a response for an unmarked task.
     *
     * @param task the task that was unmarked
     * @return formatted unmark response
     */
    public String formatUnmarkedTask(Task task) {
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Formats a response for a deleted task.
     *
     * @param task the task that was deleted
     * @return formatted delete response
     */
    public String formatDeletedTask(Task task) {
        return "Noted. I've removed this task:\n  " + task
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Formats help message for GUI display.
     *
     * @return formatted help message
     */
    public String formatHelpMessage() {
        return Ui.formatHelpMessage();
    }

    /**
     * Formats response for clearing tasks.
     *
     * @return formatted clear response
     */
    public String formatClearedTasks() {
        if (tasks.getTasks().isEmpty()) {
            return "Task list is already empty!";
        }
        tasks.getTasks().clear();
        return "All tasks have been cleared!";
    }

    /**
     * Formats response for duplicate task found.
     *
     * @param existing the existing task with duplicate description
     * @return formatted duplicate message
     */
    public String formatDuplicateMessage(Task existing) {
        return "Duplicate task found:\n  " + existing
            + "\n\nUse the same command again to replace it, or use a different description.";
    }

    /**
     * Closes the GUI stage gracefully.
     */
    public void closeApplication() {
        if (guiStage != null) {
            Platform.runLater(() -> guiStage.close());
        } else {
            Platform.exit();
        }
    }

    /**
     * Formats goodbye message.
     *
     * @return goodbye message
     */
    public String formatByeMessage() {
        return "Byebye. Hope to see you again soon!";
    }
}
