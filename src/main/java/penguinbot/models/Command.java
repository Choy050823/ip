package penguinbot.models;

import penguinbot.exceptions.PenguinBotException;
import penguinbot.services.TaskList;

public abstract class Command {
    protected Actions action;
    protected TaskList tasks;

    public Command(Actions action, TaskList tasks) {
        this.action = action;
        this.tasks = tasks;
    }

    public abstract String execute() throws PenguinBotException;
}
