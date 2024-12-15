package com.example.todolist.ui.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.theme.Gray40
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.remote.api.RetrofitInstance
import com.example.todolist.remote.api.UserApiInterface
import com.example.todolist.remote.dto.JwtRes
import com.example.todolist.remote.dto.UserPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignUpPage : ComponentActivity() {
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
                    SignUpPageContent(
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
fun SignUpPageContent(
    navController: NavController,
    toDoViewModel: ToDoViewModel
) {
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var emailDomain by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var emailExpanded by remember { mutableStateOf(false) }
    var isNicknameValid by remember { mutableStateOf(false) }
    var emailCheckResult by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(toDoViewModel.isSignUpSuccessful) {
        if (toDoViewModel.isSignUpSuccessful.value) {
            navController.navigate("login")
        }
    }

    fun checkEmailAvailability() {
        val fullEmail = "${email}@${emailDomain}"
        val retrofit = RetrofitInstance.getInstance(context)
        val userApi = retrofit.create(UserApiInterface::class.java)

        userApi.testEmail(fullEmail).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("email check", response.toString())
                if (response.isSuccessful) {
                    emailCheckResult = "사용 가능한 이메일입니다."
                } else if (response.code() == 409) { // 409 Conflict 코드 처리
                    emailCheckResult = "이미 존재하는 이메일입니다."
                } else {
                    emailCheckResult = "이메일 확인 중 오류가 발생했습니다."
                    Log.d("checkEmailFail", response.toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                emailCheckResult = "이메일 확인 중 오류가 발생했습니다2."
                Log.e("fail email check", t.message.toString())
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = "    회원가입",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 20.sp
                )
            }
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.Clear, contentDescription = "Back to login")
            }
        }
        Divider(color = Gray40, thickness = 1.dp)
        Spacer(modifier = Modifier.height(24    .dp))

        // 아이디 입력
        Text(
            "닉네임",
            color = Color(0xFFA3A3A3),
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                placeholder = { Text("닉네임 입력", color = Color(0xFFCBCBCB), fontSize = 14.sp) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFFF6F6F6),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                trailingIcon = {
                    IconButton(onClick = { nickname = "" }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Clear id"
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))

            // 중복 확인 버튼
            Button(
                onClick = {
                    if (validateNickname(nickname)) {
                        isNicknameValid = true
                        // 중복 확인 로직 추가
                    } else {
                        isNicknameValid = false
                        Toast.makeText(context, "올바른 닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFEEFEFEF)),
                modifier = Modifier.defaultMinSize(minWidth = 80.dp),
                shape = RoundedCornerShape(5.dp),
            ) {
                Text("확인", color = Gray40)
            }
        }
        Text(
            "한글과 영어만 사용하여, 2~10자의 닉네임을 입력해주세요",
            color = Color(0xFFE27676),
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 이메일 입력
        Text(
            "이메일",
            color = Gray40,
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    toDoViewModel.updateEmail(it)
                },
                textStyle = TextStyle(fontSize = 14.sp),
                placeholder = { Text("이메일 입력", color = Color.LightGray, fontSize = 14.sp) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFFF6F6F6),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("@", color = Color(0xFFA3A3A3), fontSize = 15.sp)
            Spacer(modifier = Modifier.width(8.dp))
            ExposedDropdownMenuBox(
                expanded = emailExpanded,
                onExpandedChange = { emailExpanded = !emailExpanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = emailDomain,
                    onValueChange = {
                        emailDomain = it
                        toDoViewModel.updateEmailDomain(it) // 뷰모델 상태 업데이트
                    },
                    placeholder = { Text("선택", color = Color.LightGray, fontSize = 14.sp) },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = emailExpanded) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color(0xFFF6F6F6),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .menuAnchor() // 필요 시 추가
                        .clickable { emailExpanded = true } // 클릭 시 드롭다운 메뉴 열기
                )
                ExposedDropdownMenu(
                    expanded = emailExpanded,
                    onDismissRequest = { emailExpanded = false }
                ) {
                    listOf("naver.com", "daum.net", "gmail.com").forEach { domain ->
                        DropdownMenuItem(
                            text = { Text(domain, fontSize = 16.sp) },
                            onClick = {
                                emailDomain = domain
                                emailExpanded = false
                                checkEmailAvailability()
                            }
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = emailCheckResult,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 비밀번호 입력
        Text(
            "비밀번호",
            color = Color(0xFFA3A3A3),
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("비밀번호", color = Color.LightGray, fontSize = 14.sp) },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFF6F6F6),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("비밀번호 확인", color = Color.LightGray, fontSize = 14.sp) },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color(0xFFF6F6F6),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "영문 대문자와 소문자, 숫자, 특수문자 중 2가지 이상을 조합하여 6~20자로 입력해주세요.",
            color = Color(0xFFE27676),
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                when {
                    !validatePassword(password) -> {
                        Toast.makeText(context, "올바른 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                    }

                    password != confirmPassword -> {
                        Toast.makeText(context, "비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
                    }

                    isNicknameValid -> {
                        val userPost = UserPost(
                            email = "$email@$emailDomain",
                            password = password,
                            nickname = nickname
                        )
                        val retrofit = RetrofitInstance.getInstance(context)
                        val userApi = retrofit.create(UserApiInterface::class.java)

                        userApi.createUser(userPost).enqueue(object : Callback<JwtRes> {
                            override fun onResponse(
                                call: Call<JwtRes>,
                                response: Response<JwtRes>
                            ) {
                                if (response.isSuccessful) {
                                    val sharedPreferences = context.getSharedPreferences(
                                        "MyAppPrefs",
                                        Context.MODE_PRIVATE
                                    )
                                    val editor = sharedPreferences.edit()
                                    editor.putString("NICKNAME_KEY", nickname) // 닉네임 저장
                                    editor.apply()
                                    Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show()
                                    navController.navigate("login")
                                } else {
                                    Toast.makeText(
                                        context,
                                        "회원가입 실패: ${response.message()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<JwtRes>, t: Throwable) {
                                Toast.makeText(context, "회원가입 오류: ${t.message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                    }

                    else -> {
                        Toast.makeText(context, "닉네임을 다시 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (true) Color.Black else Color(0xFFEFEFEF)
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("가입하기", color = Color(0xFFA3A3A3))
        }
    }
}

fun validateNickname(nickname: String): Boolean {
    val pattern = Pattern.compile("^[a-zA-Z가-힣]{2,10}$")
    return pattern.matcher(nickname).matches()
}

fun validatePassword(password: String): Boolean {
    val pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9!@#\$%^&*]).{6,20}$")
    return pattern.matcher(password).matches()
}
