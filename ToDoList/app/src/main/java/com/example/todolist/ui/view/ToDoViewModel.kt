package com.example.todolist.ui.view

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    val priorityHigh = mutableStateListOf<Task>()
    val priorityMedium = mutableStateListOf<Task>()
    val priorityLow = mutableStateListOf<Task>()
    val countList = mutableStateOf(0) // Incompleted tasks count

    init {
        loadTasks()
    }

    fun addTask(priority: String, task: String) {
        viewModelScope.launch {
            when (priority) {
                "상" -> priorityHigh.add(Task(priorityHigh.size + 1, task))
                "중" -> priorityMedium.add(Task(priorityMedium.size + 1, task))
                "하" -> priorityLow.add(Task(priorityLow.size + 1, task))
            }
            countList.value++
            saveTasks()
        }
    }

    private fun saveTasks() {
        editor.putStringSet("priorityHigh", priorityHigh.map { it.description }.toSet())
        editor.putStringSet("priorityMedium", priorityMedium.map { it.description }.toSet())
        editor.putStringSet("priorityLow", priorityLow.map { it.description }.toSet())
        editor.putInt("countList", countList.value)
        editor.apply()
    }

    private fun loadTasks() {
        val highTasks = sharedPreferences.getStringSet("priorityHigh", emptySet())?.mapIndexed { index, description -> Task(index + 1, description) } ?: emptyList()
        val mediumTasks = sharedPreferences.getStringSet("priorityMedium", emptySet())?.mapIndexed { index, description -> Task(index + 1, description) } ?: emptyList()
        val lowTasks = sharedPreferences.getStringSet("priorityLow", emptySet())?.mapIndexed { index, description -> Task(index + 1, description) } ?: emptyList()
        priorityHigh.addAll(highTasks)
        priorityMedium.addAll(mediumTasks)
        priorityLow.addAll(lowTasks)
        countList.value = sharedPreferences.getInt("countList", 0)
    }
}
