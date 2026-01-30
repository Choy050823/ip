package penguinbot;

import penguinbot.exceptions.PenguinBotException;

import penguinbot.services.Parser;
import penguinbot.services.Storage;
import penguinbot.services.TaskList;
import penguinbot.services.Ui;

import java.io.FileNotFoundException;

/**
 * Entry point for the PenguinBot application, wiring storage, UI, and parsing.
 */
public class PenguinBot {

    /** Handles persistence of tasks. */
    private final Storage storage;

    /**
     * Current in-memory task list.
     */
    private TaskList tasks;

    /** User interface handler for input/output. */
    private final Ui ui;

    /**
     * Constructs the bot and initializes storage and task list from disk.
     *
     * @param filePathString path to the task storage file.
     */
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

    /**
     * Starts the interactive command loop.
     */
    public void run() {
        ui.showWelcome();

        Parser parser = new Parser(tasks, storage, ui);

        while (!parser.isExit()) {
            String userInput = ui.readCommand();
            parser.parseAndExecute(userInput);
        }
    }

    /**
     * Program entry point.
     *
     * @param args CLI arguments.
     */
    public static void main(String[] args) {
        new PenguinBot("./src/data/TaskList.txt").run();
    }
}