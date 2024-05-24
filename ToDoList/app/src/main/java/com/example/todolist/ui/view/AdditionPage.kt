package com.example.todolist.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    var year by remember { mutableStateOf("2024") }
    var month by remember { mutableStateOf("5") }
    var day by remember { mutableStateOf("24") }
    val context = LocalContext.current

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
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            TextField(value = year, onValueChange = { year = it }, modifier = Modifier.weight(1.8f))
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "년", modifier = Modifier.weight(1f))
            TextField(
                value = month,
                onValueChange = { month = it },
                modifier = Modifier.weight(1.3f)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "월", modifier = Modifier.weight(1f))
            TextField(value = day, onValueChange = { day = it }, modifier = Modifier.weight(1.3f))
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "일", modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(140.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    if (task != "")
                        onSaveClick(selectedPriority, task)
                    else
                        Toast.makeText(context, "할 일을 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text("저장")
            }
            Button(
                onClick = onCancelClick, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("취소")
            }
        }
    }
}
