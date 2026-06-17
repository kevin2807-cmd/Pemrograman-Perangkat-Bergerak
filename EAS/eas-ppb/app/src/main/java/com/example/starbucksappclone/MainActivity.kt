package com.example.starbucksappclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.starbucksappclone.data.local.AppDatabase
import com.example.starbucksappclone.navigation.NavGraph
import com.example.starbucksappclone.repository.CoffeeBlissRepository
import com.example.starbucksappclone.ui.theme.StarbucksAppCloneTheme
import com.example.starbucksappclone.viewmodel.CoffeeBlissViewModel
import com.example.starbucksappclone.viewmodel.CoffeeBlissViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarbucksAppCloneTheme {
                val database = AppDatabase.getDatabase(applicationContext)
                val repository = CoffeeBlissRepository(database.memberDao(), database.transactionDao())
                val factory = CoffeeBlissViewModelFactory(repository)
                val viewModel: CoffeeBlissViewModel = viewModel(factory = factory)

                val navController = rememberNavController()
                NavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}