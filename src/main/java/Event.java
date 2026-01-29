import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task{
    
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        
        this.dateTime = LocalDateTime.parse
                       (from, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        this.from = from.split(" ")[1];
        this.to = to;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }


    @Override
    public String toString() {
        String date = dateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        return "[E]"+super.toString() +" (at: "+ date  + " " + from + " to " + to + ")";
    }
}
