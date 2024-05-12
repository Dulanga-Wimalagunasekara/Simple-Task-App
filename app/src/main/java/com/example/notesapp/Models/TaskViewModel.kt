package com.example.notesapp.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.Database.TaskDatabase
import com.example.notesapp.Database.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository

    val allnotes: LiveData<List<Task>>

    init {
        val dao = TaskDatabase.getDatabase(application).getNoteDao()
        repository = TaskRepository(dao)
        allnotes = repository.allNotes
    }

    fun deleteNote(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(task)
    }

    fun insertNote(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }

    fun updateNote(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(task)
    }
}