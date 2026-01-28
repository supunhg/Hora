package com.supunhg.horus.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.supunhg.horus.TaskStatus

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val status: TaskStatus,
    val dateId: Long, // Reference to TaskDateEntity
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "task_dates")
data class TaskDateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long, // Timestamp for the date (start of day)
    val customTitle: String? = null, // Optional custom title for the day
    val isToday: Boolean = false
)
