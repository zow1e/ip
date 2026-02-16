# Kiwi User Guide


> *Kiwi is your personal task manager chatbot. Chat with Kiwi to add, list, delete, and manage your tasks with deadlines and events.*

## Quick Start

1. Type commands in the chat input at the bottom
2. Press **Enter** or click **Send** 
3. Kiwi responds instantly with task updates

**Supported task types:**
- `todo` - Tasks without deadlines
- `deadline` - Tasks by specific date/time  
- `event` - Events with duration

## Features

### Adding Deadlines
Add tasks that expire by a specific date/time.

**Format:** `deadline KEYWORD /by DATE [TIME]`

**Examples:**
```
deadline project meeting /by tomorrow 2pm
deadline submit assignment /by Apr 16 2359
deadline CS2103T milestone /by next Friday
```

**Expected output:**
```
Got it. I've added this task:
[D][Apr 16 14:00] project meeting
Now you have 4 tasks in the list.
```

### Adding Todos
Simple tasks without deadlines.

**Format:** `todo KEYWORD`

**Example:**
```
todo read book
```

**Expected output:**
```
Got it. I've added this task:
[T][ ] read book
Now you have 5 tasks in the list.
```

### Adding Events
Scheduled events with start/end times.

**Format:** `event KEYWORD /from START /to END`

**Example:**
```
event project meeting /from 2pm /to 4pm
```

**Expected output:**
```
Got it. I've added this event:
[E][ ] project meeting (at: 2pm-4pm)
Now you have 6 tasks in the list.
```

### Listing Tasks
View all your current tasks.

**Format:** `list`

**Expected output:**
```
Here are the tasks in your list:
1.[T][ ] run
2.[T][ ] climb
3.[D][Apr 16 23:59] capstone
4.[E][Mar 7 10:00-19:30] open house
```

### Deleting Tasks
Remove tasks by index number.

**Format:** `delete INDEX`

**Example:** `delete 2`

**Expected output:**
```
Noted. I've removed this task:
[T][ ] climb
Now you have 3 tasks in the list.
```

### Marking Tasks
Mark tasks as done or toggle status.

**Format:** `mark INDEX` | `unmark INDEX`

**Examples:**
```
mark 1
unmark 1
```

**Expected output:**
```
[T][X] run ✓ Nice job, you've completed 1 task!
```

### Finding Tasks
Search tasks by keyword.

**Format:** `find KEYWORD`

**Example:** `find meeting`

**Expected output:**
```
Here are the matching tasks in your list:
1.[D][Apr 16 14:00] project meeting
```

## Data Persistence
- All tasks **automatically saved** to `kiwi.txt`
- Tasks **load on startup** 
- Data survives app restarts

## Task Storage Format
```
D | 1 | project meeting | Apr 16 14:00
T | 0 | read book
E | 0 | team dinner | 2pm-4pm
```
**Legend:** `Type | Done(0/1) | Description | Time/Date`

## Command Summary

| Command | Format | Example |
|---------|--------|---------|
| Add Todo | `todo NAME` | `todo homework` |
| Add Deadline | `deadline NAME /by DATE` | `deadline report /by tomorrow` |
| Add Event | `event NAME /from START /to END` | `event party /from 8pm /to 10pm` |
| List | `list` | `list` |
| Delete | `delete INDEX` | `delete 1` |
| Mark Done | `mark INDEX` | `mark 2` |
| Unmark | `unmark INDEX` | `unmark 2` |
| Find | `find KEYWORD` | `find meeting` |
| Help | `help` | `help` |
| Bye | `bye` | `bye` |

**Pro Tip:** Type `list` anytime to see task numbers for delete/mark commands!