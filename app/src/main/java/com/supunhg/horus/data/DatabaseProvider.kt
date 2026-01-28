package com.supunhg.horus.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: TaskDatabase? = null
    
    fun getDatabase(context: Context): TaskDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "horus_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
