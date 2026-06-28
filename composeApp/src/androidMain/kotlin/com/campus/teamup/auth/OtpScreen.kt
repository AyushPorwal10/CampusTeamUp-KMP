package com.campus.teamup.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.campus.teamup.ui.components.FloatingBubbles
import com.campus.teamup.ui.theme.BackgroundGradientColor
import com.campus.teamup.ui.theme.ButtonColor
import com.campus.teamup.ui.theme.IconColor

@Composable
fun OtpScreenLandingPage(
    navController: NavHostController,
    viewModel: AuthViewModel,
    onOtpVerified: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val verificationId by viewModel.verificationId.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                // Resend OTP: refresh OTP screen with new verificationId
                is AuthEvent.NavigateToOtp -> {
                    navController.navigate("otp") {
                        popUpTo("otp") { inclusive = true }
                    }
                }
                is AuthEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            Toast.makeText(context, "OTP Verified!", Toast.LENGTH_SHORT).show()
            onOtpVerified()
        }
    }

    OtpScreen(
        uiState = uiState,
        phone = phone,
        onVerifyOtp = { code -> viewModel.verifyOtp(verificationId, code) },
        onResendOtp = { viewModel.sendOtp(phone) },
        onBack = { navController.popBackStack() }
    )
}

@Composable
fun OtpScreen(
    uiState: AuthUiState,
    phone: String,
    onVerifyOtp: (String) -> Unit,
    onResendOtp: () -> Unit,
    onBack: () -> Unit
) {
    var otpValue by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Error) otpValue = ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = BackgroundGradientColor,
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    ) {
        FloatingBubbles()

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { if (otpValue.length == 6) onVerifyOtp(otpValue) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        enabled = otpValue.length == 6
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = if (otpValue.length == 6) ButtonColor
                                        else listOf(Color(0xFFB0BEC5), Color(0xFFB0BEC5))
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState is AuthUiState.Loading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text(text = "Verify OTP", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    TextButton(onClick = onResendOtp) {
                        Text(text = "Resend OTP", color = IconColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        ) { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 48.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Verify OTP",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3748)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "Sent to +91 $phone", fontSize = 14.sp, color = Color(0xFF718096))
                            TextButton(onClick = onBack, contentPadding = PaddingValues(0.dp)) {
                                Text(text = "Edit", fontSize = 14.sp, color = IconColor, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Enter 6-digit code",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2D3748)
                        )

                        BasicTextField(
                            value = otpValue,
                            onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) otpValue = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            decorationBox = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    repeat(6) { index ->
                                        OtpBox(char = otpValue.getOrNull(index), isCurrent = otpValue.length == index)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OtpBox(char: Char?, isCurrent: Boolean) {
    val borderColor = when {
        isCurrent -> Color(0xFF667eea)
        char != null -> Color(0xFF667eea).copy(alpha = 0.5f)
        else -> Color(0xFFE2E8F0)
    }

    Box(
        modifier = Modifier
            .size(width = 44.dp, height = 52.dp)
            .border(
                width = if (isCurrent) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = if (char != null) Color(0xFF667eea).copy(alpha = 0.06f) else Color.White,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char?.toString() ?: "",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D3748),
            textAlign = TextAlign.Center
        )
    }
}
