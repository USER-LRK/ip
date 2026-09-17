# Kaykay project template

This is a project template for a greenfield Java project for the Kaykay chatbot. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, run the application using `./gradlew run` (or the equivalent Gradle run configuration in IntelliJ). This currently opens the initial JavaFX window. The console version remains available by running `kaykay.Kaykay.main()` directly; it will continue to show the greeting and farewell output from Kaykay.
   ```
   ____________________________________________________________
   #   #   ###   #   #  #   #   ###   #   #
   #  #   #   #   # #   #  #   #   #   # #
   ###    #####    #    ###    #####    #
   #  #   #   #    #    #  #   #   #    #
   #   #  #   #    #    #   #  #   #    #
   Kaykay online!
   What's our next mission?
   ____________________________________________________________
   All changes saved. Kaykay signing off!
   ____________________________________________________________
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Managing places

Kaykay stores places separately from tasks. A command such as `todo visit Burnt Ends` creates a task;
only the following `place` commands create or change place records:

| Command | Purpose |
| --- | --- |
| `place add <name>` | Save a place. |
| `place list` | Display all saved places. |
| `place find <keyword>` | Search across all recorded place details. |
| `place edit <number> /<field> <value>` | Change one or more details of a saved place. |
| `place delete <number>` | Delete a saved place. |

When adding a place, its name is required. The optional fields are `/type`, `/location`, `/visited`,
`/rating`, and `/notes`. Visit dates use `dd MM yyyy`, and ratings are whole numbers from 1 to 5.

For example:

```
place add Burnt Ends /type restaurant /location Dempsey /visited 10 09 2026 /rating 5 /notes Great brisket
place edit 1 /rating 4 /notes Worth revisiting
place find restaurant
```
