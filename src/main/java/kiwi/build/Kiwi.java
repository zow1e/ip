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
        initializeStorage();
        ui.showTasks(tasks.getTasks());

        runCliLoop();
    }

    private static void initializeStorage() {
        storage = new Storage(DATA_DIR, DATA_FILE);
        tasks = new TaskList(storage.loadTasks());
    }

    private static void runCliLoop() {
        Scanner scanner = new Scanner(System.in);
        boolean isActive = true;

        while (isActive && scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            try {
                isActive = processCliCommand(input);
            } catch (KiwiException e) {
                System.err.println(e.getMessage() + "\nPlease retry\n");
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.err.println(INVALID_IDX_MSG);
            }
        }
        scanner.close();
    }

    private static boolean processCliCommand(String input) throws KiwiException {
        Parser parsed = Parser.parse(input);
        String command = parsed.getType().toLowerCase();

        switch (command) {
        case "bye":
            storage.saveTasks(tasks.getTasks());
            ui.showBye();
            return false;

        case "list":
            ui.showTasks(tasks.getTasks());
            break;

        case "todo":
            addTaskCli(new ToDo(parsed.getArg(0)));
            break;

        case "deadline":
            addTaskCli(new Deadline(parsed.getArg(0), parsed.getArg(1)));
            break;

        case "event":
            addTaskCli(new Event(parsed.getArg(0), parsed.getArg(1), parsed.getArg(2)));
            break;

        case "mark":
            markTaskCli(Integer.parseInt(parsed.getArg(0)));
            break;

        case "unmark":
            unmarkTaskCli(Integer.parseInt(parsed.getArg(0)));
            break;

        case "delete":
            deleteTaskCli(Integer.parseInt(parsed.getArg(0)));
            break;

        case "find":
            findTasksCli(parsed.getArg(0).toLowerCase());
            break;

        default:
            throw new KiwiException("Command not recognised!");
        }
        return true;
    }

    private static void addTaskCli(Task task) throws KiwiException {
        Task finalTask = checkDuplicateDesc(task, false);
        tasks.add(finalTask);
        ui.showAddTask(finalTask, tasks.size());
    }

    private static void markTaskCli(int index) {
        Task markTask = tasks.mark(index);
        ui.showMarked(markTask);
    }

    private static void unmarkTaskCli(int index) {
        Task unmarkTask = tasks.unmark(index);
        ui.showUnmarked(unmarkTask);
    }

    private static void deleteTaskCli(int index) {
        Task deleted = tasks.delete(index);
        ui.showDeleted(deleted, tasks.size());
    }

    private static void findTasksCli(String keyword) {
        ArrayList<Task> matchingTasks = tasks.find(keyword);
        ui.showMatchingTasks(matchingTasks);
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

            return handleGuiCommand(command, parsed);

        } catch (KiwiException e) {
            return e.getMessage();
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return INVALID_IDX_MSG;
        }
    }

    private String handleGuiCommand(String command, Parser parsed) throws KiwiException {
        switch (command) {
        case "bye":
            storage.saveTasks(tasks.getTasks());
            return "Byebye. Hope to see you again soon!";

        case "list":
            return formatTasks(tasks.getTasks());

        case "todo":
            return addTaskGui(new ToDo(parsed.getArg(0)));

        case "deadline":
            return addTaskGui(new Deadline(parsed.getArg(0), parsed.getArg(1)));

        case "event":
            return addTaskGui(new Event(parsed.getArg(0), parsed.getArg(1), parsed.getArg(2)));

        case "mark":
            return markTaskGui(Integer.parseInt(parsed.getArg(0)));

        case "unmark":
            return unmarkTaskGui(Integer.parseInt(parsed.getArg(0)));

        case "delete":
            return deleteTaskGui(Integer.parseInt(parsed.getArg(0)));

        case "find":
            return formatTasks(tasks.find(parsed.getArg(0).toLowerCase()));

        default:
            return "Command not recognised!";
        }
    }

    private String markTaskGui(int index) {
        validateIndex(index);
        Task markTask = tasks.mark(index);
        return "Nice! I've marked this task as done:\n  " + markTask;
    }

    private String unmarkTaskGui(int index) {
        validateIndex(index);
        Task unmarkTask = tasks.unmark(index);
        return "OK, I've marked this task as not done yet:\n  " + unmarkTask;
    }

    private String deleteTaskGui(int index) {
        Task deleted = tasks.delete(index);
        return "Noted. I've removed this task:\n  " + deleted
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private void validateIndex(int index) {
        assert index > 0 && index <= tasks.size() : INVALID_IDX_MSG;
    }

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
     * In GUI mode: throws exception asking user to retry.
     *
     * @param newTask the newly created task
     * @param isGuiMode true if running in GUI mode, false for CLI
     * @return the task to keep (either newTask or an existing matching task)
     * @throws KiwiException if duplicate found in GUI mode
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
        return isGuiMode ? handleGuiDuplicate(existing) : handleCliDuplicate(existing, newTask);
    }

    private static Task handleGuiDuplicate(Task existing) throws KiwiException {
        throw new KiwiException("Duplicate task found:\n  " + existing
            + "\n\nUse the same command again to replace it, or use a different description.");
    }

    private static Task handleCliDuplicate(Task existing, Task newTask) {
        System.out.println("!! Duplicate task found: " + existing);
        System.out.print("Replace with new task? [y/n]: ");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim().toLowerCase();
        scanner.close();

        return choice.equals("y") ? newTask : existing;
    }

    private static String addTaskGui(Task task) throws KiwiException {
        Task finalTask = checkDuplicateDesc(task, true);
        tasks.add(finalTask);
        return "Added: " + task + "\nThere are now " + tasks.size() + " tasks in the list";
    }
}
