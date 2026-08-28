# UI Test Plan

Program command: `java -cp src/main/java Dukey`
Compile command: `javac src/main/java/*.java`
Data file: `src/main/data/dukey.txt`

## Test Case: Loads tasks from disk

Aim: Verify that the chatbot loads todo, deadline, and event tasks from the data file when it starts.

Initial data:
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm-4pm
T | 1 | join sports club
```

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

Expected data:
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm-4pm
T | 1 | join sports club
```

## Test Case: Saves added todo automatically

Aim: Verify that adding a todo updates the data file without needing another task-list command.

Initial data:
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm-4pm
T | 1 | join sports club
```

Input:
```text
todo borrow book
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
Bye. Hope to see you again soon!
____________________________________________________________
```

Expected data:
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm-4pm
T | 1 | join sports club
T | 0 | borrow book
```

## Test Case: Saves added deadline and event automatically

Aim: Verify that added deadline and event tasks are saved in the data file using their file formats.

Initial data:
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm-4pm
T | 1 | join sports club
```

Input:
```text
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
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
Bye. Hope to see you again soon!
____________________________________________________________
```

Expected data:
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm-4pm
T | 1 | join sports club
D | 0 | return book | Sunday
E | 0 | project meeting | Mon 2pm-4pm
```

## Test Case: Saves mark and unmark automatically

Aim: Verify that marking and unmarking tasks updates the saved done statuses.

Initial data:
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm-4pm
T | 1 | join sports club
```

Input:
```text
mark 2
unmark 4
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
Nice! I've marked this task as done:
  [D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
OK, I've marked this task as not done yet:
  [T][ ] join sports club
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Expected data:
```text
T | 1 | read book
D | 1 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm-4pm
T | 0 | join sports club
```

## Test Case: Saves delete automatically

Aim: Verify that deleting a task removes it from the data file.

Initial data:
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm-4pm
T | 1 | join sports club
```

Input:
```text
delete 3
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
Noted. I've removed this task:
  [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Expected data:
```text
T | 1 | read book
D | 0 | return book | June 6th
T | 1 | join sports club
```

## Test Case: Does not save invalid commands as tasks

Aim: Verify that command errors do not change the saved task data.

Initial data:
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm-4pm
T | 1 | join sports club
```

Input:
```text
todo
deadline return book Sunday
event project meeting /from Mon 2pm /to
delete 999
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
 OOPS!!! Please use: deadline {DESCRIPTION} /by {WHEN}
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a event task date/time after /to.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
 OOPS!!! I'm sorry, but I don't know what that means :-(
____________________________________________________________
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
```

Expected data:
```text
T | 1 | read book
D | 0 | return book | June 6th
E | 0 | project meeting | Aug 6th 2pm-4pm
T | 1 | join sports club
```
