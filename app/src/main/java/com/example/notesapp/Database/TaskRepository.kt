package com.example.notesapp.Database

import androidx.lifecycle.LiveData
import com.example.notesapp.Models.Task

class TaskRepository(private val taskDAO: TaskDAO) {
    val allNotes: LiveData<List<Task>> = taskDAO.getAllNotes() // Update property to non-nullable LiveData

    suspend fun insert(task: Task) {
        taskDAO.insert(task)
    }

    suspend fun delete(task: Task) {
        taskDAO.delete(task)
    }

    suspend fun update(task: Task) {
        val rowsUpdated = task.id?.let { task.title?.let { it1 ->
            task.note?.let { it2 ->
                taskDAO.update(it,
                    it1, it2
                )
            }
        } }
        if (rowsUpdated == 0) {
            // Handle accordingly, maybe throw an exception or log a message
        }
    }
}