package com.example.loginmvvpapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loginmvvpapp.data.local.database.AppDatabase
import com.example.loginmvvpapp.data.repository.UserRepository
import com.example.loginmvvpapp.ui.screen.LoginScreen
import com.example.loginmvvpapp.ui.screen.RegisterScreen
import com.example.loginmvvpapp.viewmodel.LoginViewModel
import com.example.loginmvvpapp.viewmodel.LoginViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = UserRepository(database.userDao())
        val factory = LoginViewModelFactory(repository)

        setContent {

            val navController = rememberNavController()
            val viewModel: LoginViewModel = viewModel(factory = factory)

            LaunchedEffect(Unit) {
                viewModel.insertDummyUser()
            }

            NavHost(navController = navController, startDestination = "login_screen") {

                composable("login_screen") {
                    LoginScreen(
                        viewModel = viewModel,
                        onNavigateToRegister = {
                            navController.navigate("register_screen")
                        }
                    )
                }

                composable("register_screen") {
                    RegisterScreen(
                        repository = repository,
                        onNavigateBackToLogin = {
                            navController.popBackStack() // Kembali ke layar sebelumnya (Login)
                        }
                    )
                }
            }
        }
    }
}