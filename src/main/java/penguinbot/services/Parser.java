package penguinbot.services;

import penguinbot.exceptions.PenguinBotException;

import penguinbot.models.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Interprets user commands and dispatches task operations.
 */
public class Parser {

    /** Task list being manipulated. */
    private final TaskList tasks;

    /** Storage handler for persistence on exit. */
    private final Storage storage;

    /**
     * Creates a parser with dependencies injected.
     *
     * @param tasks   task list to modify.
     * @param storage storage for persistence.
     */
    public Parser(TaskList tasks, Storage storage) {
        this.tasks = tasks;
        this.storage = storage;
    }

    /**
     * Parses a user command into a command
     *
     * @param userInput raw input string.
     */
    public Command parseCommand(String userInput) throws PenguinBotException {
        try {
            String[] parts = userInput.split("\\s+");
            String action = parts[0];
            String parameters = userInput.substring(action.length()).trim();
            Actions parsedAction = Actions.valueOf(action.toUpperCase());

            switch (parsedAction) {
            case BYE -> {
                return new ExitCommand(Actions.BYE, tasks, storage);
            }

            case LIST -> {
                return new ListCommand(Actions.LIST, tasks);
            }

            case MARK -> {
                String taskIndexString = parts[1];
                return new MarkCommand(Actions.MARK, tasks, taskIndexString);
            }

            case UNMARK -> {
                String taskIndexString = parts[1];
                return new UnmarkCommand(Actions.MARK, tasks, taskIndexString);
            }

            case DELETE -> {
                String taskIndexString = parts[1];
                return new DeleteCommand(Actions.DELETE, tasks, taskIndexString);
            }

            case FIND -> {
                if (parameters.isBlank()) {
                    throw new PenguinBotException("Find task needs a keyword.");
                }

                return new FindCommand(Actions.FIND, tasks, parameters);
            }

            case TODO -> {
                if (parameters.isBlank()) {
                    throw new PenguinBotException("Todo needs a description.");
                }

                return new ToDoCommand(Actions.TODO, tasks, parameters);
            }

            case DEADLINE -> {
                String deadlineRawString = "";
                String[] deadlineRawStringSplit = parameters.split("/by", 2);
                String taskDescription = deadlineRawStringSplit[0].trim();

                if (deadlineRawStringSplit.length > 1) {
                    deadlineRawString = deadlineRawStringSplit[1].trim();
                }

                if (taskDescription.isEmpty() || deadlineRawString.isEmpty()) {
                    throw new PenguinBotException(
                            "Deadline needs taskDescription and"
                                    + " ISO date-time (e.g. 2024-02-01T13:30)."
                    );
                }

                return new DeadlineCommand(
                        Actions.DEADLINE,
                        tasks,
                        taskDescription,
                        deadlineRawString
                );
            }
            case EVENT -> {
                String startDateTimeRawString = "";
                String endDateTimeRawString = "";
                String[] fromSplit = parameters.split("/from", 2);
                String taskDescription = fromSplit[0].trim();

                if (fromSplit.length > 1) {
                    String[] toSplit = fromSplit[1].split("/to", 2);
                    startDateTimeRawString = toSplit[0].trim();

                    if (toSplit.length > 1) {
                        endDateTimeRawString = toSplit[1].trim();
                    }
                }

                if (taskDescription.isEmpty()
                        || startDateTimeRawString.isEmpty()
                        || endDateTimeRawString.isEmpty()) {

                    throw new PenguinBotException(
                            "Event needs taskDescription, start, and end "
                                    + "in ISO date-time (e.g. 2024-02-01T13:30)."
                    );
                }

                return new EventCommand(
                        Actions.EVENT,
                        tasks,
                        taskDescription,
                        startDateTimeRawString,
                        endDateTimeRawString
                );
            }

            default -> throw new PenguinBotException("Unknown command: " + action);
            }

        } catch (IllegalArgumentException e) {
            throw new PenguinBotException("Unknown Command");

        } catch (RuntimeException e) {
            throw new PenguinBotException(e.getMessage());
        }
    }

    /**
     * Parses user-provided date-time strings using ISO format.
     *
     * @param raw   raw date-time text.
     * @param label context label for error messaging.
     * @return parsed {@link LocalDateTime}.
     * @throws PenguinBotException when parsing fails or input is blank.
     */
    public static LocalDateTime parseUserDateTime(
            String raw, String label) throws PenguinBotException {
        if (raw == null || raw.isBlank()) {
            throw new PenguinBotException("Missing " + label + " date-time (use ISO e.g. 2024-02-01T13:30).");
        }
        try {
            return LocalDateTime.parse(raw.trim(), Storage.STORAGE_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new PenguinBotException("Invalid " + label + " date-time (use ISO e.g. 2024-02-01T13:30).");
        }
    }
}
