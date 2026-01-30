package penguinbot.services;

import penguinbot.exceptions.PenguinBotException;
import penguinbot.models.Deadline;
import penguinbot.models.Event;
import penguinbot.models.Task;
import penguinbot.models.ToDo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles persistence of tasks to and from the backing text file.
 */
public class Storage {
    /** Formatter used to serialize and deserialize {@link java.time.LocalDateTime}. */
    public static final DateTimeFormatter STORAGE_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /** Absolute or relative path to the task storage file. */
    private final String filePathString;

    /**
     * Creates a storage instance bound to the given file path.
     *
     * @param filePathString path to the storage file.
     */
    public Storage(String filePathString) {
        this.filePathString = filePathString;
    }

    /**
     * Loads all tasks from the storage file, creating the file and its parent
     * directories when missing, and skipping corrupted lines.
     *
     * @return list of tasks loaded from disk.
     * @throws PenguinBotException   when a recoverable parsing issue occurs.
     * @throws FileNotFoundException when the file cannot be opened.
     */
    public List<Task> loadTasks() throws PenguinBotException, FileNotFoundException {
        ArrayList<Task> tasks = new ArrayList<>();

        // Load the file
        Path filePath = Paths.get(filePathString);
        Path parentDir = filePath.getParent();

        // Handle the file not found problems
        try {
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory/file: " + e.getMessage());
        }

        File textListFile = new File(filePathString);
        try (Scanner fileScanner = new Scanner(textListFile)) {
            // Load the string into task objects
            while (fileScanner.hasNext()) {
                String taskString = fileScanner.nextLine();
                String[] parts = taskString.split(" \\| ");
                if (parts.length == 0 || parts[0].isBlank()) {
                    continue;
                }
                try {
                    boolean isDone = parseDone(parts);
                    String type = parts[0].trim();
                    switch (type) {
                        case "T" -> {
                            String description = extractDescription(parts);
                            Task todoTask = new ToDo(description);
                            if (isDone) {
                                todoTask.markAsDone();
                            }
                            tasks.add(todoTask);
                        }
                        case "D" -> {
                            String description = extractDescription(parts);
                            String byRaw = extractDateSegment(parts, "by:");
                            LocalDateTime by = parseStoredDateTime(byRaw);
                            Task deadlineTask = new Deadline(description, by);
                            if (isDone) {
                                deadlineTask.markAsDone();
                            }
                            tasks.add(deadlineTask);
                        }
                        case "E" -> {
                            String description = extractDescription(parts);
                            String timeSegment = parts.length >= 4 ? parts[3] : "";
                            if (!timeSegment.toLowerCase().startsWith("from:")) {
                                throw new PenguinBotException("Missing from: segment");
                            }
                            Pattern pattern = Pattern.compile("from:\\s*(.*?)\\s*to:\\s*(.*)", Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(timeSegment.trim());
                            if (!matcher.matches()) {
                                throw new PenguinBotException("Corrupted event line");
                            }
                            LocalDateTime start = parseStoredDateTime(matcher.group(1));
                            LocalDateTime end = parseStoredDateTime(matcher.group(2));
                            Task eventTask = new Event(description, start, end);
                            if (isDone) {
                                eventTask.markAsDone();
                            }
                            tasks.add(eventTask);
                        }
                        default -> System.out.println("Not a valid task");
                    }
                } catch (PenguinBotException e) {
                    System.out.println("    ____________________________________________________________\n"
                            + "     Skipping bad entry: " + e.getMessage() + "\n"
                            + "    ____________________________________________________________");
                }
            }
        }

        return tasks;
    }

    /**
     * Persists all tasks to the storage file, replacing any existing content.
     *
     * @param list tasks to persist.
     * @throws PenguinBotException when writing fails.
     */
    public void storeTasks(List<Task> list) throws PenguinBotException {
        // Write the list into the file
        try (FileWriter fw = new FileWriter(filePathString, false)) {
            for (Task task : list) {
                fw.write(task.toString());
                fw.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new PenguinBotException("Failed to save tasks: " + e.getMessage());
        }
    }

    /**
     * Parses the completion flag from a serialized task line.
     *
     * @param parts tokenized task line.
     * @return true if the task is marked done.
     */
    private static boolean parseDone(String[] parts) {
        if (parts.length < 2) {
            return false;
        }
        String token = parts[1].trim();
        if ("1".equals(token)) {
            return true;
        }
        if ("0".equals(token)) {
            return false;
        }
        return token.startsWith("[X]");
    }

    /**
     * Extracts the task description from serialized parts.
     *
     * @param parts tokenized task line.
     * @return description string, possibly empty.
     */
    private static String extractDescription(String[] parts) {
        if (parts.length >= 3) {
            return parts[2];
        }
        if (parts.length == 2) {
            return parts[1].replaceFirst("^\\[[X ]]\\s*", "");
        }
        return "";
    }

    /**
     * Extracts a date segment prefixed by the given label.
     *
     * @param parts   tokenized task line.
     * @param prefix  expected prefix (e.g., "by:" or "from:").
     * @return the trimmed date-time segment.
     * @throws PenguinBotException when the prefix is missing.
     */
    private static String extractDateSegment(String[] parts, String prefix) throws PenguinBotException {
        String candidate = parts.length >= 4 ? parts[3] : (parts.length == 3 ? parts[2] : "");
        if (!candidate.toLowerCase().startsWith(prefix)) {
            throw new PenguinBotException("Missing " + prefix + " segment");
        }
        return candidate.substring(prefix.length()).trim().replaceFirst("^:", "").trim();
    }

    /**
     * Parses a stored date-time string into {@link LocalDateTime}.
     *
     * @param raw serialized date-time.
     * @return parsed {@link LocalDateTime}.
     * @throws PenguinBotException when the value cannot be parsed.
     */
    private static LocalDateTime parseStoredDateTime(String raw) throws PenguinBotException {
        try {
            return LocalDateTime.parse(raw.trim(), STORAGE_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new PenguinBotException("Invalid stored date-time: " + raw);
        }
    }
}