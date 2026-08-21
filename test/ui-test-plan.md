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
