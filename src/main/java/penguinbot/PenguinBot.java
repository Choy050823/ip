package penguinbot;

import penguinbot.exceptions.PenguinBotException;

import penguinbot.models.Command;
import penguinbot.services.Parser;
import penguinbot.services.Storage;
import penguinbot.services.TaskList;

import java.io.FileNotFoundException;

/**
 * Entry point for the PenguinBot application, wiring storage, UI, and parsing.
 */
public class PenguinBot {

    private final Parser parser;

    /**
     * Constructs the bot and initializes storage and task list from disk.
     *
     * @param filePathString path to the task storage file.
     */
    public PenguinBot(String filePathString) {
        Storage storage = new Storage(filePathString);

        TaskList tasks;

        try {
            tasks = new TaskList(storage.loadTasks());

        } catch (PenguinBotException e) {
            System.out.println(e.getMessage());
            tasks = new TaskList();

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!");
            tasks = new TaskList();
        }

        this.parser = new Parser(tasks, storage);
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            Command command = parser.parseCommand(input);

            return command.execute();
        } catch (PenguinBotException e) {
            return "Error: " + e.getMessage();
        }
    }
}