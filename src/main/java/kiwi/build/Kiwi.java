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
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    /** Error message for out of bounds index input. */
    private static final String INVALID_IDX_MSG = "Please enter a valid task number\n";

    /** Error message for out of bounds index input. */
    private static Ui ui = new Ui();

    /** Manages persistent task storage. */
    private static Storage storage;

    /** Manages the task collection. */
    private static TaskList tasks;



    /**
     * Constructs a Kiwi instance and initializes storage and tasks.
     */
    public Kiwi() {
        storage = new Storage(DATA_DIR, DATA_FILE);
        tasks = new TaskList(storage.loadTasks());
        assert tasks != null : "TaskList should not be null after initialization";
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
                    addTask(currTask);
                    break;

                case "deadline":
                    String desc = parsed.getArg(0);
                    String by = parsed.getArg(1);
                    currTask = new Deadline(desc, by);
                    addTask(currTask);
                    break;

                case "event":
                    String evDesc = parsed.getArg(0);
                    String from = parsed.getArg(1);
                    String to = parsed.getArg(2);
                    currTask = new Event(evDesc, from, to);
                    addTask(currTask);
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
                System.err.println(INVALID_IDX_MSG);
            } catch (IndexOutOfBoundsException e) {
                System.err.println(INVALID_IDX_MSG);
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

            assert parsed != null : "Parser should not return a null object";
            assert !command.isEmpty() : "Parsed command cannot be empty";

            switch (command) {
            case "bye":
                storage.saveTasks(tasks.getTasks());
                return "Byebye. Hope to see you again soon!";

            case "list":
                return formatTasks(tasks.getTasks());

            case "todo":
                Task currTask = new ToDo(parsed.getArg(0));
                return addTaskGui(currTask);

            case "deadline":
                String desc = parsed.getArg(0);
                String by = parsed.getArg(1);
                currTask = new Deadline(desc, by);
                return addTaskGui(currTask);

            case "event":
                String evDesc = parsed.getArg(0);
                String from = parsed.getArg(1);
                String to = parsed.getArg(2);
                currTask = new Event(evDesc, from, to);
                return addTaskGui(currTask);

            case "mark":
                int markIdx = Integer.parseInt(parsed.getArg(0));
                assert markIdx > 0 && markIdx <= tasks.size() : INVALID_IDX_MSG;
                Task markTask = tasks.mark(markIdx);
                return "Nice! I've marked this task as done:\n  " + markTask;

            case "unmark":
                int unmarkIdx = Integer.parseInt(parsed.getArg(0));
                assert unmarkIdx > 0 && unmarkIdx <= tasks.size() : "Index to unmark must be valid";
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
                return formatTasks(matchingTasks);

            default:
                return "Command not recognised!";
            }

        } catch (KiwiException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return INVALID_IDX_MSG;
        } catch (IndexOutOfBoundsException e) {
            return INVALID_IDX_MSG;
        }
    }


    /**
     * Format TaskList for display.
     *
     * @return formatted task list
     */
    private String formatTasks(ArrayList<Task> taskList) {
        if (taskList.isEmpty()) {
            return "No matching tasks found.";
        }
        return IntStream.range(0, taskList.size())
            .mapToObj(index -> (index + 1) + ". " + taskList.get(index))
            .collect(Collectors.joining("\n", "Here are the tasks:\n", ""));
    }

    /**
     * Checks if there is an existing task with the same description.
     *
     * In CLI mode: prompts user to replace or keep the existing task.
     * In GUI mode: returns a formatted message asking the user to decide.
     *
     * @param newTask the newly created task
     * @param isGuiMode true if running in GUI mode, false for CLI
     * @return the task to keep (either newTask or an existing matching task)
     * @throws KiwiException if duplicate handling fails
     */
    private static Task checkDuplicateDesc(Task newTask, boolean isGuiMode) throws KiwiException {
        String newDesc = newTask.getDescription().toLowerCase();

        Optional<Task> duplicate = tasks.getTasks().stream()
            .filter(t -> t.getDescription().toLowerCase().equals(newDesc))
            .findFirst();

        if (duplicate.isEmpty()) {
            return newTask;
        }

        Task existing = duplicate.get();

        if (isGuiMode) {
            throw new KiwiException("Duplicate task found:\n  " + existing);
        }

        // CLI mode
        System.out.println("!! Duplicate task found: " + existing);
        System.out.print("Replace with new task? [y/n]: ");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim().toLowerCase();
        scanner.close();
        return choice.equals("y") ? newTask : existing;
    }

    /**
     * Adds a task and handles duplicates (for CLI mode).
     *
     * @param task the new task
     * @return formatted response
     */
    private static void addTask(Task task) throws KiwiException {
        Task finalTask = checkDuplicateDesc(task, false);
        tasks.add(finalTask);
        ui.showAddTask(finalTask, tasks.size());;
    }

    /**
     * Adds a task and handles duplicates (for GUI mode).
     *
     * @param task the new task
     * @return formatted response
     * @throws KiwiException if duplicate found
     */
    private static String addTaskGui(Task task) throws KiwiException {
        Task finalTask = checkDuplicateDesc(task, true);
        tasks.add(finalTask);
        return ("Added: " + task.toString() + "\nThere are now " + tasks.size() + " tasks in the list\n");
    }

}
