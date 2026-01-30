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
}
