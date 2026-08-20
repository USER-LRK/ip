# Kaykay UI Test Plan

The runner compares each complete console transcript exactly, apart from newline style and a final newline. Inputs are sent to Kaykay in order. A failure stops the session immediately.

## Case 1: Add all Level 4 task types

**Aim:** Verify that todo, deadline, and event commands create the correct task subtypes and display their details.

Commands / console input:

```
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
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
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][ ] borrow book
2. [D][ ] return book (by: Sunday)
3. [E][ ] project meeting (from: Mon 2pm to: 4pm)
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
deadline submit report /by Friday
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
[D][ ] submit report (by: Friday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! I've marked this task as done:
[T][X] revise notes
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
[D][ ] submit report (by: Friday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [T][X] revise notes
2. [D][ ] submit report (by: Friday)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Case 3: Preserve date/time strings exactly

**Aim:** Verify dates and times are stored and displayed as user-provided strings without date conversion.

Commands / console input:

```
deadline do homework /by no idea :-p
event orientation week /from 4/10/2019 /to 11/10/2019
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
[D][ ] do homework (by: no idea :-p)
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1. [D][ ] do homework (by: no idea :-p)
2. [E][ ] orientation week (from: 4/10/2019 to: 11/10/2019)
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
OOPS! A todo needs a description.
____________________________________________________________
____________________________________________________________
OOPS! I don't recognise that command. Try todo, deadline, event, list, mark, unmark, or bye.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
