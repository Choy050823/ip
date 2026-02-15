package penguinbot.models;

import penguinbot.services.TaskList;

public class ListCommand extends Command{
    public ListCommand(Actions action, TaskList tasks) {
        super(action, tasks);
    }

    @Override
    public String execute() {
        return tasks.printTasks();
    }
}
