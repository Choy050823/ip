package penguinbot.models;

import penguinbot.services.TaskList;

public class ToDoCommand extends Command {
    private final String taskDescription;

    public ToDoCommand(Actions action, TaskList tasks, String... args) {
        super(action, tasks);
        this.taskDescription = args[0];
    }

    @Override
    public String execute() {
        return tasks.addTask(new ToDo(taskDescription));
    }
}
