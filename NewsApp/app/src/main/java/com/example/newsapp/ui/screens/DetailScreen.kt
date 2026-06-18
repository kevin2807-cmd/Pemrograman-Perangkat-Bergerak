package com.example.newsapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import coil.compose.AsyncImage
import com.example.newsapp.data.model.Article

@Composable
fun DetailScreen(
    article: Article
) {
    Column {
        AsyncImage(
            model = article.urlToImage,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        Text(
            article.title,
            style =
                MaterialTheme.typography.headlineSmall,
            modifier =
                Modifier.padding(16.dp)
        )
        Text(
            article.description ?: "",
            modifier =
                Modifier.padding(16.dp)
        )
        Text(
            article.content ?: "",
            modifier =
                Modifier.padding(16.dp)
        )
    }
}