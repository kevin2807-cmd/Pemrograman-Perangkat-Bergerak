package com.example.newsapp.data.api
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.newsapp.data.model.NewsResponse

interface ApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        country: String = "us",
        @Query("apiKey")
        apiKey: String
    ): NewsResponse
}