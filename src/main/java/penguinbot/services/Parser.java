package penguinbot.services;

import penguinbot.exceptions.PenguinBotException;

import penguinbot.models.Actions;
import penguinbot.models.Deadline;
import penguinbot.models.Event;
import penguinbot.models.ToDo;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Interprets user commands and dispatches task operations.
 */
public class Parser {
    /** Task list being manipulated. */
    private TaskList taskList;

    /** Storage handler for persistence on exit. */
    private Storage storage;

    /** UI for user interaction. */
    private Ui ui;
    /**  Flag indicating whether the session should terminate. */
    private boolean isExit;

    /**
     * Creates a parser with dependencies injected.
     *
     * @param taskList   task list to modify.
     * @param storage storage for persistence.
     * @param ui      user interface helper.
     */
    public Parser(TaskList taskList, Storage storage, Ui ui) {
        this.taskList = taskList;
        this.storage = storage;
        this.ui = ui;
        this.isExit = false;
    }

    /**
     * Returns true if the user requested exit.
     *
     * @return exit flag.
     */
    public boolean isExit() {
        return this.isExit;
    }

    /**
     * Parses a user command and executes the corresponding action,
     * printing any user-facing errors.
     *
     * @param userInput raw input string.
     */
    public void parseAndExecute(String userInput) {
        try {
            if (userInput.equals("bye")) {
                // Write the list into the file
                taskList.storeTasksToStorage(storage);
                this.isExit = true;

            } else if (userInput.equals("list")) {
                taskList.printTasks();
            } else {
                String[] parts = userInput.split("\\s+");
                String action = parts[0];

                final String parameters = userInput.substring(action.length()).trim();
                try {
                    Actions parsedAction = Actions.valueOf(action.toUpperCase());
                    switch (parsedAction) {
                        case MARK -> {
                            int number = Integer.parseInt(parts[1]);
                            taskList.markTask(number);
                        }
                        case UNMARK -> {
                            int number = Integer.parseInt(parts[1]);
                            taskList.unmarkTask(number);
                        }
                        case DELETE -> {
                            int number = Integer.parseInt(parts[1]);
                            taskList.deleteTask(number);
                        }
                        case FIND -> {
                            if (parameters.isBlank()) {
                                throw new PenguinBotException("Find task needs a keyword.");
                            }
                            taskList.findTasks(parameters);
                        }
                        case TODO -> {
                            if (parameters.isBlank()) {
                                throw new PenguinBotException("Todo needs a description.");
                            }
                            ToDo toDo = new ToDo(parameters);
                            taskList.addTask(toDo);
                        }
                        case DEADLINE -> {
                            String by = "";
                            String[] bySplit = parameters.split("/by", 2);
                            String description = bySplit[0].trim();
                            if (bySplit.length > 1) {
                                by = bySplit[1].trim();
                            }
                            if (description.isEmpty() || by.isEmpty()) {
                                throw new PenguinBotException(
                                        "Deadline needs description and ISO date-time (e.g. 2024-02-01T13:30)."
                                );
                            }
                            LocalDateTime byDateTime = parseUserDateTime(by, "deadline");
                            Deadline deadlineTask = new Deadline(description, byDateTime);
                            taskList.addTask(deadlineTask);
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
                                throw new PenguinBotException(
                                        "Event needs description, start, and end "
                                                + "in ISO date-time (e.g. 2024-02-01T13:30)."
                                );
                            }
                            LocalDateTime start = parseUserDateTime(startTime, "event start");
                            LocalDateTime end = parseUserDateTime(endTime, "event end");
                            Event eventTask = new Event(description, start, end);
                            taskList.addTask(eventTask);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    throw new PenguinBotException("Unknown command: " + action);
                }
            }
        } catch (PenguinBotException e) {
            ui.showLine();
            ui.showPenguinBotExceptionMessage(e);
            ui.showLine();
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
    private static LocalDateTime parseUserDateTime(
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
