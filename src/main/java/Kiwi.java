
import java.util.ArrayList;
import java.util.Scanner;

public class Kiwi {
    public static void main(String[] args) {
        String chatbotName = "Kiwi";

        // chatbot intro
        System.out.println("Hello! I'm " + chatbotName);
        System.out.println("What can I do for you? \n");


        // echo user input
        Scanner scanner = new Scanner(System.in);
        String input;
        // no more than 100 tasks
        ArrayList<String> fullList = new ArrayList<>();

        while (scanner.hasNextLine()) {
            input = scanner.nextLine().trim();

            if (input.toLowerCase().equals("bye")) {
                // chatbot exit
                System.out.println("\nByebye. Hope to see you again soon!");
                break;

            // list items added
            } else if (input.toLowerCase().equals("list")) {
                for (int i=1; i<= fullList.size(); i++) {
                    System.out.println(i + ". " + fullList.get(i-1));
                }
                System.out.println("\n");

            } else {
                // add input to list
                fullList.add(input);
                // echo the input
                System.out.println("added: " + input + "\n");
            }
        }
        scanner.close();
    }
}

