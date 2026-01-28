# Horus - AI Coding Agent Instructions

## Project Overview
Horus is a personal productivity/task management Android app built with **Kotlin** and **Jetpack Compose**. The app uses **single-activity architecture** with `MainActivity` hosting all Compose content. All UI is built declaratively using Compose - no XML layouts.

**Package**: `com.supunhg.horus`  
**Min SDK**: 24 | **Target SDK**: 36 | **Java**: 11

## Architecture & Key Files

### MVVM with Room Database
The codebase uses MVVM architecture with Room for local persistence:
- **Data layer**: [`data/TaskEntity.kt`](app/src/main/java/com/supunhg/horus/data/TaskEntity.kt) - Room entities (`TaskEntity`, `TaskDateEntity`)
- **Data layer**: [`data/TaskDao.kt`](app/src/main/java/com/supunhg/horus/data/TaskDao.kt) - DAO interfaces and database definition
- **Data layer**: [`data/DatabaseProvider.kt`](app/src/main/java/com/supunhg/horus/data/DatabaseProvider.kt) - Singleton database instance
- **UI models**: Data classes (`Task`, `TaskStatus` enum) in [`MainActivity.kt`](app/src/main/java/com/supunhg/horus/MainActivity.kt)
- **UI composables**: All screens and components in `MainActivity.kt`
- **ViewModel**: [`TaskViewModel.kt`](app/src/main/java/com/supunhg/horus/TaskViewModel.kt) - Extends `AndroidViewModel` to access database

### State Management
- Use `mutableStateListOf()` in ViewModel for observable lists that trigger recomposition
- Room DAOs return `Flow<List<T>>` for reactive database queries
- ViewModel collects flows in `viewModelScope.launch`
- Local UI state uses `remember { mutableStateOf() }` (e.g., dialog visibility, input fields)
- ViewModels are obtained via `viewModel()` function from `androidx.lifecycle.viewmodel.compose`

### Task & Date Management
Tasks are organized by date sessions:
- Each day has a `TaskDateEntity` with optional custom title
- Tasks reference their date via `dateId` foreign key
- "Today" flag marks the active date session
- New days automatically archive previous "today" and create new session
- Users can browse history and edit any date's title

### Task Lifecycle Pattern
Tasks have three states (see `TaskStatus` enum):
- `PENDING` → `DONE` (click to toggle)
- `PENDING` → `DROPPED` (long-press dialog → "Drop")
- Any state → deleted (long-press dialog → "Delete")

## Build & Dependencies

### Build Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Install on connected device/emulator
./gradlew installDebug

# Sync dependencies
./gradlew build --refresh-dependencies
```

### Version Catalog (Gradle)
All dependencies use version catalog aliases from [`gradle/libs.versions.toml`](gradle/libs.versions.toml):
- Add new deps: Define in `[libraries]` section, reference via `libs.dependency.name`
- Example: `implementation(libs.androidx.room.ktx)`
- Room requires KSP plugin: `alias(libs.plugins.ksp)` in app build.gradle.kts

### Current Stack
- **Compose BOM**: `2024.09.00` (centralized Compose versioning)
- **Material3**: Primary design system
- **Kotlin**: `2.0.21` with Compose compiler plugin
- **Room**: `2.6.1` for local database (runtime, ktx, compiler)
- **KSP**: `2.0.21-1.0.28` for annotation processing

## Coding Conventions

### Composables
- **File organization**: All composables for a feature can live in `MainActivity.kt` until complexity warrants extraction
- **Modifier pattern**: Always accept `Modifier` parameter (default `Modifier`) as first param after required data
- **Preview annotations**: Use `@Preview` for design-time previews
- **ExperimentalFoundationApi**: Used for `combinedClickable` (long-press + click handling)

### UI Patterns
```kotlin
// Chat-style input (send button on right)
Row(verticalAlignment = Alignment.Bottom) {
    TextField(modifier = Modifier.weight(1f), ...)
    IconButton { Icon(Icons.Default.Send) }
}

// Task item with dual-interaction
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(task: Task, ...) {
    Surface(
        modifier = Modifier.combinedClickable(
            onClick = onToggle,
            onLongClick = { showDialog = true }
        )
    ) { ... }
}
```

### Database Patterns
```kotlin
// ViewModel database access
class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val database = DatabaseProvider.getDatabase(application)
    private val taskDao = database.taskDao()
    
    init {
        viewModelScope.launch {
            taskDao.getTasksForDate(dateId).collect { entities ->
                tasks.clear()
                tasks.addAll(entities.map { it.toTask() })
            }
        }
    }
    
    fun addTask(title: String) {
        viewModelScope.launch {
            taskDao.insertTask(TaskEntity(...))
        }
    }
}
```

### State Hoisting
Follow Compose unidirectional data flow:
- State flows **down** via parameters
- Events flow **up** via lambda callbacks
- Example: `ChatStyleInput` owns text field state but calls `onAddTask` callback

## Future Roadmap
When implementing features, align with planned additions:
- Google Calendar sync integration
- AI-assisted task creation (LLM integration)
- WhatsApp command parsing
- Advanced notification system

## Testing
- **Unit tests**: `app/src/test/` - use for ViewModel logic
- **Instrumented tests**: `app/src/androidTest/` - use for UI tests with Compose testing library
- Test files follow `Example*Test.kt` naming convention

## Theming
- **Theme file**: [`ui/theme/Theme.kt`](app/src/main/java/com/supunhg/horus/ui/theme/Theme.kt) with Material3 dynamic color support (Android 12+)
- Color schemes: `DarkColorScheme` and `LightColorScheme` with fallback to dynamic colors
- When adding UI: Prefer Material3 components (`Button`, `AlertDialog`, `TextField`) over custom implementations
- Design philosophy: Minimalist, spacious (24dp padding), peaceful colors, smooth interactions
