package penguinbot.models;

import penguinbot.services.TaskList;

public class UnmarkCommand extends Command {
    private final int taskIndex;

    public UnmarkCommand(Actions action, TaskList tasks, String... args) {
        super(action, tasks);
        this.taskIndex = Integer.parseInt(args[0]);
    }

    @Override
    public String execute() {
        return this.tasks.unmarkTask(taskIndex);
    }
}
