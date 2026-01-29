public class Task {
    
    protected String description;
    protected boolean isDone;

    // constructor
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    // display task status
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    // mark as done 
    public void markTask() {
        this.isDone = true;
    }

    // mark as not done 
    public void unmarkTask() {
        this.isDone = false;
    }
    
    // display description
    public String toString() {
        String fullDesc = "[" + getStatusIcon() + "] " + description.toString();
        return fullDesc;
    }
}
