package com.example.todolist.ui.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import androidx.compose.foundation.clickable
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainPage(modifier: Modifier = Modifier, navController: NavController, name: String, toDoViewModel: ToDoViewModel = viewModel()) {
    val localDate: LocalDate = LocalDate.now()
    var selectedPriority by remember { mutableStateOf("중") }
    val countList by toDoViewModel.countList

    // 새로 작성한 투두에서 받은 할 일과 우선순위를 저장하는 변수들
    val newTaskPriority = navController.currentBackStackEntry?.savedStateHandle?.get<String>("PRIORITY")
    val newTask = navController.currentBackStackEntry?.savedStateHandle?.get<String>("TASK")

    LaunchedEffect(newTaskPriority, newTask) {
        if (newTaskPriority != null && newTask != null) {
            toDoViewModel.addTask(newTaskPriority, newTask)
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("PRIORITY")
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("TASK")
        }
    }

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        ),
                        title = {},
                        navigationIcon = {},
                        actions = {
                            IconButton(onClick = { navController.navigate("additionpage") }) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add To-Do",
                                    tint = Color.Black
                                )
                            }
                        }
                    )
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // 프로필 섹션 추가
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .clickable {
                                navController.navigate("mypage?user_name=${name}")
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(
                                text = name,
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Happy with schedule",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Today List",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = localDate.toString(),
                            textAlign = TextAlign.Right,
                            color = Color(0xFFA3A3A3)
                        )

                    }
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "${countList.toString()} Incompleted tasks",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    // Priority Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PriorityButton(
                            "상",
                            selectedPriority,
                            Modifier.weight(1f)
                        ) { selectedPriority = it }
                        PriorityButton(
                            "중",
                            selectedPriority,
                            Modifier.weight(1f)
                        ) { selectedPriority = it }
                        PriorityButton(
                            "하",
                            selectedPriority,
                            Modifier.weight(1f)
                        ) { selectedPriority = it }
                    }

                    LazyColumn {
                        when (selectedPriority) {
                            "상" -> items(toDoViewModel.priorityHigh) { task ->
                                TaskRow(task)
                            }

                            "중" -> items(toDoViewModel.priorityMedium) { task ->
                                TaskRow(task)
                            }

                            "하" -> items(toDoViewModel.priorityLow) { task ->
                                TaskRow(task)
                            }
                        }

                    }
                }
            }

        }
    }

}
