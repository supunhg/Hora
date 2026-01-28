# Horus - Productivity App

Horus is a personal task management app built with Kotlin and Jetpack Compose.  
A **free, minimalist, offline-first** productivity app designed for simplicity and efficiency.

## ✨ Features

### Core Functionality
- **Effortless Task Management** - Quick add with chat-style input (send button on right)
- **Three-State Task Lifecycle**:
  - Tap to toggle: Pending ↔ Done
  - Long-press for options: Drop or Delete
- **Smart Task Counter** - Real-time tracking of pending and completed tasks
- **Persistent Storage** - All tasks saved locally with Room database

### Date-Based Organization
- **Daily Sessions** - Automatic day rollover at midnight
- **Custom Day Titles** - Personalize each day with custom names
- **Task History** - Browse and manage all previous days
- **Swipe to Delete** - Remove old history entries with a swipe gesture

### Minimalist Design
- **Clean Interface** - Spacious 24dp padding, peaceful colors
- **Status Indicators** - Color-coded dots for task states
- **Smooth Animations** - Polished transitions and interactions
- **Material 3** - Dynamic color support (Android 12+)

## 🎯 Simple Enhancements to Consider

Without bloating the app, these minimal additions could improve usability:
- **Task Reordering** - Drag-and-drop to prioritize
- **Quick Filters** - Show/hide done or dropped tasks
- **Task Notes** - Optional details on long-press
- **Dark Mode Toggle** - Manual override for system theme
- **Export/Backup** - Save tasks as text or JSON
- **Task Statistics** - Weekly/monthly completion insights
- **Undo Delete** - Short-term recovery for accidents
- **Task Search** - Find tasks across all dates

Future integrations (when ready):
- Google Calendar sync
- AI-assisted task creation
- WhatsApp command parsing
- Smart notifications

## 🚀 Getting Started

1. Clone the repository:
```bash
git clone https://github.com/supunhg/horus.git
```
2. Open in Android Studio (Ladybug or later recommended)
3. Sync Gradle dependencies
4. Run on emulator or device (minSdk 24+)

## 🛠️ Tech Stack

- **Kotlin** - 2.0.21
- **Jetpack Compose** - BOM 2024.09.00
- **Material 3** - Modern design system
- **Room Database** - 2.6.1 for local persistence
- **Coroutines & Flow** - Reactive data streams
- **MVVM Architecture** - Clean separation of concerns
- **KSP** - 2.0.21-1.0.28 for annotation processing

## 📁 Project Structure

- [`MainActivity.kt`](app/src/main/java/com/supunhg/horus/MainActivity.kt) - Main UI with all Compose screens
- [`TaskViewModel.kt`](app/src/main/java/com/supunhg/horus/TaskViewModel.kt) - Business logic and state management
- [`data/TaskEntity.kt`](app/src/main/java/com/supunhg/horus/data/TaskEntity.kt) - Room database entities
- [`data/TaskDao.kt`](app/src/main/java/com/supunhg/horus/data/TaskDao.kt) - Database access layer
- [`ui/theme/Theme.kt`](app/src/main/java/com/supunhg/horus/ui/theme/Theme.kt) - Material 3 theming

## License

This project is **MIT licensed**. Free to use, modify, and distribute.
