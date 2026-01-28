package com.supunhg.horus

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.supunhg.horus.data.DatabaseProvider
import com.supunhg.horus.data.TaskEntity
import com.supunhg.horus.data.TaskDateEntity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val database = DatabaseProvider.getDatabase(application)
    private val taskDao = database.taskDao()
    private val dateDao = database.taskDateDao()
    
    var tasks = mutableStateListOf<Task>()
    var currentDateId by mutableStateOf<Long?>(null)
    var currentDateTitle by mutableStateOf("Today")
    var allDates = mutableStateListOf<TaskDateEntity>()
    var showHistory by mutableStateOf(false)
    
    init {
        initializeToday()
        loadAllDates()
    }
    
    private fun initializeToday() {
        viewModelScope.launch {
            val today = dateDao.getTodayDate()
            if (today == null) {
                // Create today's entry
                val todayStart = getTodayStartTimestamp()
                val newDateId = dateDao.insertDate(
                    TaskDateEntity(
                        date = todayStart,
                        isToday = true
                    )
                )
                currentDateId = newDateId
                loadTasksForDate(newDateId)
            } else {
                // Check if the stored "today" is actually today
                val todayStart = getTodayStartTimestamp()
                if (today.date != todayStart) {
                    // It's a new day, archive old today and create new
                    dateDao.clearAllTodayFlags()
                    val newDateId = dateDao.insertDate(
                        TaskDateEntity(
                            date = todayStart,
                            isToday = true
                        )
                    )
                    currentDateId = newDateId
                    loadTasksForDate(newDateId)
                } else {
                    currentDateId = today.id
                    currentDateTitle = today.customTitle ?: "Today"
                    loadTasksForDate(today.id)
                }
            }
        }
    }
    
    private fun loadTasksForDate(dateId: Long) {
        viewModelScope.launch {
            taskDao.getTasksForDate(dateId).collect { taskEntities ->
                tasks.clear()
                tasks.addAll(taskEntities.map { it.toTask() })
            }
        }
    }
    
    private fun loadAllDates() {
        viewModelScope.launch {
            dateDao.getAllDates().collect { dates ->
                allDates.clear()
                allDates.addAll(dates)
            }
        }
    }
    
    fun toggleTask(taskId: Int) {
        viewModelScope.launch {
            val taskEntity = taskDao.getTaskById(taskId)
            taskEntity?.let {
                val newStatus = if (it.status == TaskStatus.PENDING) 
                    TaskStatus.DONE else TaskStatus.PENDING
                taskDao.updateTask(it.copy(status = newStatus, updatedAt = System.currentTimeMillis()))
            }
        }
    }

    fun dropTask(taskId: Int) {
        viewModelScope.launch {
            val taskEntity = taskDao.getTaskById(taskId)
            taskEntity?.let {
                taskDao.updateTask(it.copy(status = TaskStatus.DROPPED, updatedAt = System.currentTimeMillis()))
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            taskDao.deleteTaskById(taskId)
        }
    }

    fun addTask(title: String) {
        currentDateId?.let { dateId ->
            viewModelScope.launch {
                taskDao.insertTask(
                    TaskEntity(
                        title = title,
                        status = TaskStatus.PENDING,
                        dateId = dateId
                    )
                )
            }
        }
    }
    
    fun updateDateTitle(newTitle: String) {
        currentDateId?.let { dateId ->
            viewModelScope.launch {
                val date = dateDao.getDateById(dateId)
                date?.let {
                    dateDao.updateDate(it.copy(customTitle = newTitle))
                    currentDateTitle = newTitle
                }
            }
        }
    }
    
    fun loadHistoryDate(dateId: Long) {
        viewModelScope.launch {
            val date = dateDao.getDateById(dateId)
            date?.let {
                currentDateId = dateId
                currentDateTitle = it.customTitle ?: formatDate(it.date)
                loadTasksForDate(dateId)
                showHistory = false
            }
        }
    }
    
    fun backToToday() {
        viewModelScope.launch {
            val today = dateDao.getTodayDate()
            today?.let {
                currentDateId = it.id
                currentDateTitle = it.customTitle ?: "Today"
                loadTasksForDate(it.id)
            }
        }
    }
    
    fun deleteHistoryDate(dateId: Long) {
        viewModelScope.launch {
            // Delete all tasks for this date first
            val tasksToDelete = taskDao.getTasksForDate(dateId).firstOrNull() ?: emptyList()
            tasksToDelete.forEach { taskDao.deleteTask(it) }
            
            // Delete the date entry
            val date = dateDao.getDateById(dateId)
            date?.let {
                dateDao.deleteDate(it)
            }
        }
    }
    
    private fun getTodayStartTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    private fun formatDate(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, java.util.Locale.getDefault())
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR)
        return "$month $day, $year"
    }
}

// Extension function to convert entity to UI model
private fun TaskEntity.toTask() = Task(
    id = id,
    title = title,
    status = status
)


