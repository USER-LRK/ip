# Kaykay User Guide

Kaykay is your mission-control companion for keeping track of todos, deadlines,
events, and saved locations.

## Adding tasks

### Todos

Use `todo` followed by a description.

```text
todo borrow library book
```

### Deadlines

Use `deadline` followed by a description and a `/by` date/time.

```text
deadline submit report /by 25 12 2026 18:30
```

### Events

Use `event` followed by a description, a `/from` start date/time, and a `/to`
end date/time.

```text
event project meeting /from 26 12 2026 14:00 /to 26 12 2026 16:00
```

If a task has the same type and details as an existing task, Kaykay reports the
existing mission instead of adding a duplicate. Tasks with the same description
but different types or date/time details are still allowed.

## Date and time format

Deadline and event date/times must use this exact format. The time is compulsory:

```text
dd MM yyyy HH:mm
```

The day, month, and year are separated by spaces. The format means:

- `dd`: two-digit day
- `MM`: two-digit month
- `yyyy`: four-digit year
- `HH`: hour in 24-hour time
- `mm`: two-digit minute

For example, `05 01 2026 06:07` means 5 January 2026 at 6:07 AM, while
`05 01 2026 18:07` means 6:07 PM.

Values such as `Friday`, `10am`, `2026-01-05`, or `31 02 2026 10:00` are not
accepted. An event's end must be later than its start. Kaykay will
identify invalid input and explain how to correct it.

## Managing tasks

To find tasks whose descriptions contain a keyword, use `find`:

```text
find book
```

Searches ignore letter case and display matching tasks in their original order.

Use the following commands:

```text
list       Show all tasks
find book  Show tasks whose descriptions contain "book"
mark 1     Mark task 1 as done
unmark 1   Mark task 1 as not done
delete 1   Delete task 1
bye        Exit Kaykay
```

Kaykay saves task changes automatically. Date/time values are saved in the same
standard format shown above.

## Existing save files

Save files created before the standard date/time format was introduced are not
supported if they contain free-form deadline or event date strings. Such files
must be cleared or recreated using the new format.

If Kaykay cannot load a save file, it reports the problem and blocks further
writes for that run. The existing file remains unchanged so it can be repaired
before Kaykay is restarted.
