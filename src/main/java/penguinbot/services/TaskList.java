package penguinbot.services;

import penguinbot.exceptions.PenguinBotException;
import penguinbot.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Maintains an in-memory list of tasks and provides task operations.
 */
public class TaskList {
    /** Internal task collection. */
    private final List<Task> tasks;

    /**
     * Creates a task list seeded with existing tasks.
     *
     * @param tasks tasks to manage.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return mutable task list.
     */
    public List<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Adds a task and prints confirmation.
     *
     * @param task task to add.
     */
    public String addTask(Task task) {
        if (isDuplicateTask(task)) {
            return "It is a duplicate task, aborting...";
        }

        int taskListSize = tasks.size();
        this.tasks.add(task);
        assert tasks.size() == taskListSize + 1;

        return
                "Got it. I've added this task:\n"
                + task + "\n"
                + "Now you have " + tasks.size() + " tasks in the list."
        ;
    }

    private boolean isDuplicateTask(Task newTask) {
        List<Boolean> wasDone = tasks.stream()
                .map(task -> "1".equals(task.getStatusIcon()))
                .toList();

        tasks.forEach(Task::markAsUndone);

        boolean isDuplicate = tasks
                .stream()
                .anyMatch(task -> Objects.equals(task.toString(), newTask.toString()));

        for (int i =0; i < tasks.size(); i++) {
            if (wasDone.get(i)) {
                tasks.get(i).markAsDone();
            }
        }

        return isDuplicate;
    }

    /**
     * Deletes a task by 1-based index and prints confirmation.
     *
     * @param number 1-based index of task to remove.
     */
    public String deleteTask(int number) throws PenguinBotException {
        if (tasks.get(number - 1) != null) {
            Task task = tasks.get(number - 1);
            int taskListSize = tasks.size();
            tasks.remove(number - 1);
            assert tasks.size() == taskListSize + 1;

            return
                    "Noted. I've removed this task:\n"
                            + task.toString() + "\n"
                            + "Now you have " + tasks.size() + " tasks in the list."
                    ;
        }

        throw new PenguinBotException("Unable to delete task!");
    }


    /**
     * Marks a task as done by 1-based index.
     *
     * @param number 1-based index of task to mark.
     */
    public String markTask(int number) {
        Task task = tasks.get(number - 1);
        task.markAsDone();

        String taskStatusIcon = task.getStatusIcon();
        assert Objects.equals(taskStatusIcon, "1");

        return
                "OK, I've marked this task as not done yet:\n"
                + tasks.get(number - 1).toString()
        ;
    }

    /**
     * Marks a task as not done by 1-based index.
     *
     * @param number 1-based index of task to unmark.
     */
    public String unmarkTask(int number) {
        Task task = tasks.get(number - 1);
        task.markAsUndone();

        String taskStatusIcon = tasks.get(number - 1).getStatusIcon();
        assert Objects.equals(taskStatusIcon, "0");

        return
                "Nice! I've marked this task as undone:\n" + task
        ;
    }

    /**
     * Prints all tasks with indices to standard output.
     */
    public String printTasks() {
        StringBuilder output = new StringBuilder();
        int i = 1;

        while (i <= tasks.size() && tasks.get(i - 1) != null) {
            output
                    .append(i)
                    .append(". ")
                    .append(tasks.get(i - 1).toString())
                    .append("\n");
            i++;
        }
        return output.toString();
    }

    /**
     * Persists tasks using the provided storage instance.
     *
     * @param storage storage handler.
     * @throws PenguinBotException when persistence fails.
     */
    public void storeTasksToStorage(Storage storage) throws PenguinBotException {
        storage.storeTasks(tasks);
    }

    public String findTasks(String keyword) {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");
        int count = 1;
        for (Task task : tasks) {
            if (task.toString().contains(keyword)) {
                sb
                        .append(count)
                        .append(". ")
                        .append(task)
                        .append("\n");
                count++;
            }
        }

        return sb.toString();
    }
}
