# PenguinBot (JavaFX)

A desktop chatbot built with Java 17 and JavaFX. Includes GUI-style task management (todo, deadline, event).

## Prerequisites
- Java 17 (JDK). Confirm with `java -version`.
- Git (optional, for cloning).
- Internet access for the first Gradle wrapper run (downloads dependencies).

## Project layout
- `src/main/java/penguinbot/` — app source (GUI: `Main`, `MainWindow`, `DialogBox`; logic: commands, parser, storage).
- `src/main/resources/view/` — FXML layouts and styles (`MainWindow.fxml`, `DialogBox.fxml`, `theme.css`).
- `src/data/TaskList.txt` — default save file (created/updated at runtime).
- `text-ui-test/` — simple text-mode regression harness.

## Running the app (GUI)
Use the Gradle wrapper; no manual JavaFX setup required.

```powershell
# From the project root
./gradlew run
```

Gradle will compile, download JavaFX, and launch the PenguinBot window. If JavaFX fails to load, re-check that you are on JDK 17 and rerun the command.

## Running tests
```powershell
./gradlew test
```

## Packaging a runnable jar
Creates `build/libs/penguinBot-v0.2.jar` with bundled JavaFX dependencies.
```powershell
./gradlew shadowJar
java -jar build/libs/penguinBot-v0.2.jar
```

## Using the bot (commands)
Type these in the GUI input box:
- `list`
- `todo <description>`
- `deadline <description> /by <yyyy-mm-ddThh:mm>`
- `event <description> /from <yyyy-mm-ddThh:mm> /to <yyyy-mm-ddThh:mm>`
- `mark <index>` / `unmark <index>`
- `delete <index>`
- `find <keyword>`
- `bye` to exit

## Storage
Tasks persist in `src/data/TaskList.txt`. If the file contains malformed lines, the app will skip them and continue. Delete the file to start fresh.

## Troubleshooting
- **ClassCastException / FXML load errors:** ensure `MainWindow.fxml` is unchanged and the app is launched via `./gradlew run`.
- **Java version issues:** make sure `JAVA_HOME` points to JDK 17. Reopen terminal after changing it.
- **Fresh dependencies:** run `./gradlew clean` followed by `./gradlew run` if you see stale-class errors.
