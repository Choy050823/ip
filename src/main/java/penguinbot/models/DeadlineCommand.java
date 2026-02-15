package penguinbot.models;

import penguinbot.exceptions.PenguinBotException;
import penguinbot.services.TaskList;

import java.time.LocalDateTime;

import static penguinbot.services.Parser.parseUserDateTime;

public class DeadlineCommand extends Command {
    private final String taskDescription;
    private final String deadlineRawString;

    public DeadlineCommand(Actions action, TaskList tasks, String... args) {
        super(action, tasks);
        this.taskDescription = args[0];
        this.deadlineRawString = args[1];
    }

    @Override
    public String execute() {
        try {
            LocalDateTime deadlineDateTime = parseUserDateTime(deadlineRawString, "deadline");

            return tasks.addTask(new Deadline(taskDescription, deadlineDateTime));

        } catch (PenguinBotException e) {
            return e.getMessage();
        }
    }
}
