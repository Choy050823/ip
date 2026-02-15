package penguinbot.models;

import penguinbot.exceptions.PenguinBotException;
import penguinbot.services.TaskList;

import java.time.LocalDateTime;

import static penguinbot.services.Parser.parseUserDateTime;

public class EventCommand extends Command {
    private final String taskDescription;
    private final String startDateTimeRawString;
    private final String endDateTimeRawString;

    public EventCommand(Actions action, TaskList tasks, String... args) {
        super(action, tasks);
        this.taskDescription = args[0];
        this.startDateTimeRawString = args[1];
        this.endDateTimeRawString = args[2];
    }

    @Override
    public String execute() throws PenguinBotException {
        LocalDateTime startDateTime = parseUserDateTime(startDateTimeRawString, "event start");
        LocalDateTime endDateTime = parseUserDateTime(endDateTimeRawString, "event end");

        return tasks.addTask(new Event(taskDescription, startDateTime, endDateTime));
    }
}
