package penguinbot.models;

import penguinbot.services.TaskList;

public class FindCommand extends Command {
    private final String searchKeyword;
    public FindCommand(Actions action, TaskList tasks, String... args) {
        super(action, tasks);
        this.searchKeyword = args[0];
    }

    @Override
    public String execute() {
        return tasks.findTasks(searchKeyword);
    }
}
