package penguinbot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;

    private final Image userImage = new Image(
            Objects.requireNonNull(
                    this.getClass().getResourceAsStream("/images/DaUser.png")
            )
    );

    private final Image penguinBotImage = new Image(
            Objects.requireNonNull(
                    this.getClass().getResourceAsStream("/images/DaPenguinBot.png")
            )
    );

    private final PenguinBot penguinBot = new PenguinBot("./src/data/TaskList.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setPenguinBot(penguinBot);  // inject the Duke instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a dialog box containing user input, and appends it to
     * the dialog container. Clears the user input after processing.
     */
//    private void handleUserInput() {
//        String userText = userInput.getText();
//        String penguinBotText = penguinBot.getResponse(userInput.getText());
//        dialogContainer.getChildren().addAll(
//                DialogBox.getUserDialog(userText, userImage),
//                DialogBox.getPenguinBotDialog(penguinBotText, penguinBotImage)
//        );
//        userInput.clear();
//    }
}
