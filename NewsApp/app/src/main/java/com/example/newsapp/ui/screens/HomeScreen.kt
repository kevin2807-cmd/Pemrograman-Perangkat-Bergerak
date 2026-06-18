package com.example.newsapp.ui.screens


import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import com.example.newsapp.data.model.Article
import com.example.newsapp.ui.components.NewsCard
import com.example.newsapp.viewmodel.NewsUiState
import com.example.newsapp.viewmodel.NewsViewModel

@Composable
fun HomeScreen(
    viewModel: NewsViewModel,
    onDetailClick: (Article) -> Unit
) {
    val state by
    viewModel.uiState.collectAsState()
    when(state){
        is NewsUiState.Loading -> {
            Box(
                modifier =
                    Modifier.fillMaxSize(),
                contentAlignment =
                    Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        is NewsUiState.Success -> {
            val articles =
                (state as NewsUiState.Success)
                    .articles
            LazyColumn {
                items(articles){ article ->NewsCard(
                    article = article
                ){
                    onDetailClick(article)

                }
                }
            }
        }
        is NewsUiState.Error
            -> {

            val error =
                state as NewsUiState.Error
            Column(
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ){
                Text(error.message)
                Button(
                    onClick = {
                        viewModel.loadNews()

                    }
                ){
                    Text("Retry")
                }
            }
        }
    }
}