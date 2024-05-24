package com.example.todolist.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

data class Task(val id: Int, val description: String, var isCompleted: MutableState<Boolean> = mutableStateOf(false))


class PriorityHigh {
    private val tasks = mutableListOf<Task>()

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun getTasks(): List<Task> {
        return tasks
    }
}

class PriorityMedium {
    private val tasks = mutableListOf<Task>()

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun getTasks(): List<Task> {
        return tasks
    }
}

class PriorityLow {
    private val tasks = mutableListOf<Task>()

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun getTasks(): List<Task> {
        return tasks
    }
}

@Composable
fun TaskRow(task: Task) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted.value,
            onCheckedChange = { task.isCompleted.value = it }
        )
        Text(
            color = Color.Black,
            text = task.description
        )
    }
}

@Composable
fun PriorityButton(
    priority: String,
    selectedPriority: String,
    modifier: Modifier = Modifier,
    onPrioritySelected: (String) -> Unit
) {
    Button(
        onClick = { onPrioritySelected(priority) },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            containerColor = if (priority == selectedPriority) MaterialTheme.colorScheme.primary else Color.White,
        )
    ) {
        Text(priority)
    }
}




