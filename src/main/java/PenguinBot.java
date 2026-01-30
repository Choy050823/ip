import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PenguinBot {

    public static void main(String[] args) throws IOException {
        String initialPrompt = """
                    ____________________________________________________________
                     Hello! I'm PenguinBot
                     What can I do for you?
                    ____________________________________________________________
                """;

        System.out.println(initialPrompt);

        ArrayList<Task> list = new ArrayList<>();

        // Load the file
        String filePathString = "./src/data/TaskList.txt";
        Path filePath = Paths.get(filePathString);
        Path parentDir = filePath.getParent();

        // Handle file not found problem
        try {
            if (parentDir != null) {
                Files.createDirectories(parentDir);
                System.out.println("Directory ensured: " + parentDir.toString());
            }

            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory/file: " + e.getMessage());
            e.printStackTrace();
        }

        File textListFile = new File(filePathString);
        Scanner fileScanner = new Scanner(textListFile);

        // Load the string into task objects
        while (fileScanner.hasNext()) {
            String taskString = fileScanner.nextLine();
            String[] parts = taskString.split(" \\| ");
            // decide which type of task it is
            switch (parts[0]) {
                case "T":
                    // It is a TODO task
                    Task todoTask = new ToDo(parts[2]);
                    list.add(todoTask);
                    break;

                case "D":
                    // It is a Deadline task
                    String BY_PREFIX = "by: ";
                    Task deadlineTask = new Deadline(
                            parts[2],
                            parts[3].substring(BY_PREFIX.length())
                    );

                    list.add(deadlineTask);
                    break;

                case "E":
                    // It is an Event task
                    Pattern pattern = Pattern.compile("from: (.*?) to: (.*)");
                    Matcher matcher = pattern.matcher(parts[3]);

                    Task eventTask = new Event(
                            parts[2],
                            matcher.group(1),
                            matcher.group(2)
                    );

                    list.add(eventTask);
                    break;

                default:
                    System.out.println("Not a valid task");
            }
        }

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            try {
                if (userInput.equals("bye")) {
                    // Write the list into the file
                    try {
                        FileWriter fw = new FileWriter(filePathString, false);
                        for (Task task : list) {
                            fw.write(task.toString());
                            fw.close();
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

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
                                        "       " + toDo + "\n" +
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
                                        "       " + deadlineTask + "\n" +
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
                                        "       " + eventTask + "\n" +
                                        "     Now you have " + list.size() + " tasks in the list.\n" +
                                        "    ____________________________________________________________\n");
                            }
                            case BYE, LIST -> {
                                
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
