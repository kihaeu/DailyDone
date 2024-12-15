package com.example.todolist.ui.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import com.example.todolist.ui.theme.ToDoListTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.remote.dto.TodoPut

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val toDoViewModel: ToDoViewModel = viewModel()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("modifydelete") {
                            ModifyDeleteContent(
                                navController = navController,
                                onSaveClick = { selectedTodo, description, title, selectedPriority, year, month, day, progress ->
                                    toDoViewModel.updateTodo(
                                        todo = selectedTodo,
                                        description = description,
                                        title = title,
                                        progress = progress,
                                        year = year,
                                        month = month,
                                        day = day,
                                        priority = selectedPriority
                                    )
                                    navController.popBackStack()
                                },
                                onDeleteClick = {selectedTodo ->
                                    // 삭제 로직 추가
                                    toDoViewModel.deleteTodo(
                                        selectedTodo = selectedTodo
                                    )
                                    navController.popBackStack()
                                },
                                toDoViewModel = toDoViewModel
                            )
                        }
                        composable("signup") {
                            SignUpPageContent(
                                navController = navController,
                                toDoViewModel = toDoViewModel
                            )
                        }
                        composable("changePW") {
                            ChangePasswordContent(navController = navController)
                        }
                        composable("login") {
                            LoginPageContent(
                                navController = navController,
                                toDoViewModel = toDoViewModel
                            )
                        }
                        composable("main") {
                            MainPage(navController = navController, toDoViewModel = toDoViewModel)
                        }
                        composable("mypage") { backStackEntry ->
                            MyPageContent(
                                navController = navController,
                                toDoViewModel = toDoViewModel
                            )
                        }
                        composable("additionpage") {
                            AdditionPageContent(
                                navController = navController,
                                onSaveClick = {description, title, selectedPriority, year, month, day, progress ->
                                    toDoViewModel.plusTask(
                                        priority = selectedPriority,
                                        title = title,
                                        description = description,
                                        year = year,
                                        month = month,
                                        day = day,
                                        progress = progress
                                    )
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


