package penguinbot.models;

import java.time.LocalDateTime;

public class Deadline extends Task {
    protected LocalDateTime deadline;

    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String getTaskType() {
        return "D";
    }

    @Override
    public String toString() {
        return "D | " + super.toString() + " | by: " + deadline;
    }
}
