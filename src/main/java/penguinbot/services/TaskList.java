package penguinbot.services;

import penguinbot.exceptions.PenguinBotException;
import penguinbot.models.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> tasks;

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        System.out.println("    ____________________________________________________________\n" +
                "     Got it. I've added this task:\n" +
                "       " + task + "\n" +
                "     Now you have " + tasks.size() + " tasks in the list.\n" +
                "    ____________________________________________________________\n");
    }

    public void deleteTask(int number) {
        if (tasks.get(number - 1) != null) {
            Task task = tasks.get(number - 1);
            tasks.remove(number - 1);
            System.out.println("    ____________________________________________________________\n" +
                    "     Noted. I've removed this task:\n" +
                    "       " + task.toString() + "\n" +
                    "     Now you have " + tasks.size() + " tasks in the list.\n" +
                    "    ____________________________________________________________");
        }
    }

    public void markTask(int number) {
        tasks.get(number - 1).markAsDone();
        System.out.println("    ____________________________________________________________\n" +
                "     OK, I've marked this task as not done yet:\n" +
                "       " + tasks.get(number - 1).toString() + "\n" +
                "    ____________________________________________________________");
    }

    public void unmarkTask(int number) {
        tasks.get(number - 1).markAsUndone();
        System.out.println("    ____________________________________________________________\n" +
                "     Nice! I've marked this task as done:\n" +
                "       " + tasks.get(number - 1).toString() + "\n" +
                "    ____________________________________________________________");
    }

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
        System.out.println("    ____________________________________________________________\n" +
                output + "\n" +
                "    ____________________________________________________________\n");
    }

    public void storeTasksToStorage(Storage storage) throws PenguinBotException {
        storage.storeTasks(tasks);
    }

    public void findTasks(String keyword) {
        System.out.println("    ____________________________________________________________");
        System.out.println("     Here are the matching tasks in your list:");
        int count = 1;
        for (Task task : tasks) {
            if (task.toString().contains(keyword)) {
                System.out.println("     " + count + "." + task.toString());
                count++;
            }
        }
        System.out.println("    ____________________________________________________________");
    }
}
