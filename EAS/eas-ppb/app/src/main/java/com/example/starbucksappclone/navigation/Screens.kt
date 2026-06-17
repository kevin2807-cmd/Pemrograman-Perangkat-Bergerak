package com.example.starbucksappclone.navigation

sealed class Screens(val route: String) {
    object SplashScreen : Screens("splash_screen")
    object LoginScreen : Screens("login_screen")
    object HomeScreen : Screens("home_screen/{memberId}") {
        fun createRoute(memberId: Int) = "home_screen/$memberId"
    }
    object MemberCardScreen : Screens("member_card_screen/{memberId}") {
        fun createRoute(memberId: Int) = "member_card_screen/$memberId"
    }
    object TransactionHistoryScreen : Screens("transaction_history_screen/{memberId}") {
        fun createRoute(memberId: Int) = "transaction_history_screen/$memberId"
    }
    object AddTransactionScreen : Screens("add_transaction_screen/{memberId}") {
        fun createRoute(memberId: Int) = "add_transaction_screen/$memberId"
    }
    object TransactionSuccessScreen : Screens("transaction_success_screen/{pointsEarned}/{totalPoints}") {
        fun createRoute(pointsEarned: Int, totalPoints: Int) = "transaction_success_screen/$pointsEarned/$totalPoints"
    }
    object RewardScreen : Screens("reward_screen/{memberId}") {
        fun createRoute(memberId: Int) = "reward_screen/$memberId"
    }
    object RewardDetailScreen : Screens("reward_detail_screen/{memberId}/{rewardName}/{rewardPoints}") {
        fun createRoute(memberId: Int, rewardName: String, rewardPoints: Int) = "reward_detail_screen/$memberId/$rewardName/$rewardPoints"
    }
    object RedeemSuccessScreen : Screens("redeem_success_screen/{remainingPoints}") {
        fun createRoute(remainingPoints: Int) = "redeem_success_screen/$remainingPoints"
    }
    object ProfileScreen : Screens("profile_screen/{memberId}") {
        fun createRoute(memberId: Int) = "profile_screen/$memberId"
    }
}
