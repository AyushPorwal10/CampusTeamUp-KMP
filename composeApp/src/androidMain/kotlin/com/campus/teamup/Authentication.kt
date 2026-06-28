package com.campus.teamup

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.campus.teamup.auth.AuthViewModel
import com.campus.teamup.auth.LoginScreenLandingPage
import com.campus.teamup.auth.OtpScreenLandingPage
import org.koin.androidx.compose.koinViewModel

@Composable
fun Authentication(onOtpVerified: () -> Unit) {
    val activity = LocalActivity.current as ComponentActivity
    val authViewModel: AuthViewModel = koinViewModel(viewModelStoreOwner = activity)
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreenLandingPage(navController = navController, viewModel = authViewModel)
        }
        composable("otp") {
            OtpScreenLandingPage(
                navController = navController,
                viewModel = authViewModel,
                onOtpVerified = onOtpVerified
            )
        }
    }
}
