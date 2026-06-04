package com.example.registrasisiswa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.registrasisiswa.data.AppDatabase
import com.example.registrasisiswa.ui.MainScreen
import com.example.registrasisiswa.viewmodel.StudentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = AppDatabase
            .getDatabase(applicationContext)
            .siswaDao()

        setContent {
            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return StudentViewModel(dao) as T
                }
            }

            val viewModel: StudentViewModel = viewModel(factory = factory)

            MainScreen(viewModel)
        }
    }
}