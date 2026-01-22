
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

        while (scanner.hasNextLine()) {
            input = scanner.nextLine().trim();

            if (input.toLowerCase().equals("bye")) {
                // chatbot exit
                System.out.println("\nByebye. Hope to see you again soon!");
                break;
            } else {
                // echo the input
                System.out.println(input + "\n");
            }
        }
        scanner.close();
    }
}

