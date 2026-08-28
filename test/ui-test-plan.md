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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[D][ ] return book (by: 25 12 2026 18:30)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[E][ ] project meeting (from: 26 12 2026 14:00 to: 26 12 2026 16:00)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] borrow book
2. [D][ ] return book (by: 25 12 2026 18:30)
3. [E][ ] project meeting (from: 26 12 2026 14:00 to: 26 12 2026 16:00)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[T][ ] revise notes
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[D][ ] submit report (by: 27 12 2026 09:00)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
[T][X] revise notes
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
[D][ ] submit report (by: 27 12 2026 09:00)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][X] revise notes
2. [D][ ] submit report (by: 27 12 2026 09:00)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[D][ ] do homework (by: 28 12 2026 23:45)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[E][ ] orientation week (from: 29 12 2026 08:00 to: 29 12 2026 10:30)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS! The deadline date/time 'Friday' is invalid. Please use dd MM yyyy HH:mm, for example 01 01 2026 18:30.
____________________________________________________________
____________________________________________________________
OOPS! The event start date/time 'no idea' is invalid. Please use dd MM yyyy HH:mm, for example 01 01 2026 18:30.
____________________________________________________________
____________________________________________________________
OOPS! The deadline date/time '31 02 2026 10:00' is invalid. Please use dd MM yyyy HH:mm, for example 01 01 2026 18:30.
____________________________________________________________
____________________________________________________________
OOPS! The event end date/time '30 12 2026 25:00' is invalid. Please use dd MM yyyy HH:mm, for example 01 01 2026 18:30.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [D][ ] do homework (by: 28 12 2026 23:45)
2. [E][ ] orientation week (from: 29 12 2026 08:00 to: 29 12 2026 10:30)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Case 4: Reject empty and unknown commands

**Aim:** Verify that an empty todo and arbitrary text produce errors without adding tasks.

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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
OOPS! A todo needs a description. Try: todo <description>.
____________________________________________________________
____________________________________________________________
OOPS! I don't recognise that command. Try todo, deadline, event, list, find, delete, mark, unmark, or bye.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[T][ ] keep this
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS! I don't recognise that command. Try todo, deadline, event, list, find, delete, mark, unmark, or bye.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[D][ ] finish report (by: 30 12 2026 17:00)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS! A deadline needs a description and a date. Try: deadline <description> /by <date>.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[E][ ] meeting (from: 31 12 2026 10:00 to: 31 12 2026 11:00)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS! An event needs a description, start, and end. Try: event <description> /from <start> /to <end>.
____________________________________________________________
____________________________________________________________
OOPS! Please provide an existing task number to mark or unmark.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] keep this
2. [D][ ] finish report (by: 30 12 2026 17:00)
3. [E][ ] meeting (from: 31 12 2026 10:00 to: 31 12 2026 11:00)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[T][ ] first task
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[D][ ] second task (by: 01 01 2027 12:00)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[E][ ] third task (from: 02 01 2027 10:00 to: 02 01 2027 11:00)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [D][ ] second task (by: 01 01 2027 12:00)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
OOPS! Please provide an existing task number to delete.
____________________________________________________________
____________________________________________________________
OOPS! Please provide an existing task number to delete.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] first task
2. [E][ ] third task (from: 02 01 2027 10:00 to: 02 01 2027 11:00)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[T][ ] persist this
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
[T][X] persist this
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
[T][ ] persist this
____________________________________________________________
____________________________________________________________
Noted. I've removed this task:
  [T][ ] persist this
Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[T][ ] saved final
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
[T][X] saved final
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][X] saved final
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][X] saved final
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[T][ ] pipe | slash \
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] pipe | slash \
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] pipe | slash \
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
OOPS! I couldn't load your tasks. Please check the data file.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[D][ ] persist deadline (by: 25 12 2026 18:30)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[E][ ] persist event (from: 26 12 2026 09:00 to: 26 12 2026 10:00)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [D][ ] persist deadline (by: 25 12 2026 18:30)
2. [E][ ] persist event (from: 26 12 2026 09:00 to: 26 12 2026 10:00)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [D][ ] persist deadline (by: 25 12 2026 18:30)
2. [E][ ] persist event (from: 26 12 2026 09:00 to: 26 12 2026 10:00)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
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
Hello! I'm kaykay.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[D][ ] return book (by: 06 06 2027 18:30)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[T][ ] buy milk
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
1. [T][ ] read book
2. [D][ ] return book (by: 06 06 2027 18:30)
____________________________________________________________
____________________________________________________________
Here are the matching tasks in your list:
____________________________________________________________
____________________________________________________________
OOPS! A find command needs a keyword. Try: find <keyword>.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
