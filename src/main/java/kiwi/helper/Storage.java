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

        // nothing to load if folder/file do not exist
        if (!dir.exists()) {
            return taskList;
        }
        if (!file.exists()) {
            return taskList;
        }

        try {
            // if exists, load existing data
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                // expected format: type | doneBoolean | description | *date
                String line = s.nextLine().trim();
                String[] parts = line.split("\\|", -1); // -1 = keep empty parts
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                // if corrupted data, skip the line
                if (parts.length < 3) {
                    continue;
                }

                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                String description = parts[2];

                Task currTask;
                switch (type.toUpperCase()) {
                case "T":
                    currTask = new ToDo(description);
                    break;
                case "D":
                    // Deadline: type | done | desc | date
                    if (parts.length < 4) {
                        continue; // corrupted: missing date
                    }
                    currTask = new Deadline(description, parts[3].trim());
                    break;
                case "E":
                    // Event: type | done | desc | date/time
                    if (parts.length < 4) {
                        continue;
                    }
                    String eventDetails = parts[3].trim();
                    String[] dateParts = eventDetails.split("\\s+to\\s+", 2);
                    if (dateParts.length < 2) {
                        continue;
                    }

                    String fromFull = dateParts[0].trim(); // "2026-01-31 1430"
                    String toTime = dateParts[1].trim(); // "1600"

                    String[] fromParts = fromFull.split(" ");
                    String toFull = fromParts[0] + " " + toTime; // "2026-01-31 1600"

                    currTask = new Event(description, fromFull, toFull);
                    break;
                default:
                    continue; // unknown type: skip
                }

                if (isDone) {
                    currTask.markTask();
                }
                taskList.add(currTask);
            }
            s.close();
        } catch (IOException e) {
            // if no input, create empty list
            taskList = new ArrayList<>();
        }

        return taskList;
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
            File dir = new File(this.dirPath);
            // create data directory if it doesnt exist
            if (!dir.exists()) {
                dir.mkdir();
            }

            // write the data to text file
            FileWriter fw = new FileWriter(filePath);
            for (Task task : taskList) {
                String status = task.getStatusIcon();
                String doneBoolean = (status.equals("X")) ? "1" : "0";

                if (task instanceof ToDo) {
                    // T | done | description
                    fw.write("T | " + doneBoolean + " | " + task.getDescription() + "\n");
                } else if (task instanceof Deadline) {
                    // D | done | description | date
                    Deadline dl = (Deadline) task;
                    String dueDate = dl.getDateTime()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                    fw.write("D | " + doneBoolean + " | " + task.getDescription() + " | "
                            + dueDate + "\n");
                } else if (task instanceof Event) {
                    // E | done | description | from-to
                    Event ev = (Event) task;
                    String eventDate = ev.getDateTime()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String timeRange = ev.getFrom() + " to " + ev.getTo();
                    fw.write("E | " + doneBoolean + " | " + task.getDescription() + " | "
                            + eventDate + " " + timeRange + "\n");
                }
            }
            fw.close();
        } catch (IOException e) {
            throw new KiwiException("Unable to save this task");
        }
    }
}
