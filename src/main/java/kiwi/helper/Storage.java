/**
 * Manages persistent storage of tasks in a text file for the Kiwi task manager.
 *
 * Handles loading tasks from `kiwi.txt` and saving tasks back to it using a custom
 * pipe-delimited format. Supports all task types: {@link ToDo}, {@link Deadline},
 * {@link Event}.
 *
 * @author zow1e
 * @see Task
 * @see ToDo
 * @see Deadline
 * @see Event
 */
package kiwi.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import kiwi.build.Deadline;
import kiwi.build.Event;
import kiwi.build.Task;
import kiwi.build.ToDo;

/**
 * Handles task persistence in file storage.
 */
public class Storage {

    /** Directory path for storing Kiwi data files. */
    private String dirPath;

    /** Full file path for the kiwi.txt data file. */
    private String filePath;

    /**
     * Constructs a Storage instance with specified directory and file paths.
     *
     * Paths are stored for use in load/save operations. Directory is created
     * automatically during save if missing.
     *
     * @param dirPath directory path (e.g., "./data")
     * @param filePath full file path (e.g., "./data/kiwi.txt")
     */
    public Storage(String dirPath, String filePath) {
        this.dirPath = dirPath;
        this.filePath = filePath;
    }

    /**
     * Loads all tasks from the kiwi.txt file into an ArrayList.
     *
     * Parses pipe-delimited lines and reconstructs Task objects:
     * <ul>
     * <li>T | done | description</li>
     * <li>D | done | description | date</li>
     * <li>E | done | description | date from-to</li>
     * </ul>
     *
     * Skips corrupted lines and returns empty list if file/directory missing.
     * Marks tasks as done based on stored status.
     *
     * @return ArrayList containing all valid tasks from file
     */
    public ArrayList<Task> loadTasks() {
        File dir = new File(this.dirPath);
        File file = new File(this.filePath);

        ArrayList<Task> taskList = new ArrayList<>();

        if (!dir.exists() || !file.exists()) {
            return taskList;
        }

        try (Scanner s = new Scanner(file)) {
            while (s.hasNextLine()) {
                Task loadedTask = parseTaskLine(s.nextLine());
                if (loadedTask != null) {
                    taskList.add(loadedTask);
                }
            }
        } catch (IOException e) {
            return new ArrayList<>();
        }

        return taskList;
    }

    /**
     * Parses a single line from the storage file into a Task object.
     *
     * Expected format: type | doneBoolean | description | *date
     * Skips corrupted lines silently during load.
     *
     * @param line the line to parse
     * @return Task object if valid, null if corrupted
     */
    private Task parseTaskLine(String line) {
        String[] parts = line.split("\\|", -1);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task = createTaskByType(type, description, parts);
        if (task == null) {
            return null;
        }

        if (isDone) {
            task.markTask();
        }
        return task;
    }

    /**
     * Creates a Task object based on type and parts.
     *
     * @param type task type (T, D, E)
     * @param description task description
     * @param parts parsed line parts from storage file
     * @return Task object if valid, null if corrupted
     */
    private Task createTaskByType(String type, String description, String[] parts) {
        try {
            switch (type.toUpperCase()) {
            case "T":
                return new ToDo(description);

            case "D":
                if (parts.length < 4) {
                    return null; // corrupted: missing date
                }
                return new Deadline(description, parts[3]);

            case "E":
                return parseEventTask(description, parts);

            default:
                return null; // unknown type
            }
        } catch (KiwiException e) {
            // Skip corrupted tasks during load
            return null;
        } catch (IllegalArgumentException e) {
            // Skip tasks with invalid format during load
            return null;
        }
    }

    /**
     * Parses an Event task from storage format.
     *
     * @param description task description
     * @param parts parsed line parts from storage file
     * @return Event task if valid, null if corrupted
     * @throws KiwiException if time input is invalid
     * @throws IllegalArgumentException if event validation fails
     */
    private Task parseEventTask(String description, String[] parts) throws KiwiException {
        if (parts.length < 4) {
            return null;
        }

        String eventDetails = parts[3].trim();
        String[] dateParts = eventDetails.split("\\s+to\\s+", 2);
        if (dateParts.length < 2) {
            return null;
        }

        String fromFull = dateParts[0].trim();
        String toTime = dateParts[1].trim();
        String[] fromParts = fromFull.split(" ");

        if (fromParts.length == 0) {
            return null;
        }

        String toFull = fromParts[0] + " " + toTime;

        try {
            return new Event(description, fromFull, toFull);
        } catch (KiwiException e) {
            // Re-throw for proper handling in createTaskByType
            throw e;
        }
    }

    /**
     * Saves the given task list to the kiwi.txt file.
     *
     * Creates data directory if missing. Writes tasks in pipe-delimited format.
     * Overwrites existing file completely.
     *
     * @param taskList list of tasks to save
     * @throws KiwiException if file I/O fails
     */
    public void saveTasks(ArrayList<Task> taskList) throws KiwiException {
        try {
            File dataDir = new File(this.dirPath);
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }
            if (taskList.isEmpty()) {
                return;
            }

            try (FileWriter fw = new FileWriter(filePath);
                 PrintWriter pw = new PrintWriter(fw)) {
                taskList.stream()
                    .map(this::taskToPipeString)
                    .forEach(pw::println);
            }
        } catch (IOException e) {
            throw new KiwiException("Unable to save tasks to file");
        }
    }

    /**
     * Converts a Task object to pipe-delimited storage format.
     *
     * Format:
     * <ul>
     * <li>T | done | description</li>
     * <li>D | done | description | date</li>
     * <li>E | done | description | from date to time</li>
     * </ul>
     *
     * @param task the task to convert
     * @return formatted string for storage
     */
    private String taskToPipeString(Task task) {
        String isDone = task.getStatusIcon().equals("X") ? "1" : "0";

        if (task instanceof ToDo) {
            return String.format("T | %s | %s", isDone, task.getDescription());
        }

        if (task instanceof Deadline) {
            Deadline dl = (Deadline) task;
            String dueDate = dl.getDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            return String.format("D | %s | %s | %s", isDone, task.getDescription(), dueDate);
        }

        if (task instanceof Event) {
            Event ev = (Event) task;
            String eventDate = ev.getFrom()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String fromTime = ev.getFrom()
                    .format(DateTimeFormatter.ofPattern("HHmm"));
            String toTime = ev.getTo()
                    .format(DateTimeFormatter.ofPattern("HHmm"));
            String timeRange = String.format("%s %s to %s", eventDate, fromTime, toTime);
            return String.format("E | %s | %s | %s", isDone, task.getDescription(), timeRange);
        }

        return "";
    }
}
