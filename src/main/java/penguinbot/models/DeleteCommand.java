package penguinbot.models;

import penguinbot.exceptions.PenguinBotException;
import penguinbot.services.TaskList;

public class DeleteCommand extends Command {
    private final int taskIndex;

    public DeleteCommand(Actions action, TaskList tasks, String... args) {
        super(action, tasks);
        this.taskIndex = Integer.parseInt(args[0]);
    }

    @Override
    public String execute() throws PenguinBotException {
        return tasks.deleteTask(taskIndex);
    }
}
