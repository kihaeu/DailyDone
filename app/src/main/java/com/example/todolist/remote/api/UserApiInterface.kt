package com.example.todolist.remote.api

import com.example.todolist.remote.dto.JwtRes
import com.example.todolist.remote.dto.TodoPost
import com.example.todolist.remote.dto.TodoPut
import com.example.todolist.remote.dto.TodoRes
import com.example.todolist.remote.dto.UserLoginPost
import com.example.todolist.remote.dto.UserPatchNickname
import com.example.todolist.remote.dto.UserPatchPassword
import com.example.todolist.remote.dto.UserPost
import com.example.todolist.remote.dto.UserRes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiInterface {
    @GET("/api/users")
    fun searchUser(
    ): Call<UserRes>

    @POST("/api/users")
    fun createUser(
        @Body userPost: UserPost
    ): Call<JwtRes>

    @DELETE("/api/users")
    fun deleteUser(
    ): Call<Unit>

    @POST("/api/users/login")
    fun loginUser(
        @Body userLoginPost: UserLoginPost
    ): Call<JwtRes>

    @GET("/api/users/admin")
    fun getAllUserInfo(
    ): Call<UserRes>

    @POST("/api/users/admin")
    fun loginAdminUser(
    ): Call<JwtRes>

    @DELETE("/api/users/admin")
    fun deleteAllUser(
    ): Call<Unit>

    @PATCH("/api/users/password")
    fun modifyPassword(
        @Body userPatchPassword: UserPatchPassword
    ): Call<Int>

    @PATCH("/api/users/nickname")
    fun modifyNickname(
        @Body userPatchNickname: UserPatchNickname
    ): Call<Int>

    @GET("/api/users/check-email")
    fun testEmail(
        @Query("email") email: String
    ): Call<String>

    @PUT("/api/todos/{todoId}")
    fun modifyToDo(
        @Path("todoId") todoId: Long,
        @Body todoPut: TodoPut
    ): Call<Long>

    @DELETE("/api/todos/{todoId}")
    fun deleteToDo(
        @Path("todoId") todoId: Long
    ): Call<Unit>

    @GET("/api/todos")
    fun getToDoList(
        @Query("priority") priority: String
    ): Call<TodoRes>

    @POST("/api/todos")
    fun createToDo(
        @Body todoPost: TodoPost
    ): Call<Long>

    @PATCH("/api/todos/{todoId}/progress")
    fun modifyToDoProgress(
        @Path("todoId") todoId: Long
    ): Call<Long>
}