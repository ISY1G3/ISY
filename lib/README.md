## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

^ Default message van VS Code.

## Links dat handig zijn

Om JavaFX te runnen moet je de volgende tutorial volgen: https://openjfx.io/openjfx-docs/#introduction

In vscode: ga naar explorer > 3 dots > Java Projects, helemaal onderin moet je bij referenced libraries alles dat in lib staat (van javaFX folder dat je hebt gedownloaded) toevoegen.

De guy in de tutorial gebruikt ook encapsulation met get, dit wordt gebruikt bij gevoelige informatie om ze te verbergen van gebruikers.
Zie: https://www.w3schools.com/java/java_encapsulation.asp

## Instructies om dit project te starten (gemaakt door ChatGPT)

1. Clone of download de repository en open de map `ISY` in je IDE. Open **niet** de bovenliggende `TicTacToe`-map.
2. Installeer een Java JDK als die nog niet op je computer staat.
3. Download de **JavaFX SDK 27** voor jouw besturingssysteem via https://openjfx.io/ en pak deze uit. Een voorbeeldpad op Windows is:

   ```text
   C:\Java\javafx-sdk-27
   ```

4. Gebruik je VS Code? Installeer dan de extensie **Extension Pack for Java**.
5. Open `.vscode/settings.json`. Vervang alle paden die beginnen met `c:\Java\javafx-sdk-27` door het pad naar jouw eigen uitgepakte JavaFX SDK.
6. Open `.vscode/launch.json`. Vervang het pad na `--module-path` door het pad naar jouw eigen JavaFX SDK-map `lib`, bijvoorbeeld:

   ```text
   --module-path "C:/Java/javafx-sdk-27/lib" --add-modules javafx.controls,javafx.fxml
   ```

7. Open **Run and Debug** in VS Code en start de configuratie **Main**. Het Tic-Tac-Toe-venster opent nu.

### Andere IDE's

1. Importeer de map `ISY` als Java-project en stel `src` in als source folder.
2. Voeg alle `.jar`-bestanden uit `javafx-sdk-27/lib` toe aan de dependencies/build path van het project.
3. Voeg deze VM options toe aan de run configuration. Vervang het voorbeeldpad door jouw eigen JavaFX-pad:

   ```text
   --module-path "C:\Java\javafx-sdk-27\lib" --add-modules javafx.controls,javafx.fxml
   ```

4. Start de class `Main`.
