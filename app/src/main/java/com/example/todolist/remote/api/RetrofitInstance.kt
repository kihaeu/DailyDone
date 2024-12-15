package com.example.todolist.remote.api

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL: String = "http://na2ru2.me:5153"

    // SharedPreferences에서 토큰을 가져오는 메서드
    private fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("TOKEN_KEY", null)
    }

    // 토큰을 SharedPreferences에서 제거하는 메서드
    private fun clearToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("TOKEN_KEY")
        editor.apply()
    }

    // 토큰을 헤더에 추가하는 Interceptor
    class AuthInterceptor(private val context: Context) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = getToken(context)
            val requestBuilder = chain.request().newBuilder()

            if (token != null) {
                Log.e("KKKKK", token)
                requestBuilder.header("Auth-Token", token)
            }

            val response = chain.proceed(requestBuilder.build())

            // 403 에러가 발생하면 토큰을 지움
            if (response.code == 403) {
                clearToken(context)
                Log.e("AuthInterceptor", "Token is invalid. Clearing token from SharedPreferences.")
            }

            return response
        }
    }

    // Retrofit 인스턴스를 반환하는 메서드
    fun getInstance(context: Context): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

}
