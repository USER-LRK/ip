# Kaykay UI Test Plan

The runner compares each complete console transcript exactly, apart from newline style and a final newline. Inputs are sent to Kaykay in order. A failure stops the session immediately.

## Case 1: Add all Level 4 task types

**Aim:** Verify that todo, deadline, and event commands create the correct task subtypes and display their details.

Commands / console input:

```
todo borrow book
deadline return book /by 25 12 2026 18:30
event project meeting /from 26 12 2026 14:00 /to 26 12 2026 16:00
list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission added:
[T][ ] borrow book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Mission added:
[D][ ] return book (by: 25 12 2026 18:30)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Event scheduled:
[E][ ] project meeting (from: 26 12 2026 14:00 to: 26 12 2026 16:00)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are your current missions:
1. [T][ ] borrow book
2. [D][ ] return book (by: 25 12 2026 18:30)
3. [E][ ] project meeting (from: 26 12 2026 14:00 to: 26 12 2026 16:00)
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 2: Mark and unmark typed tasks

**Aim:** Verify inherited mark/unmark behavior works for tasks stored polymorphically.

Commands / console input:

```
todo revise notes
deadline submit report /by 27 12 2026 09:00
mark 1
unmark 2
list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission added:
[T][ ] revise notes
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Mission added:
[D][ ] submit report (by: 27 12 2026 09:00)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Mission complete! Nicely done.
[T][X] revise notes
____________________________________________________________
____________________________________________________________
Mission reopened. Let's get back to it.
[D][ ] submit report (by: 27 12 2026 09:00)
____________________________________________________________
____________________________________________________________
Here are your current missions:
1. [T][X] revise notes
2. [D][ ] submit report (by: 27 12 2026 09:00)
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 3: Parse and display standard date/time values

**Aim:** Verify dates and times are accepted and displayed in the standard format.

Commands / console input:

```
deadline do homework /by 28 12 2026 23:45
event orientation week /from 29 12 2026 08:00 /to 29 12 2026 10:30
deadline reject random /by Friday
event reject random /from no idea /to no idea
deadline reject invalid date /by 31 02 2026 10:00
event reject invalid time /from 30 12 2026 10:00 /to 30 12 2026 25:00
list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission added:
[D][ ] do homework (by: 28 12 2026 23:45)
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Event scheduled:
[E][ ] orientation week (from: 29 12 2026 08:00 to: 29 12 2026 10:30)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Mission snag: The deadline date/time 'Friday' is invalid. Please use dd MM yyyy HH:mm, for example 01 01 2026 18:30.
____________________________________________________________
____________________________________________________________
Mission snag: The event start date/time 'no idea' is invalid. Please use dd MM yyyy HH:mm, for example 01 01 2026 18:30.
____________________________________________________________
____________________________________________________________
Mission snag: The deadline date/time '31 02 2026 10:00' is invalid. Please use dd MM yyyy HH:mm, for example 01 01 2026 18:30.
____________________________________________________________
____________________________________________________________
Mission snag: The event end date/time '30 12 2026 25:00' is invalid. Please use dd MM yyyy HH:mm, for example 01 01 2026 18:30.
____________________________________________________________
____________________________________________________________
Here are your current missions:
1. [D][ ] do homework (by: 28 12 2026 23:45)
2. [E][ ] orientation week (from: 29 12 2026 08:00 to: 29 12 2026 10:30)
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 4: Reject empty and unknown commands

**Aim:** Verify that blank input, an empty todo, and arbitrary text produce predictable errors without adding tasks.

Commands / console input:

```

todo
blah
list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission snag: Please enter a command.
____________________________________________________________
____________________________________________________________
Mission snag: A todo needs a description. Try: todo <description>.
____________________________________________________________
____________________________________________________________
Mission snag: I don't recognise that command. Try todo, deadline, event, list, find, delete, mark, unmark, place, or bye.
____________________________________________________________
____________________________________________________________
Here are your current missions:
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 5: Reject invalid commands without changing task state

**Aim:** Verify that invalid commands between valid commands do not add tasks and provide specific correction guidance.

Commands / console input:

```
todo keep this
blah
deadline finish report /by 30 12 2026 17:00
deadline missing
event meeting /from 31 12 2026 10:00 /to 31 12 2026 11:00
event missing /from 31 12 2026 10:00
mark 99
list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission added:
[T][ ] keep this
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Mission snag: I don't recognise that command. Try todo, deadline, event, list, find, delete, mark, unmark, place, or bye.
____________________________________________________________
____________________________________________________________
Mission added:
[D][ ] finish report (by: 30 12 2026 17:00)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Mission snag: A deadline needs a description and a date. Try: deadline <description> /by <date>.
____________________________________________________________
____________________________________________________________
Event scheduled:
[E][ ] meeting (from: 31 12 2026 10:00 to: 31 12 2026 11:00)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Mission snag: An event needs a description, start, and end. Try: event <description> /from <start> /to <end>.
____________________________________________________________
____________________________________________________________
Mission snag: Please provide an existing task number to mark or unmark.
____________________________________________________________
____________________________________________________________
Here are your current missions:
1. [T][ ] keep this
2. [D][ ] finish report (by: 30 12 2026 17:00)
3. [E][ ] meeting (from: 31 12 2026 10:00 to: 31 12 2026 11:00)
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 6: Delete tasks and reject invalid task numbers

**Aim:** Verify that deleting a task removes it, renumbers the remaining tasks, and rejects missing or non-existent task numbers.

Commands / console input:

```
todo first task
deadline second task /by 01 01 2027 12:00
event third task /from 02 01 2027 10:00 /to 02 01 2027 11:00
delete 2
delete 9
delete nope
list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission added:
[T][ ] first task
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Mission added:
[D][ ] second task (by: 01 01 2027 12:00)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Event scheduled:
[E][ ] third task (from: 02 01 2027 10:00 to: 02 01 2027 11:00)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Mission removed:
  [D][ ] second task (by: 01 01 2027 12:00)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Mission snag: Please provide an existing task number to delete.
____________________________________________________________
____________________________________________________________
Mission snag: Please provide an existing task number to delete.
____________________________________________________________
____________________________________________________________
Here are your current missions:
1. [T][ ] first task
2. [E][ ] third task (from: 02 01 2027 10:00 to: 02 01 2027 11:00)
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 7: Exercise automatic task-list saving

**Aim:** Verify that task-list mutations save the final task state to the data file.

Commands / console input:

```
todo persist this
mark 1
unmark 1
delete 1
todo saved final
mark 1
list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission added:
[T][ ] persist this
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Mission complete! Nicely done.
[T][X] persist this
____________________________________________________________
____________________________________________________________
Mission reopened. Let's get back to it.
[T][ ] persist this
____________________________________________________________
____________________________________________________________
Mission removed:
  [T][ ] persist this
Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
Mission added:
[T][ ] saved final
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Mission complete! Nicely done.
[T][X] saved final
____________________________________________________________
____________________________________________________________
Here are your current missions:
1. [T][X] saved final
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

Expected file contents:

```
T | 1 | saved final
```

Restart console input:

```
list
bye
```

Expected restart output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Here are your current missions:
1. [T][X] saved final
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 8: Preserve special characters across saving and loading

**Aim:** Verify that pipe characters and backslashes in task descriptions survive a save-and-restart cycle.

Commands / console input:

```
todo pipe | slash \
list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission added:
[T][ ] pipe | slash \
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Here are your current missions:
1. [T][ ] pipe | slash \
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

Expected file contents:

```
T | 0 | pipe \| slash \\
```

Restart console input:

```
list
bye
```

Expected restart output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Here are your current missions:
1. [T][ ] pipe | slash \
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 9: Reject malformed saved task data

**Aim:** Verify that malformed saved data is handled without crashing and starts the chatbot with an empty task list.

Initial file contents:

```
Q | 2 | broken task
```

Commands / console input:

```
list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission snag: I couldn't load your data. Please check the data file.
____________________________________________________________
____________________________________________________________
Here are your current missions:
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 10: Exit cleanly when input ends without bye

**Aim:** Verify that the chatbot exits normally when input ends without an explicit bye command.

Commands / console input:

```
list
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Here are your current missions:
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 11: Save and reload typed deadline and event date/times

**Aim:** Verify that deadline and event `LocalDateTime` values survive saving and restarting.

Commands / console input:

```
deadline persist deadline /by 25 12 2026 18:30
event persist event /from 26 12 2026 09:00 /to 26 12 2026 10:00
list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission added:
[D][ ] persist deadline (by: 25 12 2026 18:30)
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Event scheduled:
[E][ ] persist event (from: 26 12 2026 09:00 to: 26 12 2026 10:00)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are your current missions:
1. [D][ ] persist deadline (by: 25 12 2026 18:30)
2. [E][ ] persist event (from: 26 12 2026 09:00 to: 26 12 2026 10:00)
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

Expected file contents:

```
D | 0 | persist deadline | 25 12 2026 18:30
E | 0 | persist event | 26 12 2026 09:00 | 26 12 2026 10:00
```

Restart console input:

```
list
bye
```

Expected restart output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Here are your current missions:
1. [D][ ] persist deadline (by: 25 12 2026 18:30)
2. [E][ ] persist event (from: 26 12 2026 09:00 to: 26 12 2026 10:00)
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 12: Find tasks by description keyword

**Aim:** Verify that find displays matching tasks in their original order, ignores letter case, and handles no matches or a missing keyword.

Commands / console input:

```
todo read book
deadline return book /by 06 06 2027 18:30
todo buy milk
find BOOK
find missing
find
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission added:
[T][ ] read book
Now you have 1 task in the list.
____________________________________________________________
____________________________________________________________
Mission added:
[D][ ] return book (by: 06 06 2027 18:30)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Mission added:
[T][ ] buy milk
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here's what I found:
1. [T][ ] read book
2. [D][ ] return book (by: 06 06 2027 18:30)
____________________________________________________________
____________________________________________________________
Here's what I found:
____________________________________________________________
____________________________________________________________
Mission snag: A find command needs a keyword. Try: find <keyword>.
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 13: Manage and persist places

**Aim:** Verify that places can be added with optional details, edited, searched, listed, deleted, saved, and reloaded independently of tasks.

Commands / console input:

```
place add Burnt Ends /type restaurant /location Dempsey /visited 10 09 2026 /rating 5 /notes Great brisket
place add NUS Computing
place edit 1 /rating 4 /notes Worth revisiting
place find RESTAURANT
place list
place delete 2
place list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Location logged:
[P] Burnt Ends (type: restaurant; location: Dempsey; visited: 10 09 2026; rating: 5/5; notes: Great brisket)
Now you have 1 place saved.
____________________________________________________________
____________________________________________________________
Location logged:
[P] NUS Computing
Now you have 2 places saved.
____________________________________________________________
____________________________________________________________
Location updated:
[P] Burnt Ends (type: restaurant; location: Dempsey; visited: 10 09 2026; rating: 4/5; notes: Worth revisiting)
____________________________________________________________
____________________________________________________________
Here are the matching locations:
1. [P] Burnt Ends (type: restaurant; location: Dempsey; visited: 10 09 2026; rating: 4/5; notes: Worth revisiting)
____________________________________________________________
____________________________________________________________
Here are your logged locations:
1. [P] Burnt Ends (type: restaurant; location: Dempsey; visited: 10 09 2026; rating: 4/5; notes: Worth revisiting)
2. [P] NUS Computing
____________________________________________________________
____________________________________________________________
Location removed:
  [P] NUS Computing
Now you have 1 place saved.
____________________________________________________________
____________________________________________________________
Here are your logged locations:
1. [P] Burnt Ends (type: restaurant; location: Dempsey; visited: 10 09 2026; rating: 4/5; notes: Worth revisiting)
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

Expected file contents:

```
P | Burnt Ends | restaurant | Dempsey | 10 09 2026 | 4 | Worth revisiting
```

Restart console input:

```
place list
bye
```

Expected restart output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Here are your logged locations:
1. [P] Burnt Ends (type: restaurant; location: Dempsey; visited: 10 09 2026; rating: 4/5; notes: Worth revisiting)
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```

## Case 14: Reject invalid place commands

**Aim:** Verify that invalid place details and commands provide guidance without adding places.

Commands / console input:

```
place add
place add Cafe /rating 6
place add Cafe /visited 31 02 2026
place find
place edit 1
place edit 1 /rating 4
place delete nope
place dance
place list
bye
```

Expected output:

```
____________________________________________________________
#   #   ###   #   #  #   #   ###   #   #
#  #   #   #   # #   #  #   #   #   # #
###    #####    #    ###    #####    #
#  #   #   #    #    #  #   #   #    #
#   #  #   #    #    #   #  #   #    #
____________________________________________________________
Kaykay online!
What's our next mission?
____________________________________________________________
____________________________________________________________
Mission snag: A place needs a name. Try: place add <name> [/type <type>] [/location <location>] [/visited <date>] [/rating <1-5>] [/notes <notes>].
____________________________________________________________
____________________________________________________________
Mission snag: A place rating must be a whole number from 1 to 5.
____________________________________________________________
____________________________________________________________
Mission snag: The place visit date '31 02 2026' is invalid. Please use dd MM yyyy, for example 10 09 2026.
____________________________________________________________
____________________________________________________________
Mission snag: A place find command needs a keyword. Try: place find <keyword>.
____________________________________________________________
____________________________________________________________
Mission snag: A place edit needs a place number and at least one field. Try: place edit <number> /rating <1-5>.
____________________________________________________________
____________________________________________________________
Mission snag: Please provide an existing place number to edit.
____________________________________________________________
____________________________________________________________
Mission snag: Please provide an existing place number to delete.
____________________________________________________________
____________________________________________________________
Mission snag: I don't recognise that place command. Try place add, place list, place find, place edit, or place delete.
____________________________________________________________
____________________________________________________________
Here are your logged locations:
____________________________________________________________
____________________________________________________________
All changes saved. Kaykay signing off!
____________________________________________________________
```
