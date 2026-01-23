import java.util.ArrayList;
import java.util.Scanner;

public class PenguinBot {
    public static void main(String[] args) {
        String initialPrompt = "    ____________________________________________________________\n" +
                "     Hello! I'm PenguinBot\n" +
                "     What can I do for you?\n" +
                "    ____________________________________________________________\n";

        System.out.println(initialPrompt);

        Task[] list = new Task[100];
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
                    output.append(i)
                            .append(". [")
                            .append(list[i - 1].getStatusIcon())
                            .append("] ")
                            .append(list[i - 1].description)
                            .append("\n");
                    i++;
                }
                System.out.println("    ____________________________________________________________\n" +
                             output + "\n" +
                        "    ____________________________________________________________\n");

            } else {
                String[] parts = userInput.split("\\s+", 2);
                String action = parts[0];

                if (action.equals("mark")) {
                    int number = Integer.parseInt(parts[1]);

                    list[number - 1].markAsDone();
                    System.out.println("    ____________________________________________________________\n" +
                            "     Nice! I've marked this task as done:\n" +
                            "       [" + list[number - 1].getStatusIcon() + "] " + list[number - 1].description + "\n" +
                            "    ____________________________________________________________");
                } else if (action.equals("unmark")){
                    int number = Integer.parseInt(parts[1]);

                    list[number - 1].markAsUndone();
                    System.out.println("    ____________________________________________________________\n" +
                            "     OK, I've marked this task as not done yet:\n" +
                            "       [" + list[number - 1].getStatusIcon() + "] " + list[number - 1].description + "\n" +
                            "    ____________________________________________________________");
                } else {
                    list[count] = new Task(userInput);
                    count++;
                    System.out.println("    ____________________________________________________________\n" +
                            "Added: " + userInput + "\n" +
                            "    ____________________________________________________________\n");
                }
            }
        }
    }
}
