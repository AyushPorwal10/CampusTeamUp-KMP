package com.campus.teamup

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.campus.teamup.auth.LoginScreenLandingPage
import com.campus.teamup.auth.OtpScreenLandingPage

@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreenLandingPage(navController = navController)
        }
        composable("otp/{verificationId}/{phone}") { backStackEntry ->
            val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpScreenLandingPage(
                navController = navController,
                verificationId = verificationId,
                phone = phone
            )
        }
    }
}
