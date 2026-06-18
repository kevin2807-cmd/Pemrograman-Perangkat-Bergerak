package com.example.newsapp.data.repository

import com.example.newsapp.data.api.RetrofitClient

class NewsRepository {
    suspend fun getNews() =
        RetrofitClient.apiService
            .getTopHeadlines(
                apiKey = "4c6c57d2aa7743ef99d2d9b5e2b81949")
}