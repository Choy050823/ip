package penguinbot.services;

import penguinbot.exceptions.PenguinBotException;

import java.util.Scanner;

/**
 * Provides console-based input and output utilities for PenguinBot.
 */
public class Ui {

    /**
     * Constructs a UI helper.
     */
    public Ui() {

    }

    /**
     * Displays the welcome message.
     */
    public void showWelcome() {
        String initialPrompt = """
                    ____________________________________________________________
                     Hello! I'm PenguinBot
                     What can I do for you?
                    ____________________________________________________________
                """;

        System.out.println(initialPrompt);
    }

    /**
     * Prints a formatted exception message.
     *
     * @param e exception to display.
     */
    public void showPenguinBotExceptionMessage(PenguinBotException e) {
        System.out.println(e.getMessage());
    }

    /**
     * Prints a divider line.
     */
    public void showLine() {
        System.out.println("    ____________________________________________________________\n");
    }

    /**
     * Reads a single line command from standard input.
     *
     * @return user input, or an empty string if none.
     */
    public String readCommand() {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        } else {
            return "";
        }
    }
}
