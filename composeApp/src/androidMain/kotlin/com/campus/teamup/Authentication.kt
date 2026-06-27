package com.campus.teamup

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.campus.teamup.auth.AuthViewModel
import com.campus.teamup.auth.LoginScreenLandingPage
import com.campus.teamup.auth.OtpScreenLandingPage
import org.koin.androidx.compose.koinViewModel

@Composable
fun Authentication(onOtpVerified: () -> Unit) {
    val activity  = LocalActivity.current as ComponentActivity

    val authViewModel : AuthViewModel = koinViewModel(
        viewModelStoreOwner = activity
    )

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreenLandingPage(navController = navController,
                authViewModel, )
        }

        composable("otp/{verificationId}/{phone}") { backStackEntry ->
            val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpScreenLandingPage(
                navController = navController,
                authViewModel,
                verificationId = verificationId,
                phone = phone,
                onOtpVerified = onOtpVerified
            )
        }
    }
}
