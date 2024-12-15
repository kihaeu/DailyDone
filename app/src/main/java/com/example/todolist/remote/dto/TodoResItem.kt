package com.example.todolist.remote.dto

data class TodoResItem(
    val deadline: String,
    val description: String,
    val id: Long,
    val priority: String,
    val progress: String,
    val title: String
)