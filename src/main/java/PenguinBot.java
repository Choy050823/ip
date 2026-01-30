import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PenguinBot {

    private final Storage storage;

    private TaskList tasks;

    private final Ui ui;

    private static final DateTimeFormatter STORAGE_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public PenguinBot(String filePathString) {
        ui = new Ui();
        storage = new Storage(filePathString);

        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (PenguinBotException e) {
            ui.showPenguinBotExceptionMessage(e);
            tasks = new TaskList();
        } catch (FileNotFoundException e) {
            ui.showPenguinBotExceptionMessage(new PenguinBotException("File Not Found!"));
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            try {
                if (userInput.equals("bye")) {
                    // Write the list into the file
                    tasks.storeTasksToStorage(storage);
                    break;
//                    try (FileWriter fw = new FileWriter(filePathString, false)) {
//                        for (Task task : list) {
//                            fw.write(task.toString());
//                            fw.write(System.lineSeparator());
//                        }
//                    } catch (IOException e) {
//                        throw new PenguinBotException("Failed to save tasks: " + e.getMessage());
//                    }
//
//                    System.out.println("""
//                            ____________________________________________________________
//                             Bye. Hope to see you again soon!
//                            ____________________________________________________________\
//                        """);
//                    break;

                } else if (userInput.equals("list")) {
                    tasks.printTasks();
//                    StringBuilder output = new StringBuilder();
//                    int i = 1;
//                    while (i <= list.size() && list.get(i - 1) != null) {
//                        output.append("     ")
//                                .append(i)
//                                .append(". ")
//                                .append(list.get(i - 1).toString())
//                                .append("\n");
//                        i++;
//                    }
//                    System.out.println("    ____________________________________________________________\n" +
//                             output + "\n" +
//                        "    ____________________________________________________________\n");

                } else {
                    String[] parts = userInput.split("\\s+");
                    String action = parts[0];

                    final String parameters = userInput.substring(action.length()).trim();
                    try {
                        actions parsedAction = actions.valueOf(action.toUpperCase());
                        switch (parsedAction) {
                            case MARK -> {
                                int number = Integer.parseInt(parts[1]);
                                tasks.markTask(number);
//
//                                list.get(number - 1).markAsDone();
//                                System.out.println("    ____________________________________________________________\n" +
//                                        "     Nice! I've marked this task as done:\n" +
//                                        "       " + list.get(number - 1).toString() + "\n" +
//                                        "    ____________________________________________________________");
                            }
                            case UNMARK -> {
                                int number = Integer.parseInt(parts[1]);
                                tasks.unmarkTask(number);

//                                list.get(number - 1).markAsUndone();
//                                System.out.println("    ____________________________________________________________\n" +
//                                        "     OK, I've marked this task as not done yet:\n" +
//                                        "       " + list.get(number - 1).toString() + "\n" +
//                                        "    ____________________________________________________________");
                            }
                            case DELETE -> {
                                int number = Integer.parseInt(parts[1]);
                                tasks.deleteTask(number);
//                                if (list.get(number) != null) {
//                                    Task task = list.get(number);
//                                    list.remove(number);
//                                    System.out.println("    ____________________________________________________________\n" +
//                                            "     Noted. I've removed this task:\n" +
//                                            "       " + task.toString() + "\n" +
//                                            "     Now you have " + list.size() + " tasks in the list.\n" +
//                                            "    ____________________________________________________________");
//                                }
                            }
                            case TODO -> {
                                if (parameters.isBlank()) {
                                    throw new PenguinBotException("Todo needs a description.");
                                }
                                ToDo toDo = new ToDo(parameters);
                                tasks.addTask(toDo);
//                                System.out.println("    ____________________________________________________________\n" +
//                                        "     Got it. I've added this task:\n" +
//                                        "       " + toDo + "\n" +
//                                        "     Now you have " + list.size() + " tasks in the list.\n" +
//                                        "    ____________________________________________________________\n");
                            }
                            case DEADLINE -> {
                                String by = "";
                                String[] bySplit = parameters.split("/by", 2);
                                String description = bySplit[0].trim();
                                if (bySplit.length > 1) {
                                    by = bySplit[1].trim();
                                }
                                if (description.isEmpty() || by.isEmpty()) {
                                    throw new PenguinBotException("Deadline needs description and ISO date-time (e.g. 2024-02-01T13:30).");
                                }
                                LocalDateTime byDateTime = parseUserDateTime(by, "deadline");
                                Deadline deadlineTask = new Deadline(description, byDateTime);
                                tasks.addTask(deadlineTask);
//                                System.out.println("    ____________________________________________________________\n" +
//                                        "     Got it. I've added this task:\n" +
//                                        "       " + deadlineTask + "\n" +
//                                        "     Now you have " + list.size() + " tasks in the list.\n" +
//                                        "    ____________________________________________________________\n");
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
                                if (description.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                                    throw new PenguinBotException("Event needs description, start, and end in ISO date-time (e.g. 2024-02-01T13:30).");
                                }
                                LocalDateTime start = parseUserDateTime(startTime, "event start");
                                LocalDateTime end = parseUserDateTime(endTime, "event end");
                                Event eventTask = new Event(description, start, end);
                                tasks.addTask(eventTask);
//                                System.out.println("    ____________________________________________________________\n" +
//                                        "     Got it. I've added this task:\n" +
//                                        "       " + eventTask + "\n" +
//                                        "     Now you have " + list.size() + " tasks in the list.\n" +
//                                        "    ____________________________________________________________\n");
                            }
//                            case BYE, LIST -> {
//
//                            }
                        }
                    } catch (IllegalArgumentException e) {
                        throw new PenguinBotException("Unknown command: " + action);
                    }
                }
            } catch (PenguinBotException e) {
                ui.showPenguinBotExceptionMessage(e);
//                System.out.println("    ____________________________________________________________\n" +
//                        "     " + e.getMessage() + "\n" +
//                        "    ____________________________________________________________");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new PenguinBot("./src/data/TaskList.txt").run();
//        String initialPrompt = """
//                    ____________________________________________________________
//                     Hello! I'm PenguinBot
//                     What can I do for you?
//                    ____________________________________________________________
//                """;
//
//        System.out.println(initialPrompt);
//
//        ArrayList<Task> list = new ArrayList<>();
//
//        // Load the file
//        String filePathString = "./src/data/TaskList.txt";
//        Path filePath = Paths.get(filePathString);
//        Path parentDir = filePath.getParent();
//
//        // Handle the file not found problems
//        try {
//            if (parentDir != null) {
//                Files.createDirectories(parentDir);
//            }
//
//            if (Files.notExists(filePath)) {
//                Files.createFile(filePath);
//            }
//        } catch (IOException e) {
//            System.err.println("Failed to create directory/file: " + e.getMessage());
//        }
//
//        File textListFile = new File(filePathString);
//        try (Scanner fileScanner = new Scanner(textListFile)) {
//            // Load the string into task objects
//            while (fileScanner.hasNext()) {
//                String taskString = fileScanner.nextLine();
//                String[] parts = taskString.split(" \\| ");
//                if (parts.length == 0 || parts[0].isBlank()) {
//                    continue;
//                }
//                try {
//                    boolean isDone = parseDone(parts);
//                    String type = parts[0].trim();
//                    switch (type) {
//                        case "T" -> {
//                            String description = extractDescription(parts);
//                            Task todoTask = new ToDo(description);
//                            if (isDone) {
//                                todoTask.markAsDone();
//                            }
//                            list.add(todoTask);
//                        }
//                        case "D" -> {
//                            String description = extractDescription(parts);
//                            String byRaw = extractDateSegment(parts, "by:");
//                            LocalDateTime by = parseStoredDateTime(byRaw);
//                            Task deadlineTask = new Deadline(description, by);
//                            if (isDone) {
//                                deadlineTask.markAsDone();
//                            }
//                            list.add(deadlineTask);
//                        }
//                        case "E" -> {
//                            String description = extractDescription(parts);
//                            String timeSegment = extractDateSegment(parts, "from:");
//                            Pattern pattern = Pattern.compile("from: (.*?) to: (.*)");
//                            Matcher matcher = pattern.matcher(timeSegment);
//                            if (!matcher.find()) {
//                                throw new PenguinBotException("Corrupted event line");
//                            }
//                            LocalDateTime start = parseStoredDateTime(matcher.group(1));
//                            LocalDateTime end = parseStoredDateTime(matcher.group(2));
//                            Task eventTask = new Event(description, start, end);
//                            if (isDone) {
//                                eventTask.markAsDone();
//                            }
//                            list.add(eventTask);
//                        }
//                        default -> System.out.println("Not a valid task");
//                    }
//                } catch (PenguinBotException e) {
//                    System.out.println("    ____________________________________________________________\n"
//                            + "     Skipping bad entry: " + e.getMessage() + "\n"
//                            + "    ____________________________________________________________");
//                }
//            }
//        }


    }

    private static LocalDateTime parseUserDateTime(String raw, String label) throws PenguinBotException {
        if (raw == null || raw.isBlank()) {
            throw new PenguinBotException("Missing " + label + " date-time (use ISO e.g. 2024-02-01T13:30).");
        }
        try {
            return LocalDateTime.parse(raw.trim(), STORAGE_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new PenguinBotException("Invalid " + label + " date-time (use ISO e.g. 2024-02-01T13:30).");
        }
    }
}