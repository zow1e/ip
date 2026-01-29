
import java.util.ArrayList;
import java.util.Scanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;


public class Kiwi {

    private static Ui ui;
    private static Storage storage;

    // data file path
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = DATA_DIR + File.separator + "kiwi.txt";

    // store tasks; no more than 100 tasks
    private static ArrayList<Task> tasks = new ArrayList<>();
    public static void main(String[] args) {

        // chatbot intro
        ui = new Ui();
        ui.showWelcome();

        // load tasks from hard disk
        storage = new Storage(DATA_DIR, DATA_FILE);
        tasks = storage.loadTasks();

        ui.showTasks(tasks);

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
                        storage.saveTasks(tasks);
                        ui.showBye();
                        isActive = false;
                        break;
                    
                    case "list":
                        ui.showTasks(tasks);
                        break;

                    case "todo":
                        addTask(new ToDo(parsed.getArg(0)));
                        break;
                        
                    case "deadline":
                        String desc = parsed.getArg(0);
                        String by = parsed.getArg(1);
                        addTask(new Deadline(desc, by));
                        break;
                        
                    case "event":
                        String evDesc = parsed.getArg(0);
                        String from = parsed.getArg(1);
                        String to = parsed.getArg(2);
                        addTask(new Event(evDesc, from, to));
                        break;
                        
                    case "mark":
                        int markIdx = Integer.parseInt(parsed.getArg(0));
                        Task markTask = tasks.get(markIdx - 1);
                        markTask.markTask();
                        ui.showMarked(markTask);
                        break;
                        
                    case "unmark":
                        int unmarkIdx = Integer.parseInt(parsed.getArg(0));
                        Task unmarkTask = tasks.get(unmarkIdx - 1);
                        unmarkTask.unmarkTask();
                        ui.showUnmarked(unmarkTask);
                        break;
                        
                    case "delete":
                        int delIdx = Integer.parseInt(parsed.getArg(0));
                        Task deleted = tasks.remove(delIdx - 1);
                        ui.showDeleted(deleted, tasks.size());
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


    

    // create Task objects
    private static void addTask(Task task) {
        tasks.add(task);
        ui.showAddTask(task, tasks.size());
    }

    private static void addDeadline(String input) throws KiwiException {
        try {
            String[] fullLine = input.split(" /by ");
            if (fullLine.length < 2) throw new KiwiException
                                    ("Correct Deadline format: deadline <task> /by <yyyy-MM-dd HHmm>");

            String desc = fullLine[0].trim();
            String dueDate = fullLine[1].trim();

            if (desc.isEmpty()) throw new KiwiException("Deadline description cannot be empty");
            if (dueDate.isEmpty()) throw new KiwiException("Deadline due date cannot be empty");

            try {
                // LocalDateTime dateTime = LocalDateTime.parse
                //                          (dueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
                addTask(new Deadline(desc, dueDate));
            } catch (Exception e) {
                throw new KiwiException("Invalid date format; use: <yyyy-MM-dd HHmm>");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new KiwiException("Invalid date format; use: <yyyy-MM-dd HHmm>");
        }
    }
    private static void addEvent(String input) throws KiwiException {
        try {
            String[] fullLine = input.split(" /from | /to");
            if (fullLine.length < 3) throw new KiwiException
                                     ("Invalid date format; use: event <task> /from <yyyy-MM-dd HHmm> /to <HHmm>");
            String desc = fullLine[0].trim();
            String from = fullLine[1].trim();
            String to = fullLine[2].trim();

            if (desc.isEmpty()) throw new KiwiException("Event desription cannot be empty.");
            if (from.isEmpty()) throw new KiwiException("Event start time cannot be empty.");
            if (to.isEmpty()) throw new KiwiException("Event end time cannot be empty.");

            try {
                addTask(new Event(desc, from, to));
            } catch (Exception e) {
                throw new KiwiException
                          ("Invalid date format; use: event <task> /from <yyyy-MM-dd HHmm> /to <HHmm>");
            }
            
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new KiwiException
                      ("Invalid date format; use: event <task> /from <yyyy-MM-dd HHmm> /to <HHmm>");
        }
    }



    

}

