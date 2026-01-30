import java.util.Scanner;

public class Ui {

    public Ui() {

    }

    public void showWelcome() {
        String initialPrompt = """
                    ____________________________________________________________
                     Hello! I'm PenguinBot
                     What can I do for you?
                    ____________________________________________________________
                """;

        System.out.println(initialPrompt);
    }

    public void showPenguinBotExceptionMessage(PenguinBotException e) {
        System.out.println(e.getMessage());
    }

    public void showLine() {
        System.out.println("    ____________________________________________________________\n");
    }

    public String readCommand() {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        } else {
            return "";
        }
    }
}
