# Hora - Productivity App

Hora is a personal task management app built in Kotlin using Jetpack Compose.  
It is designed for users who want a **free, customizable productivity app** with core task features and future AI integrations.

## Features

- Easy task creation
- Task reminders
- Timer-based tasks (Pomodoro-style)
- Task actions: toggle complete, drop, delete
- Dialog interactions for task management

## Future Features

- Google Calendar sync
- AI-assisted smart task creation
- Task creation via WhatsApp commands
- Advanced reminders and notifications

## Project Structure

- `MainActivity.kt` – Hosts the main Compose content
- `TaskViewModel.kt` – Handles task state and business logic
- `Task.kt` – Data models
- `HomeScreen.kt` – Composables for main UI
- `TaskItem.kt` – Individual task UI and interactions

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/supunhg/hora.git
```
2. Open the project in Android Studio (Arctic Fox or later recommended).
3. Ensure Gradle sync is complete.
4. Run the app on an emulator or physical device with **minSdk 24+**.

## Dependencies

* Jetpack Compose
* Material 3
* AndroidX Lifecycle (ViewModel, Compose integration)

## License

This project is **MIT licensed**. Free to use, modify, and distribute.
