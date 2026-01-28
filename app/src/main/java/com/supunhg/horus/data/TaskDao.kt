package com.supunhg.horus.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE dateId = :dateId ORDER BY createdAt ASC")
    fun getTasksForDate(dateId: Long): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): TaskEntity?
    
    @Insert
    suspend fun insertTask(task: TaskEntity): Long
    
    @Update
    suspend fun updateTask(task: TaskEntity)
    
    @Delete
    suspend fun deleteTask(task: TaskEntity)
    
    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)
}

@Dao
interface TaskDateDao {
    @Query("SELECT * FROM task_dates ORDER BY date DESC")
    fun getAllDates(): Flow<List<TaskDateEntity>>
    
    @Query("SELECT * FROM task_dates WHERE isToday = 1 LIMIT 1")
    suspend fun getTodayDate(): TaskDateEntity?
    
    @Query("SELECT * FROM task_dates WHERE id = :dateId")
    suspend fun getDateById(dateId: Long): TaskDateEntity?
    
    @Insert
    suspend fun insertDate(date: TaskDateEntity): Long
    
    @Update
    suspend fun updateDate(date: TaskDateEntity)
    
    @Delete
    suspend fun deleteDate(date: TaskDateEntity)
    
    @Query("UPDATE task_dates SET isToday = 0")
    suspend fun clearAllTodayFlags()
    
    @Transaction
    suspend fun setNewToday(dateId: Long) {
        clearAllTodayFlags()
        val date = getDateById(dateId)
        date?.let {
            updateDate(it.copy(isToday = true))
        }
    }
}

@Database(
    entities = [TaskEntity::class, TaskDateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun taskDateDao(): TaskDateDao
}
