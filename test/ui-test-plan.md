# UI Test Plan

Program command: `java -cp src/main/java Dukey`
Compile command: `javac src/main/java/*.java`

## Test Case: Exits on bye

Aim: Verify that the chatbot starts, accepts `bye`, and exits with the farewell message.

Input:
```text
bye
```

Expected output:
```text
____________________________________________________________
 ____        _              
|  _ \ _   _| | _____ _   _ 
| | | | | | | |/ / _ \ | | |
| |_| | |_| |   <  __/ |_| |
|____/ \__,_|_|\_\___|\__, |
                       |___/ 

Hello! I'm Dukey.
What can I do for you?
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case: Lists initial task types

Aim: Verify that the initial list shows todo, deadline, and event tasks with the correct type icons and status icons.

Input:
```text
list
bye
```

Expected output:
```text
____________________________________________________________
 ____        _              
|  _ \ _   _| | _____ _   _ 
| | | | | | | |/ / _ \ | | |
| |_| | |_| |   <  __/ |_| |
|____/ \__,_|_|\_\___|\__, |
                       |___/ 

Hello! I'm Dukey.
What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
4.[T][X] join sports club
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case: Adds a todo task

Aim: Verify that `todo` creates a Todo task, shows the added task, updates the task count, and includes the task in `list`.

Input:
```text
todo borrow book
list
bye
```

Expected output:
```text
____________________________________________________________
 ____        _              
|  _ \ _   _| | _____ _   _ 
| | | | | | | |/ / _ \ | | |
| |_| | |_| |   <  __/ |_| |
|____/ \__,_|_|\_\___|\__, |
                       |___/ 

Hello! I'm Dukey.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [T][ ] borrow book
Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
4.[T][X] join sports club
5.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case: Adds deadline and event tasks

Aim: Verify that `deadline` and `event` commands parse their keywords and create the correct task subclasses.

Input:
```text
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

Expected output:
```text
____________________________________________________________
 ____        _              
|  _ \ _   _| | _____ _   _ 
| | | | | | | |/ / _ \ | | |
| |_| | |_| |   <  __/ |_| |
|____/ \__,_|_|\_\___|\__, |
                       |___/ 

Hello! I'm Dukey.
What can I do for you?
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 5 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 6 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
1.[T][X] read book
2.[D][ ] return book (by: June 6th)
3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
4.[T][X] join sports club
5.[D][ ] return book (by: Sunday)
6.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case: Handles todo and unknown command errors

Aim: Verify that empty `todo` input and unknown commands show error messages instead of adding tasks.

Input:
```text
todo
blah
bye
```

Expected output:
```text
____________________________________________________________
 ____        _              
|  _ \ _   _| | _____ _   _ 
| | | | | | | |/ / _ \ | | |
| |_| | |_| |   <  __/ |_| |
|____/ \__,_|_|\_\___|\__, |
                       |___/ 

Hello! I'm Dukey.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
 OOPS!!! I'm sorry, but I don't know what that means :-(
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case: Handles mark and unmark errors

Aim: Verify that missing, non-numeric, and out-of-range task numbers are reported without crashing.

Input:
```text
mark
mark abc
mark 999
unmark
unmark abc
unmark 999
bye
```

Expected output:
```text
____________________________________________________________
 ____        _              
|  _ \ _   _| | _____ _   _ 
| | | | | | | |/ / _ \ | | |
| |_| | |_| |   <  __/ |_| |
|____/ \__,_|_|\_\___|\__, |
                       |___/ 

Hello! I'm Dukey.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a task number to mark.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a task number to unmark.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case: Handles deadline command errors

Aim: Verify that invalid deadline commands report missing descriptions, missing `/by`, and missing `/by` values.

Input:
```text
deadline
deadline return book Sunday
deadline /by Sunday
deadline return book /by
bye
```

Expected output:
```text
____________________________________________________________
 ____        _              
|  _ \ _   _| | _____ _   _ 
| | | | | | | |/ / _ \ | | |
| |_| | |_| |   <  __/ |_| |
|____/ \__,_|_|\_\___|\__, |
                       |___/ 

Hello! I'm Dukey.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a deadline task description.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please use: deadline {DESCRIPTION} /by {WHEN}
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a deadline task description.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a deadline task date/time after /by.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case: Handles event command errors

Aim: Verify that invalid event commands report missing descriptions, missing keywords, and missing `/from` or `/to` values.

Input:
```text
event
event project meeting
event /from Mon 2pm /to 4pm
event project meeting /from /to 4pm
event project meeting /from Mon 2pm /to
bye
```

Expected output:
```text
____________________________________________________________
 ____        _              
|  _ \ _   _| | _____ _   _ 
| | | | | | | |/ / _ \ | | |
| |_| | |_| |   <  __/ |_| |
|____/ \__,_|_|\_\___|\__, |
                       |___/ 

Hello! I'm Dukey.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a event task description.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please use: event {DESCRIPTION} /from {WHEN} /to {WHEN}
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a event task description.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a event task date/time after /from.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a event task date/time after /to.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test Case: Handles malformed attached event keyword

Aim: Verify that an attached keyword such as `/to4pm` reports a command format error instead of crashing.

Input:
```text
event project meeting /from Mon 2pm /to4pm
bye
```

Expected output:
```text
____________________________________________________________
 ____        _              
|  _ \ _   _| | _____ _   _ 
| | | | | | | |/ / _ \ | | |
| |_| | |_| |   <  __/ |_| |
|____/ \__,_|_|\_\___|\__, |
                       |___/ 

Hello! I'm Dukey.
What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! Please use: event {DESCRIPTION} /from {WHEN} /to {WHEN}
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```
