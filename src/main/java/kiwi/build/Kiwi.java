/**
 * Start file for Kiwi.
 * 
 * Functions:
 * Welcome message
 * Bye message
 * Load saved tasks
 * List tasks
 * Add task (Todo / Deadline / Event)
 * Delete task
 * 
 * @throws KiwiExecption if error occurs.
 */


package kiwi.build;

import java.util.ArrayList;
import java.util.Scanner;

import kiwi.helper.KiwiException;
import kiwi.helper.Parser;
import kiwi.helper.Storage;
import kiwi.helper.TaskList;
import kiwi.helper.Ui;

import java.io.File;


public class Kiwi {

    private static Ui ui;
    private static Storage storage;
    private static TaskList tasks;

    // data file path
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = DATA_DIR + File.separator + "kiwi.txt";

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
                        markTask.markTask();
                        ui.showMarked(markTask);
                        break;
                        
                    case "unmark":
                        int unmarkIdx = Integer.parseInt(parsed.getArg(0));
                        Task unmarkTask = tasks.unmark(unmarkIdx);
                        unmarkTask.unmarkTask();
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
                System.err.println(e.getMessage()+"\nPlease retry\n");
            } catch (NumberFormatException e) {
                System.err.println("Please enter a valid task number\n");
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Task number does not exist\n");
            }
        }
        scanner.close();
        
    }

}

