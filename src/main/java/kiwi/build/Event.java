package kiwi.build;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private LocalDateTime fromTime;
    private LocalDateTime toTime;

    public Event(String description, String from, String to) {
        super(description);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        this.fromTime = LocalDateTime.parse(from, fmt);
        this.toTime = LocalDateTime.parse(to, fmt);
        this.dateTime = fromTime;
    }

    public LocalDateTime getFrom() {
        return fromTime; 
    }
    public LocalDateTime getTo() { 
        return toTime; 
    }

    @Override
    public String toString() {
        String date = dateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        String fromStr = fromTime.format(DateTimeFormatter.ofPattern("HHmm"));
        String toStr = toTime.format(DateTimeFormatter.ofPattern("HHmm"));
        return "[E]" + super.toString() + " (at: " + date + " " + fromStr + " - " + toStr + ")";
    }
}
