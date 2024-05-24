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
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") {
                            MainPage(navController = navController, name = "이준희", toDoViewModel = toDoViewModel)
                        }
                        composable(
                            route = "mypage?user_name={user_name}",
                            arguments = listOf(navArgument("user_name") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val userName = backStackEntry.arguments?.getString("user_name") ?: "Unknown"
                            MyPageContent(navController = navController, name = userName)
                        }
                        composable("additionpage") {
                            AdditionPageContent(
                                onSaveClick = { priority, task ->
                                    toDoViewModel.addTask(priority, task)
                                    navController.popBackStack()
                                },
                                onCancelClick = {
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
