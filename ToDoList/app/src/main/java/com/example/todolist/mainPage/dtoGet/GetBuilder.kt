package com.example.todolist.mainPage.dtoGet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GetBuilder {
        private const val BASE_URL: String = "http://na2ru2.me:5153/"

        fun getInstanceFor() : Retrofit {
            val instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return instance
        }
    }