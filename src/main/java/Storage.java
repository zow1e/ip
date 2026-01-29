import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class Storage {
    private String dirPath;
    private String filePath;

    public Storage(String dirPath, String filePath) {
        this.dirPath = dirPath;
        this.filePath = filePath;
    }

    public ArrayList<Task> loadTasks() {
        File dir = new File(this.dirPath);
        File file = new File(this.filePath);

        ArrayList<Task> taskList = new ArrayList<>();

        // nothing to load if folder/file do not exist
        if (!dir.exists()) return taskList;
        if (!file.exists()) return taskList;

        try {
            // if exists, load existing data
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                // expected format: type | doneBoolean | description | *date
                String line = s.nextLine().trim();
                String[] parts = line.split("\\|", -1);  // -1 = keep empty parts
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                // if corrupted data, skip the line
                if (parts.length < 3) continue;

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
                            continue;  // corrupted: missing date
                        }
                        currTask = new Deadline(description, parts[3].trim());
                        break;
                    case "E":
                        // Event: type | done | desc | date/time
                        if (parts.length < 4) {
                            continue;  // corrupted: missing date/time
                        }
                        // parse date/time from parts[3] e.g., "Aug 6th 2 to 4pm"
                            String eventDetails = parts[3].trim();
    
                            // time
                            String[] dateParts = eventDetails.split("to");
                            if (dateParts.length < 2) {
                                continue;  // corrupted: not enough parts for date + time
                            }
                            
                            // time is stored in the last portion
                            
                            String from = dateParts[0].trim();
                            String to = dateParts[1].trim();
                            
                            currTask = new Event(description, from, to);
                        break;
                    default:
                        continue;  // unknown type: skip
                }

                if (isDone) currTask.markTask();
                taskList.add(currTask);
            }
            s.close();
        } catch (IOException e) {
            // if no input, create empty list
            taskList = new ArrayList<>();
        }

        return taskList;

    }


    public void saveTasks(ArrayList<Task> taskList) throws KiwiException {
        try {
            File dir = new File(this.dirPath);
            // create data directory if it doesnt exist
            if (!dir.exists()) dir.mkdir();

            // write the data to text file
            FileWriter fw = new FileWriter(filePath);
            for (Task task : taskList) {
                String status = task.getStatusIcon();
                String doneBoolean = (status.equals("X")) ? "1" : "0";

                if (task instanceof ToDo) {
                    // T | done | description
                    fw.write("T | " + doneBoolean + " | " + task.description + "\n");
                } else if (task instanceof Deadline) {
                    // D | done | description | date
                    Deadline dl = (Deadline) task;
                    String dueDate = dl.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                    fw.write("D | " + doneBoolean + " | " + task.description + " | " + dueDate + "\n");
                } else if (task instanceof Event) {
                    // E | done | description | from-to
                    Event ev = (Event) task;
                    String eventDate = ev.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                    String timeRange = ev.getFrom() + " to " + ev.getTo();
                    fw.write("E | " + doneBoolean + " | " + task.description + " | " + eventDate + " " + timeRange + "\n");
                }
            }
            fw.close();
        } catch (IOException e) {
            throw new KiwiException("Unable to save this task );");
        }
    }
}
