package com.example.todolist.remote.dto

data class UserPatchPassword(
    val existingPassword: String,
    val newPassword: String
)