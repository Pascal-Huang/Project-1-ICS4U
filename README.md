# Project-StuddyBuddy
StuddyBuddy is a gamified productivity application built in Java. It replaces traditional, boring focus 
timers with a virtual pet that lives on your desktop. Your goal is to keep your pet alive and help it level up 
by staying focused—but if you get distracted, your pet pays the price!

Core Concept
StuddyBuddy actively monitors your computer for distractions.

  - Focusing: If you are studying and avoiding blacklisted apps, your pet gains XP and levels up.

  - Distracted: If you open a blacklisted application (like Discord, Steam, or video games), your pet takes Damage (-HP) and loses XP.

  - Game Over: If your pet's health reaches 0, the pet dies, all progress is lost and the application closes.

Main Features

  - Real-Time App Monitoring: Uses a background thread to scan running system processes and checks them against a customizable blacklist.

  - Gamified Progression: A mathematically scaled leveling system where required XP increases as your pet grows.

  - Dynamic GUI: A clean, responsive dashboard built with Java Swing featuring:

  - Animated pixel-art pet graphics.

  - Live-updating Health and XP progress bars.

  - Real-time status indicators ("Scanning...", "Focused", "Distracted!").

  - Profile System: Support for multiple user profiles. Data (Pet Name, Health, XP, Level, and Custom Blacklists) is automatically saved and loaded via local text files.

  - Settings & Customization: Users can pause the tracker, toggle "Deep Work" mode, and dynamically add or remove apps from their blacklist.

Technical Details

  - Language: Java

  - UI Framework: Java Swing

  - Threading: Utilizes multithreading to run the scanning loop in the background without freezing the user interface.

  - Data Persistence: Custom classes handle reading and writing application state to local text files using standard Java File I/O.

  - Process Scanning: Integrates with the native OS to read active tasks and monitor applications.


How to Run

1. Clone or download the repository.

2. Compile the Java files.

Run the main class.

3. Select a profile, name your pet, and get to work!

4. Stay focused, level up, and don't let your buddy down!
