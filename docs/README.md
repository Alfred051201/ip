# Dukey User Guide

Dukey is a calm task librarian that helps you track todos, deadlines, events, and useful task statistics.

## Adding Tasks

Add a todo:

```text
todo borrow book
```

Add a deadline using `yyyy-MM-dd HHmm`:

```text
deadline return book /by 2099-12-06 1800
```

Add an event using `yyyy-MM-dd HHmm`:

```text
event project meeting /from 2099-08-06 1400 /to 2099-08-06 1600
```

## Managing Tasks

List all tasks:

```text
list
```

Mark a task as done:

```text
mark 2
```

Unmark a task:

```text
unmark 2
```

Delete a task:

```text
delete 3
```

## Finding Tasks

Search task descriptions:

```text
find book
```

Show deadlines and events on a date:

```text
on 2099-12-06
```

## Viewing Statistics

Show task statistics:

```text
stats
```

Expected output:

```text
Here are your task statistics:
Total tasks: 5
Completed tasks: 2
Pending tasks: 3
Completed in the current calendar week: 1
Completed in the current calendar month: 2
```

The `stats` command does not take arguments. For example, `stats today` is rejected.

Dukey records the date and time when a task is marked as done. Tasks that were completed before this feature was
introduced may not have a saved completion timestamp, so they are counted as completed tasks but not as completions in
the current week or month.

## Exiting

Exit Dukey:

```text
bye
```
