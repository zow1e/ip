
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
            String[] parts = input.split("\\s+", 2);


            try {
                switch (parts[0].toLowerCase()) {
                    // exit
                    case "bye":
                        storage.saveTasks(tasks);
                        System.out.println("\nByebye. Hope to see you again soon!");
                        isActive = false;
                        break;
                    
                    // list out items
                    case "list":
                        ui.showTasks(tasks);
                        break;

                    // delete item
                    case "delete":
                        if (tasks.isEmpty()) {
                            throw new KiwiException("No items in list");
                        } else if (parts.length<2) {
                            throw new KiwiException("Please choose a task number to delete");
                        } else {
                            int itemIndex = Integer.parseInt(parts[1]);
                            Task thisItem = tasks.remove(itemIndex-1);
                            System.out.println("Deletion done for:\n"+thisItem.toString()+"\n");
                            System.out.println("There are now "+tasks.size()+" tasks in the list");
                        }
                        break;
                    
                    
                    // mark item as done
                    case "mark":
                        if (tasks.isEmpty()) {
                            throw new KiwiException("No items in list");
                        } else if (parts.length<2) {
                            throw new KiwiException("Please choose a task number to mark");
                        } else {
                            int itemIndex = Integer.parseInt(parts[1]);
                            Task thisItem = tasks.get(itemIndex-1);
                            thisItem.markTask();
                            System.out.println("Great! Marked this as done!!\n"+thisItem.toString()+"\n");
                        }
                        break;
                    
                    // unmark item
                    case "unmark":
                        if (tasks.isEmpty()) {
                            throw new KiwiException("No items in list");
                        } else if (parts.length<2) {
                            throw new KiwiException("Please choose a task number to unmark");
                        } else {
                            int itemIndex = Integer.parseInt(parts[1]);
                            Task thisItem = tasks.get(itemIndex-1);
                            thisItem.unmarkTask();
                            System.out.println("ok... marked this as not done...\n"+thisItem.toString()+"\n");
                        }
                        break;
                    
                    // ToDo task
                    case "todo":
                        if (parts.length < 2 || parts[1].trim().isEmpty()) {
                            throw new KiwiException("The description of a todo cannot be empty");
                        }
                        addTask(new ToDo(parts[1]));
                        break;
                    // Deadline task
                    case "deadline":
                        if (parts.length < 2) throw new KiwiException("Please provide a deadline task");
                        addDeadline(parts[1]);
                        break;
                    //Event task
                    case "event":
                        if (parts.length < 2) throw new KiwiException("Please provide an event task");
                        addEvent(parts[1]);
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

