package com.example.todolist.remote.api

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
import retrofit2.http.Query

interface UserApiInterface {
    @POST("/api/user")
    fun createUser(
        @Body userPost: UserPost
    ): Call<Int>

    @GET("/api/user")
    fun searchUser(
        @Query("userId") userId : Int
    ): Call<UserRes>

    @DELETE("/api/user")
    fun deleteUser(
        @Query("userId") userId : Int
    ): Call<Unit>

    @PATCH("/api/user/password")
    fun modifyPassword(
        @Query("userId") userId : Int,
        @Body userPatchPassword: UserPatchPassword
    ): Call<Int>

    @PATCH("/api/user/nickname")
    fun modifyNickname(
        @Query("userId") userId : Int,
        @Body userPatchNickname: UserPatchNickname
    ): Call<Int>

}