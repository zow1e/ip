/**
 * Parser class for parsing user input.
 * 
 * Identify function from user input.
 * 
 */

package kiwi.helper;
public class Parser {
    
    public static Parser parse(String input) throws KiwiException {
        String[] parts = input.trim().split("\\s+", 2);
        String cmd = parts[0].toLowerCase();
        
        switch (cmd) {
            case "todo":
                if (parts.length < 2 || parts[1].trim().isEmpty()) {
                    throw new KiwiException("Todo description cannot be empty");
                }
                return new Parser("todo", parts[1].trim());
                
            case "deadline":
                if (parts.length < 2) {
                    throw new KiwiException("Deadline format: deadline <task> /by <date>");
                }
                String[] dlParts = parts[1].split(" /by ", 2);
                if (dlParts.length < 2) {
                    throw new KiwiException("Deadline format: deadline <task> /by <date>");
                }
                return new Parser("deadline", dlParts[0].trim(), dlParts[1].trim());
                
            case "event":
                if (parts.length < 2) {
                    throw new KiwiException("Event format: event <task> /from <time> /to <time>");
                }
                String[] evParts = parts[1].split(" /from | /to ", -1);
                if (evParts.length < 3) {
                    throw new KiwiException("Event format: event <task> /from <time> /to <time>");
                }
                return new Parser("event", evParts[0].trim(), evParts[1].trim(), evParts[2].trim());
                
            case "list":
                return new Parser("list");
                
            case "mark":
            case "unmark":
            case "delete":
                if (parts.length < 2) {
                    throw new KiwiException(cmd + " needs task number");
                }
                try {
                    int index = Integer.parseInt(parts[1].trim());
                    return new Parser(cmd, String.valueOf(index));
                } catch (NumberFormatException e) {
                    throw new KiwiException("Invalid task number: " + parts[1]);
                }
                
            case "bye":
                return new Parser("bye");
                
            default:
                throw new KiwiException("Unknown command: " + cmd);
        }
    }
    

    private String type;
    private String[] args;
    
    // private constructor
    private Parser(String type, String... args) {  
        this.type = type;
        this.args = args;
    }
    
    public String getType() { 
        return type; 
    }
    
    public String[] getArgs() { 
        return args; 
    }
    
    public String getArg(int i) { 
        return i < args.length ? args[i] : ""; 
    }
}
