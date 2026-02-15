package penguinbot.models;

import penguinbot.services.TaskList;

public class MarkCommand extends Command{
    private final int taskIndex;

    public MarkCommand(Actions action, TaskList tasks, String... args) {
        super(action, tasks);
        this.taskIndex = Integer.parseInt(args[0]);
    }

    @Override
    public String execute() {
        return this.tasks.markTask(taskIndex);
    }
}
