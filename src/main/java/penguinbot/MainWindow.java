package penguinbot;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends BorderPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private PenguinBot penguinBot;

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

    // MainWindow.java, MainWindow.fxml is refactored by GitHub Copilot
    // to enable resizable GUI feature for better GUI
    // theme.css is also generated to make the theme for the GUI
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        scrollPane.setFitToWidth(true);
        dialogContainer.setFillWidth(true);
        // Apply shared styling to the root once controls are available.
        this.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/view/theme.css")).toExternalForm()
        );
    }

    public void setPenguinBot(PenguinBot p) {
        penguinBot = p;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        String penguinBotText = penguinBot.getResponse(userInput.getText());

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getPenguinBotDialog(penguinBotText, penguinBotImage)
        );

        userInput.clear();
    }
}
