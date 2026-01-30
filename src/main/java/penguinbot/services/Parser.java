package penguinbot.services;

import penguinbot.models.actions;
import penguinbot.exceptions.PenguinBotException;
import penguinbot.models.Deadline;
import penguinbot.models.Event;
import penguinbot.models.ToDo;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Parser {
    private TaskList tasks;
    private Storage storage;
    private Ui ui;
    private boolean isExit;

    public Parser(TaskList tasks, Storage storage, Ui ui) {
        this.tasks = tasks;
        this.storage = storage;
        this.ui = ui;
        this.isExit = false;
    }

    public boolean isExit() {
        return this.isExit;
    }

    public void parseAndExecute(String userInput) {
        try {
            if (userInput.equals("bye")) {
                // Write the list into the file
                tasks.storeTasksToStorage(storage);
                this.isExit = true;

            } else if (userInput.equals("list")) {
                tasks.printTasks();
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
                        }
                        case UNMARK -> {
                            int number = Integer.parseInt(parts[1]);
                            tasks.unmarkTask(number);
                        }
                        case DELETE -> {
                            int number = Integer.parseInt(parts[1]);
                            tasks.deleteTask(number);
                        }
                        case TODO -> {
                            if (parameters.isBlank()) {
                                throw new PenguinBotException("Todo needs a description.");
                            }
                            ToDo toDo = new ToDo(parameters);
                            tasks.addTask(toDo);
                        }
                        case DEADLINE -> {
                            String by = "";
                            String[] bySplit = parameters.split("/by", 2);
                            String description = bySplit[0].trim();
                            if (bySplit.length > 1) {
                                by = bySplit[1].trim();
                            }
                            if (description.isEmpty() || by.isEmpty()) {
                                throw new PenguinBotException("penguinbot.task.Deadline needs description and ISO date-time (e.g. 2024-02-01T13:30).");
                            }
                            LocalDateTime byDateTime = parseUserDateTime(by, "deadline");
                            Deadline deadlineTask = new Deadline(description, byDateTime);
                            tasks.addTask(deadlineTask);
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
                                throw new PenguinBotException("penguinbot.task.Event needs description, start, and end in ISO date-time (e.g. 2024-02-01T13:30).");
                            }
                            LocalDateTime start = parseUserDateTime(startTime, "event start");
                            LocalDateTime end = parseUserDateTime(endTime, "event end");
                            Event eventTask = new Event(description, start, end);
                            tasks.addTask(eventTask);
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

    private static LocalDateTime parseUserDateTime(String raw, String label) throws PenguinBotException {
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
