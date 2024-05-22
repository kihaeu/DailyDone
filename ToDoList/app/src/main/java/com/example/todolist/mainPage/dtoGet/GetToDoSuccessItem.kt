package com.example.todolist.mainPage.dtoGet

data class GetToDoSuccessItem(
    val dateFromDelete: Int,
    val deadline: String,
    val deleted: Boolean,
    val description: String,
    val folderId: Int,
    val folderName: String,
    val id: Int,
    val priority: String,
    val progress: String,
    val title: String
)