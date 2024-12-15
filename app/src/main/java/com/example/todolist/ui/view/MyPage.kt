package com.example.todolist.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.theme.Gray40
import com.example.todolist.ui.theme.ToDoListTheme

class MyPage : ComponentActivity() {
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
                    MyPageContent(navController = navController, toDoViewModel = toDoViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageContent(navController: NavController, toDoViewModel: ToDoViewModel) {
    var isEditingName by remember { mutableStateOf(false) }
    var isEditingStatus by remember { mutableStateOf(false) }
    var textFieldName by remember { mutableStateOf(toDoViewModel.nickname.value) }
    var textFieldStatus by remember { mutableStateOf(toDoViewModel.statusMessage.value) }

    LaunchedEffect(textFieldName) {
        toDoViewModel.updateNickName(textFieldName)
    }

    LaunchedEffect(textFieldStatus) {
        toDoViewModel.updateStatusMessage(textFieldStatus)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(128.dp)  // 아이콘 크기를 128dp로 설정
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )

                // 사용자 이름 텍스트 필드
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .padding(4.dp)
                        .border(1.dp, color = Color(0xFFEFEFEF), shape = RoundedCornerShape(6.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 50.dp) // Adjust padding as needed
                    ) {
                        if (isEditingName) {
                            TextField(
                                value = textFieldName,
                                onValueChange = { textFieldName = it },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.White
                                )
                            )

                        } else {
                            Text(
                                text = textFieldName,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    IconButton(onClick = { isEditingName = !isEditingName }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // 상태 메시지 텍스트 필드
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .padding(4.dp)
                        .border(1.dp, color = Color(0xFFEFEFEF), shape = RoundedCornerShape(6.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 50.dp) // Adjust padding as needed
                    ) {
                        if (isEditingStatus) {
                            TextField(
                                value = textFieldStatus,
                                onValueChange = { textFieldStatus = it },
                                textStyle = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.White,
                                    focusedTextColor = Gray40,
                                    unfocusedTextColor = Gray40)
                            )
                        } else {
                            Text(
                                text = textFieldStatus,
                                style = MaterialTheme.typography.bodySmall,
                                color = Gray40,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    IconButton(onClick = { isEditingStatus = !isEditingStatus }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                Divider(color = Color(0xffE1EEE6), thickness = 1.5.dp)
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "비밀번호 변경",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            navController.navigate("changePW")
                        }
                )
                Text(
                    text = "로그아웃",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            navController.navigate("login")
                        }
                )
            }
        }
    )
}