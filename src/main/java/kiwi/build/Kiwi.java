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
 * @see kiwi.build.Cli
 * @see kiwi.build.Gui
 */
package kiwi.build;

import java.io.File;
import java.util.Optional;
import java.util.Scanner;

import javafx.stage.Stage;
import kiwi.helper.Cli;
import kiwi.helper.Gui;
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

    /** Manages CLI commands */
    private Cli cli;

    /** Manages GUI commands */
    private Gui gui;


    /**
     * Constructs a Kiwi instance and initializes storage and tasks.
     */
    public Kiwi() {
        storage = new Storage(DATA_DIR, DATA_FILE);
        tasks = new TaskList(storage.loadTasks());
        cli = new Cli(tasks);
        gui = new Gui(tasks);
        assert tasks != null : "TaskList should not be null after initialization";
    }

    /**
     * Sets the GUI stage reference for closing the application.
     *
     * @param stage the primary stage of the GUI application
     */
    public void setGuiStage(Stage stage) {
        gui.setGuiStage(stage);
    }

    /**
     * Main entry point for CLI mode.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        Kiwi kiwi = new Kiwi();
        kiwi.cli.showWelcome();
        kiwi.runCliLoop();
    }

    /**
     * Runs the CLI interactive loop.
     */
    private void runCliLoop() {
        Scanner scanner = new Scanner(System.in);
        boolean isActive = true;

        while (isActive && scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            try {
                isActive = processCliCommand(input);
            } catch (KiwiException e) {
                cli.showError(e.getMessage());
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                cli.showInvalidIndexError();
            }
        }
        scanner.close();
    }

    /**
     * Processes a CLI command.
     *
     * @param input the user's command
     * @return false if bye command, true otherwise
     * @throws KiwiException if command parsing fails
     */
    private boolean processCliCommand(String input) throws KiwiException {
        Parser parsed = Parser.parse(input);
        String command = parsed.getType().toLowerCase();

        switch (command) {
        case "bye":
            storage.saveTasks(tasks.getTasks());
            cli.showBye();
            return false;

        case "list":
            cli.listTasks();
            break;

        case "todo":
            executeAddTask(new ToDo(parsed.getArg(0)), true);
            break;

        case "deadline":
            executeAddTask(new Deadline(parsed.getArg(0), parsed.getArg(1)), true);
            break;

        case "event":
            executeAddTask(new Event(parsed.getArg(0), parsed.getArg(1), parsed.getArg(2)), true);
            break;

        case "mark":
            executeMark(Integer.parseInt(parsed.getArg(0)), true);
            break;

        case "unmark":
            executeUnmark(Integer.parseInt(parsed.getArg(0)), true);
            break;

        case "delete":
            executeDelete(Integer.parseInt(parsed.getArg(0)), true);
            break;

        case "find":
            executeFindTasks(parsed.getArg(0).toLowerCase(), true);
            break;

        case "help":
            cli.showHelp();
            break;

        case "clear":
            executeClear(true);
            break;

        default:
            throw new KiwiException("Command not recognised!");
        }
        return true;
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

            return executeGuiCommand(command, parsed);

        } catch (KiwiException e) {
            return e.getMessage();
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return "Please enter a valid task number\n";
        }
    }

    /**
     * Executes a GUI command.
     *
     * @param command the command type
     * @param parsed the parsed command
     * @return the response message
     * @throws KiwiException if command execution fails
     */
    private String executeGuiCommand(String command, Parser parsed) throws KiwiException {
        switch (command) {
        case "bye":
            storage.saveTasks(tasks.getTasks());
            gui.closeApplication();
            return gui.formatByeMessage();

        case "list":
            return gui.formatTasks(tasks.getTasks());

        case "todo":
            return executeAddTask(new ToDo(parsed.getArg(0)), false);

        case "deadline":
            return executeAddTask(new Deadline(parsed.getArg(0), parsed.getArg(1)), false);

        case "event":
            return executeAddTask(new Event(parsed.getArg(0), parsed.getArg(1), parsed.getArg(2)), false);

        case "mark":
            return executeMark(Integer.parseInt(parsed.getArg(0)), false);

        case "unmark":
            return executeUnmark(Integer.parseInt(parsed.getArg(0)), false);

        case "delete":
            return executeDelete(Integer.parseInt(parsed.getArg(0)), false);

        case "find":
            return gui.formatTasks(tasks.find(parsed.getArg(0).toLowerCase()));

        case "help":
            return gui.formatHelpMessage();

        case "clear":
            return executeClear(false);

        default:
            return "Command not recognised!";
        }
    }

    /**
     * Executes add task command.
     *
     * @param task the task to add
     * @param isCliMode true for CLI, false for GUI
     * @return response message (GUI only)
     * @throws KiwiException if duplicate found in GUI mode
     */
    private String executeAddTask(Task task, boolean isCliMode) throws KiwiException {
        Task finalTask = checkDuplicate(task, isCliMode);
        tasks.add(finalTask);

        if (isCliMode) {
            cli.showAddedTask(finalTask);
            return null;
        } else {
            return gui.formatAddedTask(finalTask);
        }
    }

    /**
     * Executes mark command.
     *
     * @param index the task index
     * @param isCliMode true for CLI, false for GUI
     * @return response message (GUI only)
     */
    private String executeMark(int index, boolean isCliMode) {
        Task marked = tasks.mark(index);

        if (isCliMode) {
            cli.showMarkedTask(marked);
            return null;
        } else {
            return gui.formatMarkedTask(marked);
        }
    }

    /**
     * Executes unmark command.
     *
     * @param index the task index
     * @param isCliMode true for CLI, false for GUI
     * @return response message (GUI only)
     */
    private String executeUnmark(int index, boolean isCliMode) {
        Task unmarked = tasks.unmark(index);

        if (isCliMode) {
            cli.showUnmarkedTask(unmarked);
            return null;
        } else {
            return gui.formatUnmarkedTask(unmarked);
        }
    }

    /**
     * Executes delete command.
     *
     * @param index the task index
     * @param isCliMode true for CLI, false for GUI
     * @return response message (GUI only)
     */
    private String executeDelete(int index, boolean isCliMode) {
        Task deleted = tasks.delete(index);

        if (isCliMode) {
            cli.showDeletedTask(deleted);
            return null;
        } else {
            return gui.formatDeletedTask(deleted);
        }
    }

    /**
     * Executes find command.
     *
     * @param keyword the search keyword
     * @param isCliMode true for CLI, false for GUI
     */
    private String executeFindTasks(String keyword, boolean isCliMode) {
        var matching = tasks.find(keyword);

        if (isCliMode) {
            cli.showMatchingTasks(matching);
            return null;
        }
        return null;
    }

    /**
     * Executes clear command.
     *
     * @param isCliMode true for CLI, false for GUI
     * @return response message (GUI only)
     */
    private String executeClear(boolean isCliMode) {
        if (isCliMode) {
            if (cli.askConfirmClear()) {
                tasks.getTasks().clear();
                cli.showTasksCleared();
            } else {
                cli.showClearCancelled();
            }
            return null;
        } else {
            return gui.formatClearedTasks();
        }
    }

    /**
     * Checks for duplicate task descriptions.
     *
     * @param newTask the new task
     * @param isCliMode true for CLI, false for GUI
     * @return the final task to add
     * @throws KiwiException if duplicate found in GUI mode
     */
    private Task checkDuplicate(Task newTask, boolean isCliMode) throws KiwiException {
        String newDesc = newTask.getDescription().toLowerCase();

        Optional<Task> duplicate = tasks.getTasks().stream()
            .filter(t -> t.getDescription().toLowerCase().equals(newDesc))
            .findFirst();

        if (duplicate.isEmpty()) {
            return newTask;
        }

        Task existing = duplicate.get();

        if (isCliMode) {
            return cli.askReplaceDuplicate(existing) ? newTask : existing;
        } else {
            throw new KiwiException(gui.formatDuplicateMessage(existing));
        }
    }
}
