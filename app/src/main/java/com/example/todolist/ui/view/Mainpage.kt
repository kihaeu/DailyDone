package com.example.todolist.ui.view

import android.content.Context
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    toDoViewModel: ToDoViewModel = viewModel()
) {
    val localDate: LocalDate = LocalDate.now()
    var selectedPriority by remember { mutableStateOf("High") }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val savedNickname = sharedPreferences.getString("NICKNAME_KEY", "User")
    val nickname by toDoViewModel.nickname.collectAsState()
    val statusMessage by toDoViewModel.statusMessage.collectAsState()
    val todoList by toDoViewModel.todoList.collectAsState()


    // 날짜 형식 변경
    val dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy (EEE)", Locale.getDefault())
    val formattedDate = localDate.format(dateFormatter)

    LaunchedEffect(Unit) {
        toDoViewModel.loadTasks(priority = selectedPriority)
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
                                    modifier = Modifier.size(24.dp),
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
                        .padding(8.dp)
                ) {
                    // 프로필 섹션 추가
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .clickable {
                                navController.navigate("mypage")
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
                                text = nickname,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                            Text(
                                text = statusMessage,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFA3A3A3)
                            )
                        }
                    }
                    Divider(color = Color(0xffE1EEE6), thickness = 1.5.dp)
                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Scheduled",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )
                        Box(
                            contentAlignment = Alignment.BottomEnd,
                            modifier = Modifier
                                .padding(bottom = 30.dp)  // 적절한 패딩을 추가하여 텍스트를 아래로 배치
                        ) {
                            Text(
                                text = formattedDate,  // 변경된 날짜 형식 사용
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Right,
                                color = Color(0xFFA3A3A3)
                            )
                        }
                    }

                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "${todoList.count { it.progress == "TODO" }} Incompleted tasks",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                    // Priority Selector
                    Row(
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Spacer(modifier = Modifier.width(15.dp))
                        PriorityButton(
                            "High",
                            selectedPriority,
                        ) {
                            selectedPriority = it
                            toDoViewModel.loadTasks(priority = "High")
                        }
                        PriorityButton(
                            "Medium",
                            selectedPriority,
                        ) {
                            selectedPriority = it
                            toDoViewModel.loadTasks(priority = "Medium")

                        }
                        PriorityButton(
                            "Low",
                            selectedPriority,
                        ) {
                            selectedPriority = it
                            toDoViewModel.loadTasks(priority = "Low")

                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(8.dp))  // 전체 리스트에 그림자 효과 추가
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {

                        LazyColumn {
                            items(todoList) { todo ->
                                TaskRow(
                                    todo,
                                    toggleTodo = { toDoViewModel.toggleTaskCompletion(todo) },
                                    onClickTodo = {
                                        toDoViewModel.setSelectedTodo(todo = it)
                                        navController.navigate("modifydelete"){
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )

                                if (todoList.last() != todo) {
                                    Divider(
                                        color = Color(0xffE1EEE6),
                                        thickness = 1.5.dp
                                    )  // 항목 간의 수평선 추가
                                }
                            }
                        }
                    }
                }
            }

        }
    }

}


