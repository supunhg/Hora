package com.supunhg.hora

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {
    var tasks by mutableStateOf(
        listOf(
            Task(1, "Buy Groceries", TaskStatus.PENDING),
            Task(2, "Study Kotlin", TaskStatus.PENDING),
            Task(3, "Workout", TaskStatus.PENDING),
        )
    )

        private set

    fun toggleTask(taskId: Int) {
        tasks = tasks.map {
            if (it.id == taskId) {
                it.copy(
                    status = if (it.status == TaskStatus.PENDING) TaskStatus.DONE else TaskStatus.PENDING
                )
            } else it
        }
    }

    fun dropTask(taskId: Int) {
        tasks = tasks.map {
            if (it.id == taskId) it.copy(status = TaskStatus.DROPPED) else it
        }
    }

    fun deleteTask(taskId: Int) {
        tasks = tasks.filter { it.id != taskId}
    }
}
