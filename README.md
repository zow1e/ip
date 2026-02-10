# Kiwi Project
This is a project for Kiwi task management application – a Java chatbot-style task manager inspired by Duke. Kiwi helps you organize your to-dos, deadlines, and events through natural language commands in a sleek GUI chat interface. Track your tasks, mark them complete, and stay productive!

## 🌟 What Kiwi Does
Add tasks: todo buy milk, deadline submit /by tomorrow 5pm, event meeting /from 2pm /to 4pm

Manage tasks: list, mark 1, unmark 2, delete 3, find project

Chat interface: Clean Duke-style GUI with task conversation flow

Persistent storage: Tasks saved to data/kiwi.txt

## 🎯 Intended Usage
Run ./gradlew run to launch the JavaFX chat GUI. Type commands like todo homework or list to manage your tasks interactively!

### Setting up in IntelliJ
Prerequisites: JDK 17, latest IntelliJ IDEA.

1. Open IntelliJ (Welcome screen → File > Close Project if needed)

2. Open project:
    - Click Open → Select project folder → OK
    - Accept all default prompts

3. Configure JDK 17:
    - File > Project Structure > Project
    - Set Project SDK → JDK 17
    - Set Project language level → SDK default

4. Verify setup:
    - Find src/main/java/kiwi/build/Launcher.java
    - Right-click → Run 'Launcher.main()'

### Expected: Kiwi GUI launches with chat interface!


Hello! I'm Kiwi 
> [chat input ready]


## Quick Start Commands:

./gradlew build      # Compile
./gradlew run        # Launch GUI  
./gradlew test       # Run tests

## Sample Usage:

todo buy groceries
deadline CS2103T /by tomorrow 11:59pm  
event meeting /from 2pm /to 4pm
list
mark 1
bye


Happy task managing! 🍃