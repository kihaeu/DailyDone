package com.example.todolist.ui.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todolist.R
import com.example.todolist.remote.api.RetrofitInstance
import com.example.todolist.remote.api.UserApiInterface
import com.example.todolist.remote.dto.JwtRes
import com.example.todolist.remote.dto.UserLoginPost
import com.example.todolist.remote.dto.UserRes
import com.example.todolist.ui.theme.Gray40
import com.example.todolist.ui.theme.ToDoListTheme

class LoginPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()
                    val toDoViewModel: ToDoViewModel = viewModel()
                    LoginPageContent(
                        navController = navController,
                        toDoViewModel = toDoViewModel
                    )
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPageContent(navController: NavController, toDoViewModel: ToDoViewModel) {
    var email by remember { mutableStateOf("sk5514224@naver.com") }
    var password by remember { mutableStateOf("Qwer1234!") }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    var nickname by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        nickname = sharedPreferences.getString("NICKNAME_KEY", "") ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(text = "안녕하세요", fontWeight = FontWeight.Bold, fontSize = 26.sp)
        Text(text = "TaskBuddy입니다.", fontWeight = FontWeight.Bold, fontSize = 26.sp)
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "할 일을 우선순위별로 정리하고,\n해야할 일을 하나씩 달성해보세요!",
            fontSize = 14.sp,
            color = Color(0xFFA3A3A3)
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFEFEFEF),
                focusedBorderColor = Color(0xFFEFEFEF),
                // 클릭 했을 때 색깔 바꿀건가요?
                unfocusedLabelColor = Color(0xFFCBCBCB),
                focusedLabelColor = Color(0xFFCBCBCB)
            ),
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("현재 비밀번호 입력", color = Gray40, fontSize = 14.sp) },
            textStyle = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color(0xFFEFEFEF),
                focusedBorderColor = Color(0xFFEFEFEF),
                unfocusedLabelColor = Color(0xFFCBCBCB),
                focusedLabelColor = Color(0xFFCBCBCB)
            ),
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("비밀번호 입력", color = Gray40, fontSize = 14.sp) },
            visualTransformation = PasswordVisualTransformation(),
            textStyle = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    val userLoginPost = UserLoginPost(email, password)
                    val retrofit = RetrofitInstance.getInstance(context)
                    val userApi = retrofit.create(UserApiInterface::class.java)

                    userApi.loginUser(userLoginPost).enqueue(object : retrofit2.Callback<JwtRes> {
                        override fun onResponse(
                            call: retrofit2.Call<JwtRes>,
                            response: retrofit2.Response<JwtRes>
                        ) {
                            if (response.isSuccessful) {
                                val token = response.body()?.accessToken ?: ""
                                val editor = sharedPreferences.edit()
                                editor.putString("TOKEN_KEY", token)
                                editor.putString("NICKNAME_KEY", nickname)  // 수정된 부분: 닉네임 저장
                                editor.apply()
                                toDoViewModel.loadUserInfo()
                                navController.navigate("main")
                            } else {
                                Toast.makeText(
                                    context,
                                    "로그인 실패: ${response.message()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<JwtRes>, t: Throwable) {
                            Log.e("login error", t.message.toString())
                            Toast.makeText(context, "로그인 오류: ${t.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                } else {
                    Toast.makeText(context, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(61.dp)
        ) {
            Text("로그인", color = Color.White)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("아이디 찾기", color = Color.Black, fontSize = 16.sp)



            Text("비밀번호 찾기", color = Color.Black, fontSize = 16.sp)



            Text(
                "회원가입",
                color = Color.Black,
                modifier = Modifier.clickable { navController.navigate("signup") },
                fontSize = 16.sp)
        }
    }
}

