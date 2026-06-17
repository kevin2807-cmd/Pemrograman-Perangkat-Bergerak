package com.example.starbucksappclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.starbucksappclone.screens.*
import com.example.starbucksappclone.viewmodel.CoffeeBlissViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: CoffeeBlissViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.route
    ) {
        composable(route = Screens.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(route = Screens.LoginScreen.route) {
            LoginScreen(navController, viewModel)
        }
        composable(
            route = Screens.HomeScreen.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            HomeScreen(navController, memberId, viewModel)
        }
        composable(
            route = Screens.MemberCardScreen.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            MemberCardScreen(navController, memberId, viewModel)
        }
        composable(
            route = Screens.TransactionHistoryScreen.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            TransactionHistoryScreen(navController, memberId, viewModel)
        }
        composable(
            route = Screens.AddTransactionScreen.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            AddTransactionScreen(navController, memberId, viewModel)
        }
        composable(
            route = Screens.TransactionSuccessScreen.route,
            arguments = listOf(
                navArgument("pointsEarned") { type = NavType.IntType },
                navArgument("totalPoints") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val pointsEarned = backStackEntry.arguments?.getInt("pointsEarned") ?: 0
            val totalPoints = backStackEntry.arguments?.getInt("totalPoints") ?: 0
            TransactionSuccessScreen(navController, pointsEarned, totalPoints)
        }
        composable(
            route = Screens.RewardScreen.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            RewardScreen(navController, memberId, viewModel)
        }
        composable(
            route = Screens.RewardDetailScreen.route,
            arguments = listOf(
                navArgument("memberId") { type = NavType.IntType },
                navArgument("rewardName") { type = NavType.StringType },
                navArgument("rewardPoints") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            val rewardName = backStackEntry.arguments?.getString("rewardName") ?: ""
            val rewardPoints = backStackEntry.arguments?.getInt("rewardPoints") ?: 0
            RewardDetailScreen(navController, memberId, rewardName, rewardPoints, viewModel)
        }
        composable(
            route = Screens.RedeemSuccessScreen.route,
            arguments = listOf(navArgument("remainingPoints") { type = NavType.IntType })
        ) { backStackEntry ->
            val remainingPoints = backStackEntry.arguments?.getInt("remainingPoints") ?: 0
            RedeemSuccessScreen(navController, remainingPoints)
        }
        composable(
            route = Screens.ProfileScreen.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            ProfileScreen(navController, memberId, viewModel)
        }
    }
}
