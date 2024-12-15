package com.example.todolist.remote.dto

data class ErrorResponse(
    val code: String,
    val error: String,
    val message: List<String>,
    val status: Int,
    val timestamp: String
)