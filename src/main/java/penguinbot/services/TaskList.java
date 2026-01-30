package penguinbot.services;

import penguinbot.exceptions.PenguinBotException;
import penguinbot.models.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains an in-memory list of tasks and provides task operations.
 */
public class TaskList {
    /** Internal task collection. */
    private List<Task> tasks;

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
    public void addTask(Task task) {
        this.tasks.add(task);
        System.out.println(
                "    ____________________________________________________________\n"
                + "     Got it. I've added this task:\n"
                + "       " + task + "\n"
                + "     Now you have " + tasks.size() + " tasks in the list.\n"
                + "    ____________________________________________________________\n"
        );
    }

    /**
     * Deletes a task by 1-based index and prints confirmation.
     *
     * @param number 1-based index of task to remove.
     */
    public void deleteTask(int number) {
        if (tasks.get(number - 1) != null) {
            Task task = tasks.get(number - 1);
            tasks.remove(number - 1);
            System.out.println(
                    "    ____________________________________________________________\n"
                    + "     Noted. I've removed this task:\n"
                    + "       " + task.toString() + "\n"
                    + "     Now you have " + tasks.size() + " tasks in the list.\n"
                    + "    ____________________________________________________________"
            );
        }
    }

    /**
     * Marks a task as done by 1-based index.
     *
     * @param number 1-based index of task to mark.
     */
    public void markTask(int number) {
        tasks.get(number - 1).markAsDone();
        System.out.println(
                "    ____________________________________________________________\n"
                + "     OK, I've marked this task as not done yet:\n"
                + "       " + tasks.get(number - 1).toString() + "\n"
                + "    ____________________________________________________________"
        );
    }

    /**
     * Marks a task as not done by 1-based index.
     *
     * @param number 1-based index of task to unmark.
     */
    public void unmarkTask(int number) {
        tasks.get(number - 1).markAsUndone();
        System.out.println(
                "    ____________________________________________________________\n"
                + "     Nice! I've marked this task as done:\n"
                + "       " + tasks.get(number - 1).toString() + "\n"
                + "    ____________________________________________________________"
        );
    }

    /**
     * Prints all tasks with indices to standard output.
     */
    public void printTasks() {
        StringBuilder output = new StringBuilder();
        int i = 1;
        while (i <= tasks.size() && tasks.get(i - 1) != null) {
            output.append("     ")
                    .append(i)
                    .append(". ")
                    .append(tasks.get(i - 1).toString())
                    .append("\n");
            i++;
        }
        System.out.println(
                "    ____________________________________________________________\n"
                + output + "\n"
                + "    ____________________________________________________________\n"
        );
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
}
