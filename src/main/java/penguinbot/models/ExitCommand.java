package penguinbot.models;

import penguinbot.exceptions.PenguinBotException;
import penguinbot.services.Storage;
import penguinbot.services.TaskList;

public class ExitCommand extends Command {
    private final Storage storage;

    public ExitCommand(Actions action, TaskList tasks, Storage storage) {
        super(action, tasks);
        this.storage = storage;
    }

    @Override
    public String execute() throws PenguinBotException {
        tasks.storeTasksToStorage(storage);
        return "Saved tasks! Exiting...";
    }
}
