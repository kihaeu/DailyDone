package com.example.todolist.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolist.ui.theme.ToDoListTheme

class AdditionPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdditionPageContent(
                        onSaveClick = { priority, task ->
                            // 여기서 데이터를 저장하고 메인 페이지로 돌아갑니다.
                            setResult(
                                RESULT_OK,
                                intent.putExtra("PRIORITY", priority).putExtra("TASK", task)
                            )
                            finish()
                        },
                        onCancelClick = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun AdditionPageContent(onSaveClick: (String, String) -> Unit, onCancelClick: () -> Unit) {
    var task by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf("상") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "To-Do 작성", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PriorityButton("상", selectedPriority) { selectedPriority = it }
            PriorityButton("중", selectedPriority) { selectedPriority = it }
            PriorityButton("하", selectedPriority) { selectedPriority = it }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = task,
            onValueChange = { task = it },
            label = { Text("할 일 작성") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = { onSaveClick(selectedPriority, task) }) {
                Text("저장")
            }
            Button(onClick = onCancelClick, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )) {
                Text("취소")
            }
        }
    }
}
