import java.util.ArrayList;
import java.util.Scanner;

public class PenguinBot {

    public static void main(String[] args) {
        String initialPrompt = "    ____________________________________________________________\n" +
                "     Hello! I'm PenguinBot\n" +
                "     What can I do for you?\n" +
                "    ____________________________________________________________\n";

        System.out.println(initialPrompt);

        ArrayList<Task> list = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            try {
                if (userInput.equals("bye")) {
                    System.out.println("""
                            ____________________________________________________________
                             Bye. Hope to see you again soon!
                            ____________________________________________________________\
                        """);
                    break;

                } else if (userInput.equals("list")){
                    StringBuilder output = new StringBuilder();
                    int i = 1;
                    while (i <= list.size() && list.get(i - 1) != null) {
                        output.append("     ")
                                .append(i)
                                .append(". ")
                                .append(list.get(i - 1).toString())
                                .append("\n");
                        i++;
                    }
                    System.out.println("    ____________________________________________________________\n" +
                             output + "\n" +
                        "    ____________________________________________________________\n");

                } else {
                    String[] parts = userInput.split("\\s+");
                    String action = parts[0];

                    final String parameters = userInput.substring(action.length()).trim();
                    try {
                        actions parsedAction = actions.valueOf(action.toUpperCase());
                        switch (parsedAction) {
                            case MARK -> {
                                int number = Integer.parseInt(parts[1]);

                                list.get(number - 1).markAsDone();
                                System.out.println("    ____________________________________________________________\n" +
                                        "     Nice! I've marked this task as done:\n" +
                                        "       " + list.get(number - 1).toString() + "\n" +
                                        "    ____________________________________________________________");
                            }
                            case UNMARK -> {
                                int number = Integer.parseInt(parts[1]);

                                list.get(number - 1).markAsUndone();
                                System.out.println("    ____________________________________________________________\n" +
                                        "     OK, I've marked this task as not done yet:\n" +
                                        "       " + list.get(number - 1).toString() + "\n" +
                                        "    ____________________________________________________________");
                            }
                            case DELETE -> {
                                int number = Integer.parseInt(parts[1]) - 1;
                                if (list.get(number) != null) {
                                    Task task = list.get(number);
                                    list.remove(number);
                                    System.out.println("    ____________________________________________________________\n" +
                                            "     Noted. I've removed this task:\n" +
                                            "       " + task.toString() + "\n" +
                                            "     Now you have " + list.size() + " tasks in the list.\n" +
                                            "    ____________________________________________________________");
                                }
                            }
                            case TODO -> {
                                if (parameters.isBlank()) {
                                    throw new PenguinBotException("Todo needs a description.");
                                }
                                ToDo toDo = new ToDo(parameters);
                                list.add(toDo);
                                System.out.println("    ____________________________________________________________\n" +
                                        "     Got it. I've added this task:\n" +
                                        "       " + toDo.toString() + "\n" +
                                        "     Now you have " + list.size() + " tasks in the list.\n" +
                                        "    ____________________________________________________________\n");
                            }
                            case DEADLINE -> {
                                String by = "";
                                String[] bySplit = parameters.split("/by", 2);
                                String description = bySplit[0].trim();
                                if (bySplit.length > 1) {
                                    by = bySplit[1].trim();
                                }
                                Deadline deadlineTask = new Deadline(description, by);
                                list.add(deadlineTask);
                                System.out.println("    ____________________________________________________________\n" +
                                        "     Got it. I've added this task:\n" +
                                        "       " + deadlineTask.toString() + "\n" +
                                        "     Now you have " + list.size() + " tasks in the list.\n" +
                                        "    ____________________________________________________________\n");
                            }
                            case EVENT -> {
                                String startTime = "";
                                String endTime = "";
                                String[] fromSplit = parameters.split("/from", 2);
                                String description = fromSplit[0].trim();
                                if (fromSplit.length > 1) {
                                    String[] toSplit = fromSplit[1].split("/to", 2);
                                    startTime = toSplit[0].trim();
                                    if (toSplit.length > 1) {
                                        endTime = toSplit[1].trim();
                                    }
                                }
                                Event eventTask = new Event(description, startTime, endTime);
                                list.add(eventTask);
                                System.out.println("    ____________________________________________________________\n" +
                                        "     Got it. I've added this task:\n" +
                                        "       " + eventTask.toString() + "\n" +
                                        "     Now you have " + list.size() + " tasks in the list.\n" +
                                        "    ____________________________________________________________\n");
                            }
                            case BYE, LIST -> {
                                // handled earlier; keep no-op here
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        throw new PenguinBotException("Unknown command: " + action);
                    }
                }
            } catch (PenguinBotException e) {
                System.out.println("    ____________________________________________________________\n" +
                        "     " + e.getMessage() + "\n" +
                        "    ____________________________________________________________");
            }
        }
    }
}
