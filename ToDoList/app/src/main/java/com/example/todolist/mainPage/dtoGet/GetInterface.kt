package com.example.todolist.mainPage.dtoGet

import retrofit2.http.GET
import retrofit2.http.Query

interface GetInterface {
    companion object {
       // private const val AUTH_KEY = "로그인토큰"
    }

    @GET("gettourspot")
    fun getTourSpot(
        //@Query("serviceKey") serviceKey : String = AUTH_KEY,
        @Query("pageNo") pageNo : String = "1",
        @Query("numOfRows") numOfRows : String = "3"
    ): Call<>
}