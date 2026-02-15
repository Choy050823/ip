package penguinbot.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Event extends Task {
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Event(String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String getTaskType() {
        return "E";
    }

    @Override
    public String toString() {
        return "E | " + super.toString()
                + " | from: " + startTime.toString() + " to: " + endTime.toString();
    }
}
