package com.example.todolist.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.todolist.remote.dto.TodoResItem
import com.example.todolist.ui.theme.Gray40
import com.example.todolist.ui.theme.Green40

data class Task(
    val id: Int,
    val description: String,
    var isCompleted: MutableState<Boolean> = mutableStateOf(false)
)


@Composable         // 메인페이지 체크박스
fun TaskRow(
    todo: TodoResItem,
    toggleTodo: (TodoResItem) -> Unit,
    onClickTodo: (TodoResItem) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                onClickTodo(todo)
            }
    ) {
        Checkbox(
            checked = (todo.progress == "DONE"),
            onCheckedChange = {
                toggleTodo(todo)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xff51D184),             // 체크되었을 때의 색상
                uncheckedColor = Color(0xffA3A3A3),           // 체크되지 않았을 때의 색상
                checkmarkColor = Color.White                        // 체크마크의 색상
            )
        )
        Text(
            color = Color.Black,
            text = todo.title,
            style = MaterialTheme.typography.labelSmall, // 글꼴 스타일 적용
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}


@Composable
fun PriorityButton(
    priority: String,
    selectedPriority: String,
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(horizontal = 15.dp),
    onPrioritySelected: (String) -> Unit,
) {
    Button(
        onClick = { onPrioritySelected(priority) },
        modifier = modifier
            .padding(6.dp),
        border = BorderStroke(1.dp, Green40),
        shape = RoundedCornerShape(33.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = if (priority == selectedPriority) Color.White else Gray40,
            containerColor = if (priority == selectedPriority) Green40 else Color.White
        ),
        contentPadding = contentPaddingValues
    ) {
        Text(
            text = priority,
            style = MaterialTheme.typography.bodySmall
        )
    }
}