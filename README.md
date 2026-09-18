# Kaykay

Kaykay is a desktop mission-control companion for managing tasks and keeping a
personal record of places. It combines a graphical chat interface with concise
commands, making it quick to add, update, and find information.

![Kaykay's graphical user interface](docs/Ui.png)

## Features

- Track todos, deadlines, and events.
- Mark tasks as completed or not completed.
- Search task descriptions.
- Save places together with their type, location, visit date, rating, and notes.
- Search across all recorded place details.
- Save changes automatically between sessions.

For installation instructions and a complete command reference, see the
**[Kaykay User Guide](docs/README.md)**.

## Using Kaykay

Download `kaykay.jar` from the
[latest GitHub release](https://github.com/USER-LRK/ip/releases/latest), place it
in a folder of your choice, and run:

```text
java -jar kaykay.jar
```

Java 25 must be installed. End users do not need Gradle or the project source
code.

## Setting up the project in IntelliJ IDEA

### Prerequisites

- JDK 25
- A recent version of IntelliJ IDEA

### Setup

1. Open IntelliJ IDEA.
1. Select **Open**, choose this project directory, and accept the default import
   options.
1. Configure the project SDK as **JDK 25** and set the project language level to
   **SDK default**. See the
   [IntelliJ IDEA SDK documentation](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk)
   if you need help with this step.
1. Run the application with the command for your terminal:

   - Windows PowerShell: `.\gradlew.bat run`
   - Git Bash, macOS, or Linux: `./gradlew run`

   You can also use the corresponding Gradle run configuration in IntelliJ
   IDEA.

> [!IMPORTANT]
> Keep `src/main/java` as the source root. Gradle and the project's other tools
> expect Java source files at this location.

## Building and testing

Run the command for your terminal to build the executable JAR:

```text
# Windows PowerShell
.\gradlew.bat clean shadowJar

# Git Bash, macOS, or Linux
./gradlew clean shadowJar
```

The resulting file is `build/libs/kaykay.jar`.

Run the automated tests and code-quality checks with:

```text
# Windows PowerShell
.\gradlew.bat check

# Git Bash, macOS, or Linux
./gradlew check
```

## Project structure

- `src/main/java`: application source code
- `src/main/resources`: JavaFX layouts, styles, and images
- `src/test/java`: JUnit tests
- `docs/README.md`: user guide
- `test/ui-test-plan.md`: console UI test cases
