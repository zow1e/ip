
import java.util.ArrayList;
import java.util.Scanner;

public class Kiwi {

    // store tasks; no more than 100 tasks
    private static ArrayList<Task> tasks = new ArrayList<>();
    public static void main(String[] args) {
        String chatbotName = "Kiwi";

        // chatbot intro
        System.out.println("Hello! I'm " + chatbotName);
        System.out.println("What can I do for you? \n");


        // echo user input
        Scanner scanner = new Scanner(System.in);
        String input;

        boolean isActive = true;

        while (isActive && scanner.hasNextLine()) {
            input = scanner.nextLine().trim();
            String[] parts = input.split("\\s+", 2);


            switch (parts[0].toLowerCase()) {
                // exit
                case "bye":
                    System.out.println("\nByebye. Hope to see you again soon!");
                    isActive = false;
                    break;
                
                // list out items
                case "list":
                    listTasks();
                    break;
                
                // mark item as done
                case "mark":
                    if (tasks.isEmpty()) {
                        System.out.println("No items in list");
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
                        System.out.println("No items in list");
                    } else {
                        int itemIndex = Integer.parseInt(parts[1]);
                        Task thisItem = tasks.get(itemIndex-1);
                        thisItem.unmarkTask();
                        System.out.println("ok... marked this as not done...\n"+thisItem.toString()+"\n");
                    }
                    break;
                
                // ToDo task
                case "todo":
                    addTask(new ToDo(parts[1]));
                    break;
                // Deadline task
                case "deadline":
                    addDeadline(parts[1]);
                    break;
                //Event task
                case "event":
                    addEvent(parts[1]);
                    break;

            
                default:
                    System.out.println("Command not recognised!");    
                    break;
            }
        }
        scanner.close();
        
    }


    private static void addTask(Task task) {
        tasks.add(task);
        System.out.println("Added: "+task.toString());
        System.out.println("There are now "+tasks.size()+" tasks in the list\n");
    }

    private static void addDeadline(String input) {
        String[] fullLine = input.split(" /by ");
        String desc = fullLine[0].trim();
        String dueDate = fullLine[1].trim();
        addTask(new Deadline(desc, dueDate));
    }
    private static void addEvent(String input) {
        String[] fullLine = input.split(" /from | /to");
        String desc = fullLine[0].trim();
        String from = fullLine[1].trim();
        String to = fullLine[2].trim();
        addTask(new Event(desc, from, to));
    }

    private static void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks yet!");
        } else {
            System.out.println("Here are your tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                Task currItem = tasks.get(i);
                System.out.println((i + 1 )+ ". " + currItem.toString());
            }
        }
        System.out.println("\n");
    }

}

