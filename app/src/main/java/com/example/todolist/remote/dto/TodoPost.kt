package com.example.todolist.remote.dto

data class TodoPost(
    val deadline: String,
    val description: String,
    val priority: String,
    val progress: String,
    val title: String
)