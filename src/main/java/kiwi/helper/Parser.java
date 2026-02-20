/**
 * Parses user input into structured command objects for the Kiwi task manager.
 *
 * Converts raw user input strings into {@link Parser} objects containing the command
 * type and arguments. Supports all Kiwi commands: todo, deadline, event, list, find,
 * mark, unmark, delete, bye.
 *
 * Validates input format and throws {@link KiwiException} for invalid syntax.
 *
 * @author zow1e
 * @see KiwiException
 */
package kiwi.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parse user input into components for easy processing.
 */
public class Parser {

    /**
     * DateTimeFormatter to ensure consistency of data.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private String type;
    private String[] args;

    private Parser(String type, String... args) {
        this.type = type;
        this.args = args;
    }

    /**
     * Parse user input to be handled by different Classes
     *
     * @param input the string command
     * @throws KiwiException if input format is invalid
     */
    public static Parser parse(String input) throws KiwiException {
        String[] parts = input.trim().split("\\s+", 2);
        String cmd = parts[0].toLowerCase();

        switch (cmd) {
        case "todo":
            return parseTodo(parts);

        case "deadline":
            return parseDeadline(parts);

        case "event":
            return parseEvent(parts);

        case "list":
            return new Parser("list");

        case "find":
            return parseFind(parts);

        case "mark":
        case "unmark":
        case "delete":
            return parseIndexCommand(cmd, parts);

        case "help":
            return new Parser("help");

        case "clear":
            return new Parser("clear");

        case "bye":
            return new Parser("bye");

        default:
            throw new KiwiException("Unknown command: '" + cmd + "'\n"
                + "Available: todo, deadline, event, list, find, mark, unmark, delete, bye");
        }
    }

    private static Parser parseTodo(String[] parts) throws KiwiException {
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new KiwiException("Todo description cannot be empty!\n"
                + "Usage: todo <description>");
        }
        return new Parser("todo", parts[1].trim());
    }

    private static Parser parseDeadline(String[] parts) throws KiwiException {
        if (parts.length < 2) {
            throw new KiwiException("Missing deadline details!\n"
                + "Usage: deadline <description> /by yyyy-MM-dd HHmm");
        }

        String[] dlParts = parts[1].split(" /by ", 2);
        if (dlParts.length < 2) {
            throw new KiwiException("Invalid deadline format!\n"
                + "Usage: deadline <description> /by yyyy-MM-dd HHmm\n"
                + "Example: deadline CS2103T /by 2026-02-15 2359");
        }

        validateDateTime(dlParts[1].trim(), "deadline");
        return new Parser("deadline", dlParts[0].trim(), dlParts[1].trim());
    }

    private static Parser parseEvent(String[] parts) throws KiwiException {
        if (parts.length < 2) {
            throw new KiwiException("Missing event details!\n"
                + "Usage: event <description> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
        }

        String eventInput = parts[1];
        if (!eventInput.contains("/from") || !eventInput.contains("/to")) {
            throw new KiwiException("Invalid event format!\n"
                + "Usage: event <description> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm\n"
                + "Example: event meeting /from 2026-02-12 1400 /to 2026-02-12 1600");
        }

        String[] evParts = eventInput.split(" /from | /to ", -1);
        if (evParts.length < 3) {
            throw new KiwiException("Event needs both /from and /to!\n"
                + "Example: event meeting /from 2026-02-12 1400 /to 2026-02-12 1600");
        }

        validateDateTime(evParts[1].trim(), "event /from");
        validateDateTime(evParts[2].trim(), "event /to");
        return new Parser("event", evParts[0].trim(), evParts[1].trim(), evParts[2].trim());
    }

    private static Parser parseFind(String[] parts) throws KiwiException {
        if (parts.length < 2) {
            throw new KiwiException("Find needs a keyword!\nUsage: find <keyword>");
        }
        return new Parser("find", parts[1].trim());
    }

    private static Parser parseIndexCommand(String cmd, String[] parts) throws KiwiException {
        if (parts.length < 2) {
            throw new KiwiException(cmd.toUpperCase() + " needs task number!\nUsage: " + cmd + " <number>");
        }

        try {
            int index = Integer.parseInt(parts[1].trim());
            if (index < 1) {
                throw new KiwiException("Task number must be 1 or higher!");
            }
            return new Parser(cmd, String.valueOf(index));
        } catch (NumberFormatException e) {
            throw new KiwiException("Invalid task number: '" + parts[1] + "'\nEnter a number like '1', '2', etc.");
        }
    }

    /**
     * Validates date/time string format.
     * Accepts: yyyy-MM-dd HHmm (full datetime) or HHmm (time only, assumes today)
     *
     * @param dateTimeStr the date/time string to validate
     * @param context description of where this datetime is used (e.g., "deadline")
     * @throws KiwiException if format is invalid
     */
    private static void validateDateTime(String dateTimeStr, String context) throws KiwiException {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            throw new KiwiException("Empty " + context + " date/time!");
        }

        String trimmed = dateTimeStr.trim();

        try {
            // Try time-only format (HHmm)
            if (trimmed.length() == 4 && trimmed.matches("\\d{4}")) {
                String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDateTime.parse(todayStr + " " + trimmed, DATE_TIME_FORMATTER);
                return;
            }

            // Try full datetime format (yyyy-MM-dd HHmm)
            LocalDateTime.parse(trimmed, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new KiwiException("Invalid " + context + ": '" + trimmed + "'\n"
                + "Use: yyyy-MM-dd HHmm (e.g. 2026-02-15 2359)\n"
                + "Or:  HHmm (e.g. 2359)");
        }
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

