package com.example.todolist.ui.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todolist.remote.api.RetrofitInstance
import com.example.todolist.remote.api.UserApiInterface
import com.example.todolist.remote.dto.JwtRes
import com.example.todolist.remote.dto.UserPatchPassword
import com.example.todolist.remote.dto.UserPost
import com.example.todolist.ui.theme.Gray40
import com.example.todolist.ui.theme.ToDoListTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    ChangePasswordContent(navController = navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordContent(navController: NavController) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("비밀번호 변경") },
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
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Divider(color = Gray40, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                Text("현재 비밀번호", fontSize = 16.sp, color = Color.Black)
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    placeholder = { Text("현재 비밀번호 입력", color = Gray40, fontSize = 14.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color(0xFFF6F6F6),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("새 비밀번호", fontSize = 16.sp, color = Color.Black)
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = { Text("비밀번호", color = Gray40, fontSize = 14.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color(0xFFF6F6F6),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("비밀번호 확인", color = Gray40, fontSize = 14.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color(0xFFF6F6F6),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "영문 대문자와 소문자, 숫자, 특수문자 중 2가지 이상을 조합하여 6-20자로 입력해주세요.",
                    fontSize = 12.sp,
                    color = Color(0xFFE27676),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(100.dp))

                Button(
                    onClick = {
                        when {
                            !validatePassword(newPassword) -> {
                                Toast.makeText(context, "올바른 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                            }

                            newPassword != confirmPassword -> {
                                Toast.makeText(context, "비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            currentPassword == newPassword -> {
                                Toast.makeText(context, "현재 비밀번호가 새 비밀번호와 같습니다.", Toast.LENGTH_SHORT).show()
                            }

                            else -> {
                                val userPatchPassword = UserPatchPassword(
                                    existingPassword = currentPassword,
                                    newPassword = newPassword
                                )
                                val retrofit = RetrofitInstance.getInstance(context)
                                val userApi = retrofit.create(UserApiInterface::class.java)

                                userApi.modifyPassword(userPatchPassword)
                                    .enqueue(object : Callback<Int> {
                                        override fun onResponse(
                                            call: Call<Int>,
                                            response: Response<Int>
                                        ) {
                                            if (response.isSuccessful) {
                                                Toast.makeText(
                                                    context,
                                                    "비밀번호 변경 완료",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.popBackStack()
                                            } else {
                                                if (response.code() == 401) {
                                                    Toast.makeText(
                                                        context,
                                                        "기존 비밀번호가 일치하지 않습니다.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                else if(response.code() == 400){
                                                    Toast.makeText(
                                                        context,
                                                        "기존 비밀번호가 일치하지 않습니다.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                else {
                                                    Toast.makeText(
                                                        context,
                                                        "비밀번호 변경 실패: ${response.message()}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.e("PasswordFail", response.toString())
                                                }
                                            }
                                        }

                                        override fun onFailure(call: Call<Int>, t: Throwable) {
                                            Toast.makeText(
                                                context,
                                                "비밀번호 변경 오류: ${t.message}",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    })
                            }

                        }
                    },
                    enabled = currentPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()) Color.Black else Color(0xFFEFEFEF)
                    ),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("변경하기", color = Color(0xFFA3A3A3))
                }
            }
        }
    )
}
