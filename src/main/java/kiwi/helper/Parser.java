/**
 * Parses user input into structured command objects for the Kiwi task manager.
 * 
 * Converts raw user input strings into {@link Parser} objects containing the command
 * type and arguments. Supports all Kiwi commands: todo, deadline, event, list, find,
 * mark, unmark, delete, bye.
 * 
 * Validates input format and throws {@link KiwiException} for invalid syntax.
 * 
 * Example usage:
 * <pre>{@code
 * Parser parser = Parser.parse("todo buy groceries");
 * System.out.println(parser.getType());  // "todo"
 * System.out.println(parser.getArg(0));  // "buy groceries"
 * 
 * Parser dlParser = Parser.parse("deadline submit /by 2026-02-05 2359");
 * System.out.println(dlParser.getArg(1));  // "2026-02-05 2359"
 * 
 * try {
 *     Parser.parse("todo");  // throws KiwiException
 * } catch (KiwiException e) {
 *     // handle "Todo description cannot be empty"
 * }
 * }</pre>
 * 
 * @author [zow1e]
 * @see KiwiException
 */
package kiwi.helper;

public class Parser {

    /** The command type parsed from user input (e.g., "todo", "deadline"). */
    private String type;
    
    /** Arguments parsed from user input. */
    private String[] args;

    /**
     * Parses the given user input into a structured Parser object.
     * 
     * Supports all Kiwi commands with format validation:
     * <ul>
     * <li>todo &lt;description&gt;</li>
     * <li>deadline &lt;description&gt; /by &lt;date&gt;</li>
     * <li>event &lt;description&gt; /from &lt;time&gt; /to &lt;time&gt;</li>
     * <li>list</li>
     * <li>find &lt;keyword&gt;</li>
     * <li>mark/unmark/delete &lt;number&gt;</li>
     * <li>bye</li>
     * </ul>
     * 
     * @param input raw user input string (trimmed)
     * @return Parser object containing command type and arguments
     * @throws KiwiException if input format is invalid or command unknown
     */
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
            
            case "find":
                return new Parser("find");

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

    /**
     * Private constructor for creating Parser instances.
     * 
     * Stores the command type and variable number of arguments using varargs.
     * Intended for internal use by the static parse() method only.
     * 
     * @param type command type (e.g., "todo", "deadline")
     * @param args command arguments (variable length)
     */
    private Parser(String type, String... args) {
        this.type = type;
        this.args = args;
    }

    /**
     * Returns the command type parsed from user input.
     * 
     * @return command type string (e.g., "todo", "list", "bye")
     */
    public String getType() {
        return type;
    }

    /**
     * Returns all arguments parsed from user input.
     * 
     * @return array of argument strings
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Returns the argument at the specified index, or empty string if out of bounds.
     * 
     * Safe accessor that prevents ArrayIndexOutOfBoundsException.
     * 
     * @param i argument index (0-based)
     * @return argument at index i, or empty string if i >= args.length
     */
    public String getArg(int i) {
        return i < args.length ? args[i] : "";
    }
}
