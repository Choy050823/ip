package penguinbot;

import penguinbot.exceptions.PenguinBotException;
import penguinbot.services.Parser;
import penguinbot.services.Storage;
import penguinbot.services.TaskList;
import penguinbot.services.Ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

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

        Parser parser = new Parser(tasks, storage, ui);

        while (!parser.isExit()) {
            String userInput = ui.readCommand();
            parser.parseAndExecute(userInput);
        }
    }

    public static void main(String[] args) throws IOException {
        new PenguinBot("./src/data/TaskList.txt").run();
    }
}