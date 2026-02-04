/**
 * Main entry point and command processor for the Kiwi task manager.
 *
 * Orchestrates the core application flow: initialization, command parsing,
 * task management, storage, and UI interactions.
 *
 * Automatically loads/saves tasks to data/kiwi.txt.
 *
 * Example execution:
 * Hello! I'm Kiwi
 * What can i do for you?
 * > todo buy milk
 * Added: [T][ ] buy milk
 * There are now 1 tasks in the list
 * > bye
 * Byebye. Hope to see you again soon!
 *
 * @author [zow1e]
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

public class Kiwi {

    /** Handles all user interface output. */
    private static Ui ui;
    
    /** Manages persistent task storage. */
    private static Storage storage;
    
    /** Manages the task collection. */
    private static TaskList tasks;

    /** Directory for storing Kiwi data files. */
    private static final String DATA_DIR = "data";
    
    /** Full path to the kiwi.txt data file. */
    private static final String DATA_FILE = DATA_DIR + File.separator + "kiwi.txt";

    /**
     * Main entry point for the Kiwi application.
     *
     * Initializes UI, storage, and task list. Enters interactive command loop
     * until "bye" command is received. Handles all exceptions gracefully.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        // chatbot intro
        ui = new Ui();
        ui.showWelcome();

        // load tasks from hard disk
        storage = new Storage(DATA_DIR, DATA_FILE);
        tasks = new TaskList(storage.loadTasks());

        ui.showTasks(tasks.getTasks());

        // echo user input
        Scanner scanner = new Scanner(System.in);
        String input;

        boolean isActive = true;

        while (isActive && scanner.hasNextLine()) {
            input = scanner.nextLine().trim();

            try {
                Parser parsed = Parser.parse(input);
                String command = parsed.getType().toLowerCase();
                switch (command) {
                // exit
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
}
