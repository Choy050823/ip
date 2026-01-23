import java.util.Scanner;

public class PenguinBot {
    public static void main(String[] args) {
        String intialPrompt = "    ____________________________________________________________\n" +
                "     Hello! I'm [YOUR CHATBOT NAME]\n" +
                "     What can I do for you?\n" +
                "    ____________________________________________________________\n";

        System.out.println(intialPrompt);

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.next();
            if (userInput.equals("bye")) {
                System.out.println("""
                            ____________________________________________________________
                             Bye. Hope to see you again soon!
                            ____________________________________________________________\
                        """);
                break;
            } else {
                System.out.println("    ____________________________________________________________\n" +
                             userInput + "\n" +
                        "    ____________________________________________________________\n");
            }
        }

    }
}
