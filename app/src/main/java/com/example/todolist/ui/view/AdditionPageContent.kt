package com.example.todolist.ui.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todolist.remote.dto.TodoResItem
import com.example.todolist.ui.theme.Gray40


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditionPageContent(
    navController: NavController,
    onSaveClick: (description: String, title: String, selectedPriority: String, year: String, month: String, day: String, progress: String) -> Unit,
){



    var description by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("2024") }
    var month by remember { mutableStateOf("0") }
    var day by remember { mutableStateOf("0") }
    var progress by remember { mutableStateOf("TODO") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Column {

                    BasicTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        decorationBox = { innerTextField ->
                            if (title.isEmpty()) {
                                Text(
                                    "Write task here",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xffA3A3A3)
                                )
                            }
                            innerTextField()
                        },
                    )
                    Divider(color = Color(0xffE1EEE6), thickness = 1.5.dp)
                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        verticalAlignment = Alignment.Top,
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "내용",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        BasicTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(
                                    color = Color(0xFFF6F6F6),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(8.dp),
                            textStyle = TextStyle(
                                fontSize = 13.sp,
                                color = Color.Black
                            ),
                            decorationBox = { innerTextField ->
                                if (description.isEmpty()) {
                                    Text(
                                        "자세한 설명을 기재하기",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xffA3A3A3)
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "마감 날짜",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 15.sp
                        )
                        Box{
                            BasicTextField(
                                value = year,
                                onValueChange = { year = it },
                                modifier = Modifier
                                    .size(60.dp, 40.dp)
                                    .padding(8.dp)
                                    .background(Color(0xFFF6F6F6)),
                                textStyle = MaterialTheme.typography.bodySmall,
                                decorationBox = { innerTextField ->
                                    if (year.isEmpty()) {
                                        Text(
                                            "2024",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xffA3A3A3)
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }

                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "년",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Box {

                            BasicTextField(
                                value = month,
                                onValueChange = { month = it },
                                modifier = Modifier
                                    .size(40.dp, 40.dp)
                                    .padding(8.dp)
                                    .background(Color(0xFFF6F6F6)),
                                textStyle = MaterialTheme.typography.bodySmall,
                                decorationBox = { innerTextField ->
                                    if (month.isEmpty()) {
                                        Text(
                                            "0",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xffA3A3A3)
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "월",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Box {

                            BasicTextField(
                                value = day,
                                onValueChange = { day = it },
                                modifier = Modifier
                                    .size(40.dp, 40.dp)
                                    .padding(8.dp)
                                    .background(Color(0xFFF6F6F6)),
                                textStyle = MaterialTheme.typography.bodySmall,
                                decorationBox = { innerTextField ->
                                    if (day.isEmpty()) {
                                        Text(
                                            "0",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xffA3A3A3)
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "일",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "우선 순위",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 15.sp
                        )
                        PriorityButton("High", selectedPriority, modifier= Modifier.weight(1f), contentPaddingValues = PaddingValues(0.dp)) { selectedPriority = it }
                        PriorityButton("Medium", selectedPriority, modifier= Modifier.weight(1f), contentPaddingValues = PaddingValues(0.dp)) { selectedPriority = it }
                        PriorityButton("Low", selectedPriority, modifier= Modifier.weight(1f), contentPaddingValues = PaddingValues(0.dp)) { selectedPriority = it }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "진행 상태",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 15.sp
                        )
                        Checkbox(
                            checked = progress == "DONE",
                            onCheckedChange = {
                                progress = if (progress == "DONE"){
                                    "TODO"
                                } else {
                                    "DONE"
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xff51D184),
                                uncheckedColor = Color(0xffA3A3A3)
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {navController.popBackStack()},
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Gray40),
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text("취소하기", color = Color(0xffA3A3A3))
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff51D184)),
                            onClick = {
                                if (title.isNotEmpty())
                                    onSaveClick(
                                        description, title, selectedPriority, year, month, day, progress
                                    )
                                else
                                    Toast.makeText(context, "할 일을 입력해주세요", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text("저장하기")
                        }
                    }
                }

            }
        }
    )
}
