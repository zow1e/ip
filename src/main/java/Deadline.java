public class Deadline extends Task {
    private String dueDate;
    
    public Deadline(String description, String dueDate) {
        super(description);
        this.dueDate = dueDate;
    }

    public String getDate() {
        return this.dueDate;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + "(by: " + this.dueDate + ")";
    }
}
