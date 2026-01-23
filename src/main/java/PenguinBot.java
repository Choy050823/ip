import java.util.ArrayList;
import java.util.Scanner;

public class PenguinBot {
    public static void main(String[] args) {
        String intialPrompt = "    ____________________________________________________________\n" +
                "     Hello! I'm PenguinBot\n" +
                "     What can I do for you?\n" +
                "    ____________________________________________________________\n";

        System.out.println(intialPrompt);

        String[] list = new String[100];
        int count = 0;

        while (true) {
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                System.out.println("""
                            ____________________________________________________________
                             Bye. Hope to see you again soon!
                            ____________________________________________________________\
                        """);
                break;
            } else if (userInput.equals("list")){
                StringBuilder output = new StringBuilder();
                int i = 1;
                while (i <= list.length && list[i - 1] != null) {
                    output.append(i).append(". ").append(list[i - 1]).append("\n");
                    i++;
                }
                System.out.println("    ____________________________________________________________\n" +
                             output + "\n" +
                        "    ____________________________________________________________\n");

            } else {
                list[count] = userInput;
                count++;
                System.out.println("    ____________________________________________________________\n" +
                        "Added: " + userInput + "\n" +
                        "    ____________________________________________________________\n");
            }
        }
    }
}
