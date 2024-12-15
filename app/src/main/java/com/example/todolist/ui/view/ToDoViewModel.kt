package com.example.todolist.ui.view

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.todolist.remote.api.RetrofitInstance
import com.example.todolist.remote.api.UserApiInterface
import com.example.todolist.remote.dto.TodoPost
import com.example.todolist.remote.dto.TodoPut
import com.example.todolist.remote.dto.TodoRes
import com.example.todolist.remote.dto.TodoResItem
import com.example.todolist.remote.dto.UserRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences =
        application.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    // 로그인페이지 변수
    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    private val _emailDomain = MutableStateFlow("")
    val emailDomain: StateFlow<String> = _emailDomain
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword
    private val _isSignUpSuccessful = MutableStateFlow(false)
    val isSignUpSuccessful: StateFlow<Boolean> = _isSignUpSuccessful
    private val _statusMessage = MutableStateFlow("Happy with schedule")
    val statusMessage: StateFlow<String> = _statusMessage

    // 투두리스트 변수
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description
    private val _deadline = MutableStateFlow("")
    val deadline: StateFlow<String> = _deadline
    private val _priority = MutableStateFlow("")
    val priority: StateFlow<String> = _priority
    private val _progress = MutableStateFlow("")
    val progress: StateFlow<String> = _progress


    // 우선순위, 할일 개수
    private val _todoList: MutableStateFlow<ArrayList<TodoResItem>> = MutableStateFlow(ArrayList())
    val todoList: StateFlow<ArrayList<TodoResItem>> = _todoList

    private val _selectedTodo: MutableStateFlow<TodoResItem> = MutableStateFlow(
        TodoResItem(
            deadline = "",
            description = "",
            id = -1,
            priority = "",
            progress = "",
            title = ""
        )
    )
    val selectedTodo: StateFlow<TodoResItem> = _selectedTodo

    init {
        loadUserInfo()
    }


    fun updateNickName(name: String) {
        _nickname.update { name }
    }

    fun updateStatusMessage(message: String) {
        _statusMessage.value = message
    }


    fun loadTasks(priority: String) {
        val retrofit = RetrofitInstance.getInstance(getApplication())
        val userApi = retrofit.create(UserApiInterface::class.java)


        userApi.getToDoList(priority = priority).enqueue(object : Callback<TodoRes> {
            override fun onResponse(call: Call<TodoRes>, response: Response<TodoRes>) {
                if (response.isSuccessful) {
                    val todoList = response.body()
                    Log.d("loadTasks", todoList.toString())
                    todoList?.let {
                        _todoList.update {
                            todoList
                        }
                    }
                } else {
                    Log.e("loadTasks", "Failed to loadTask: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<TodoRes>, t: Throwable) {
                Log.e("loadTasks", "Error loadTasks", t)
            }
        })
    }

    fun plusTask(
        priority: String,
        title: String,
        description: String,
        year: String,
        month: String,
        day: String,
        progress: String
    ) {
        val retrofit = RetrofitInstance.getInstance(getApplication())
        val userApi = retrofit.create(UserApiInterface::class.java)
        _deadline.update {
            String.format("%04d-%02d-%02dT23:59:00Z", year.toInt(), month.toInt(), day.toInt())
        }


        Log.d(
            "addTodoPostModel", TodoPost(
                deadline = deadline.value,
                description = description,
                priority = priority,
                progress = progress,
                title = title
            ).toString()
        )

        userApi.createToDo(
            TodoPost(
                deadline = deadline.value,
                description = description,
                priority = priority,
                progress = progress,
                title = title
            )
        ).enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val todoId = response.body()
                    Log.d("plusTasks", todoId.toString())
                    loadTasks(priority = priority)

                } else {
                    Log.e("plusTask", "Failed to plusTask: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                Log.e("plusTask", "Error plusTask", t)
            }
        })
    }

    fun loadUserInfo() {
        val retrofit = RetrofitInstance.getInstance(getApplication())
        val userApi = retrofit.create(UserApiInterface::class.java)

        userApi.searchUser().enqueue(object : Callback<UserRes> {
            override fun onResponse(call: Call<UserRes>, response: Response<UserRes>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    Log.d("loadUserInfo", user.toString())
                    user?.let {
                        updateNickName(it.nickname)
                    }
                } else {
                    Log.e(
                        "loadUserInfo",
                        "Failed to fetch user info: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<UserRes>, t: Throwable) {
                Log.e("loadUserInfo", "Error fetching user info", t)
            }
        })
    }

    fun toggleTaskCompletion(todo: TodoResItem) {

        val retrofit = RetrofitInstance.getInstance(getApplication())
        val userApi = retrofit.create(UserApiInterface::class.java)

        userApi.modifyToDoProgress(todoId = todo.id).enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val modifiedTodoId = response.body()
                    loadTasks(priority = todo.priority)
                } else {
                    Log.e(
                        "toggleTaskCompletion",
                        "Failed to toggleTaskCompletion: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                Log.e("toggleTaskCompletion", "Error toggleTaskCompletion", t)
            }
        })
    }


    fun deleteTodo(selectedTodo: TodoResItem) {

        val retrofit = RetrofitInstance.getInstance(getApplication())
        val userApi = retrofit.create(UserApiInterface::class.java)

        userApi.deleteToDo(todoId = selectedTodo.id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    loadTasks(priority = selectedTodo.priority)
                } else {
                    Log.e(
                        "deleteTodo",
                        "Failed to deleteTodo: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e("deleteTodo", "Error deleteTodo", t)
            }
        })
    }


    fun updateTodo(
        todo: TodoResItem,
        priority: String,
        title: String,
        description: String,
        year: String,
        month: String,
        day: String,
        progress: String
    ) {

        _deadline.update {
            String.format("%04d-%02d-%02dT23:59:00Z", year.toInt(), month.toInt(), day.toInt())
        }

        val retrofit = RetrofitInstance.getInstance(getApplication())
        val userApi = retrofit.create(UserApiInterface::class.java)

        userApi.modifyToDo(todoId = todo.id, todoPut = TodoPut(
            deadline = deadline.value,
            description = description,
            priority = priority,
            progress = progress,
            title = title
        )
        ).enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    val modifiedTodoId = response.body()
                    loadTasks(priority = todo.priority)
                } else {
                    Log.e(
                        "updateTodo",
                        "Failed to updateTodo: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                Log.e("updateTodo", "Error updateTodo", t)
            }
        })
    }


    fun setSelectedTodo(todo: TodoResItem) {
        _selectedTodo.update {
            todo
        }
    }

    fun clearSelectedTodo() {
        _selectedTodo.update {
            TodoResItem(
                deadline = "",
                description = "",
                id = -1,
                priority = "",
                progress = "",
                title = ""
            )
        }
    }

    fun updateEmail(newEmail: String) {
        _email.update { newEmail }
    }

    fun updateEmailDomain(newDomain: String) {
        _emailDomain.update { newDomain }
    }


}

