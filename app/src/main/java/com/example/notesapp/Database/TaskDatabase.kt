package com.example.notesapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.Models.Task
import com.example.notesapp.Utilities.DB_NAME

@Database(entities = arrayOf(Task::class), version=1, exportSchema = false)
abstract class TaskDatabase:RoomDatabase() {

    abstract fun getNoteDao(): TaskDAO

    companion object{

        @Volatile
        private var INSTANCE: TaskDatabase?=null

        fun getDatabase(context: Context):TaskDatabase{

            return INSTANCE?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    DB_NAME
                ).build()

                INSTANCE = instance
                instance

            }

        }

    }

}