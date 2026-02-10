/**
 * Main entry point and command processor for the Kiwi task manager.
 *
 * Orchestrates the core application flow: initialization, command parsing,
 * task management, storage, and UI interactions.
 *
 * Automatically loads/saves tasks to data/kiwi.txt.
 *
 * Supports both CLI mode (main method) and GUI mode (getResponse method).
 *
 * @author zow1e
 * @see kiwi.helper.Parser
 * @see kiwi.helper.Storage
 * @see kiwi.helper.TaskList
 * @see kiwi.helper.Ui
 * @see kiwi.helper.KiwiException
 */
package kiwi.build;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import kiwi.helper.KiwiException;
import kiwi.helper.Parser;
import kiwi.helper.Storage;
import kiwi.helper.TaskList;
import kiwi.helper.Ui;

/**
 * Core logic for the Kiwi task manager.
 * Handles command processing and task management for both CLI and GUI.
 */
public class Kiwi {

    /** Directory for storing Kiwi data files. */
    private static final String DATA_DIR = "data";

    /** Full path to the kiwi.txt data file. */
    private static final String DATA_FILE = DATA_DIR + File.separator + "kiwi.txt";

    /** Manages persistent task storage. */
    private Storage storage;

    /** Manages the task collection. */
    private TaskList tasks;

    /**
     * Constructs a Kiwi instance and initializes storage and tasks.
     */
    public Kiwi() {
        storage = new Storage(DATA_DIR, DATA_FILE);
        tasks = new TaskList(storage.loadTasks());
    }

    /**
     * Main entry point for CLI mode.
     *
     * Initializes UI, storage, and task list. Enters interactive command loop
     * until "bye" command is received. Handles all exceptions gracefully.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage(DATA_DIR, DATA_FILE);
        TaskList tasks = new TaskList(storage.loadTasks());

        ui.showTasks(tasks.getTasks());

        Scanner scanner = new Scanner(System.in);
        String input;

        boolean isActive = true;

        while (isActive && scanner.hasNextLine()) {
            input = scanner.nextLine().trim();

            try {
                Parser parsed = Parser.parse(input);
                String command = parsed.getType().toLowerCase();
                switch (command) {
                case "bye":
                    storage.saveTasks(tasks.getTasks());
                    ui.showBye();
                    isActive = false;
                    break;

                case "list":
                    ui.showTasks(tasks.getTasks());
                    break;

                case "todo":
                    Task currTask = new ToDo(parsed.getArg(0));
                    tasks.add(currTask);
                    ui.showAddTask(currTask, tasks.size());
                    break;

                case "deadline":
                    String desc = parsed.getArg(0);
                    String by = parsed.getArg(1);
                    currTask = new Deadline(desc, by);
                    tasks.add(currTask);
                    ui.showAddTask(currTask, tasks.size());
                    break;

                case "event":
                    String evDesc = parsed.getArg(0);
                    String from = parsed.getArg(1);
                    String to = parsed.getArg(2);
                    currTask = new Event(evDesc, from, to);
                    tasks.add(currTask);
                    ui.showAddTask(currTask, tasks.size());
                    break;

                case "mark":
                    int markIdx = Integer.parseInt(parsed.getArg(0));
                    Task markTask = tasks.mark(markIdx);
                    ui.showMarked(markTask);
                    break;

                case "unmark":
                    int unmarkIdx = Integer.parseInt(parsed.getArg(0));
                    Task unmarkTask = tasks.unmark(unmarkIdx);
                    ui.showUnmarked(unmarkTask);
                    break;

                case "delete":
                    int delIdx = Integer.parseInt(parsed.getArg(0));
                    Task deleted = tasks.delete(delIdx);
                    ui.showDeleted(deleted, tasks.size());
                    break;

                case "find":
                    String keyword = parsed.getArg(0).toLowerCase();
                    ArrayList<Task> matching = tasks.find(keyword);
                    ui.showMatchingTasks(matching);
                    break;

                default:
                    throw new KiwiException("Command not recognised!");
                }
            } catch (KiwiException e) {
                System.err.println(e.getMessage() + "\nPlease retry\n");
            } catch (NumberFormatException e) {
                System.err.println("Please enter a valid task number\n");
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Task number does not exist\n");
            }
        }
        scanner.close();
    }

    /**
     * Processes user input and returns a response (for GUI mode).
     *
     * @param input the user's command
     * @return the response from Kiwi
     */
    public String getResponse(String input) {
        try {
            Parser parsed = Parser.parse(input);
            String command = parsed.getType().toLowerCase();

            switch (command) {
            case "bye":
                storage.saveTasks(tasks.getTasks());
                return "Byebye. Hope to see you again soon!";

            case "list":
                return formatTaskList();

            case "todo":
                Task currTask = new ToDo(parsed.getArg(0));
                tasks.add(currTask);
                return formatAddTask(currTask);

            case "deadline":
                String desc = parsed.getArg(0);
                String by = parsed.getArg(1);
                currTask = new Deadline(desc, by);
                tasks.add(currTask);
                return formatAddTask(currTask);

            case "event":
                String evDesc = parsed.getArg(0);
                String from = parsed.getArg(1);
                String to = parsed.getArg(2);
                currTask = new Event(evDesc, from, to);
                tasks.add(currTask);
                return formatAddTask(currTask);

            case "mark":
                int markIdx = Integer.parseInt(parsed.getArg(0));
                Task markTask = tasks.mark(markIdx);
                return "Nice! I've marked this task as done:\n  " + markTask;

            case "unmark":
                int unmarkIdx = Integer.parseInt(parsed.getArg(0));
                Task unmarkTask = tasks.unmark(unmarkIdx);
                return "OK, I've marked this task as not done yet:\n  " + unmarkTask;

            case "delete":
                int delIdx = Integer.parseInt(parsed.getArg(0));
                Task deleted = tasks.delete(delIdx);
                return "Noted. I've removed this task:\n  " + deleted
                        + "\nNow you have " + tasks.size() + " tasks in the list.";

            case "find":
                String keyword = parsed.getArg(0).toLowerCase();
                ArrayList<Task> matchingTasks = tasks.find(keyword);
                return formatMatchingTasks(matchingTasks);

            default:
                return "Command not recognised!";
            }

        } catch (KiwiException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return "Please enter a valid task number";
        } catch (IndexOutOfBoundsException e) {
            return "Task number does not exist";
        }
    }

    /**
     * Formats the task list for display.
     *
     * @return formatted task list
     */
    private String formatTaskList() {
        StringBuilder sb = new StringBuilder("Here are your tasks:\n");
        ArrayList<Task> taskList = tasks.getTasks();
        for (int i = 0; i < taskList.size(); i++) {
            sb.append((i + 1)).append(". ").append(taskList.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Formats the add task response.
     *
     * @param task the task that was added
     * @return formatted response
     */
    private String formatAddTask(Task task) {
        return "Added: " + task + "\nThere are now " + tasks.size() + " tasks in the list";
    }

    /**
     * Formats the matching tasks for display.
     *
     * @param matching the list of matching tasks
     * @return formatted response
     */
    private String formatMatchingTasks(ArrayList<Task> matching) {
        if (matching.isEmpty()) {
            return "No matching tasks found.";
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks:\n");
        for (int i = 0; i < matching.size(); i++) {
            sb.append((i + 1)).append(". ").append(matching.get(i)).append("\n");
        }
        return sb.toString();
    }
}
