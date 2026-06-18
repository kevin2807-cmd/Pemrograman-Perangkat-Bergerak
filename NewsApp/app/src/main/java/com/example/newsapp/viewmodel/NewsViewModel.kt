package com.example.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.data.model.Article

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(
        val articles: List<Article>
    ) : NewsUiState()
    data class Error(
        val message: String
    ) : NewsUiState()
}

class NewsViewModel : ViewModel() {
    private val repository =
        NewsRepository()
    private val _uiState =
        MutableStateFlow<NewsUiState>(
            NewsUiState.Loading
        )
    val uiState =
        _uiState.asStateFlow()
    init {
        loadNews()
    }
    fun loadNews() {
        viewModelScope.launch {
            try {
                _uiState.value =
                    NewsUiState.Loading
                val response =
                    repository.getNews()
                _uiState.value =
                    NewsUiState.Success(
                        response.articles
                    )
            } catch (e: Exception) {
                _uiState.value =
                    NewsUiState.Error(
                        e.message ?: "Unknown Error"
                    )
            }
        }
    }
}