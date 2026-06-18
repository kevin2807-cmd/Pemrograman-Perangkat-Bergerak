package com.example.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newsapp.ui.screens.HomeScreen
import com.example.newsapp.viewmodel.NewsViewModel

@Composable
fun AppNavGraph() {
    val navController =
        rememberNavController()
    val viewModel =
        viewModel<NewsViewModel>()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel
            ) { article ->
            }
        }
    }
}