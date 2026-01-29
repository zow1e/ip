import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    
    public Deadline(String description, String dateTime) {
        super(description);
        this.dateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern
                                                       ("yyyy-MM-dd HHmm"));
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + super.getDateTimeString() + ")";
    }
}
