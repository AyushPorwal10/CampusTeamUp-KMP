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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.campus.teamup.ui.components.FloatingBubbles
import com.campus.teamup.ui.theme.BackgroundGradientColor
import com.campus.teamup.ui.theme.ButtonColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenLandingPage(navController: NavHostController) {
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        when (uiState) {
            is AuthUiState.OtpSent -> {
                val state = uiState as AuthUiState.OtpSent
                navController.navigate("otp/${state.verificationId}/${state.phone}")
            }

            is AuthUiState.Error -> {
                Toast.makeText(context, (uiState as AuthUiState.Error).message, Toast.LENGTH_SHORT)
                    .show()
                viewModel.resetError()
            }

            else -> {}
        }
    }

    LoginScreen(
        uiState = uiState,
        onSendOtp = { phone -> viewModel.sendOtp(phone) }
    )
}

@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onSendOtp: (String) -> Unit
) {
    val context = LocalContext.current
    var phone by remember { mutableStateOf("") }

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
                Button(
                    onClick = {
                        if (phone.length == 10) onSendOtp(phone)
                        else Toast.makeText(context, "Enter a valid 10-digit number", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(brush = Brush.linearGradient(BackgroundGradientColor))
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 32.dp),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(0.dp),
                ) {
                        if (uiState is AuthUiState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Continue",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                }
            }
        ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 80.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Login or signup",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3748)
                    )
                    Text(
                        text = "Enter your phone number to continue",
                        fontSize = 14.sp,
                        color = Color(0xFF718096)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Phone Number",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3748)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .border(
                                width = 1.5.dp,
                                color = if (phone.isNotEmpty()) Color(0xFF667eea) else Color(
                                    0xFFE2E8F0
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "🇮🇳 +91",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2D3748)
                        )

                        VerticalDivider(
                            modifier = Modifier
                                .height(24.dp)
                                .width(1.dp),
                            color = Color(0xFFE2E8F0)
                        )

                        BasicTextField(
                            value = phone,
                            onValueChange = {
                                if (it.length <= 10 && it.all { c -> c.isDigit() }) phone = it
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = Color(0xFF2D3748)
                            ),
                            decorationBox = { innerTextField ->
                                if (phone.isEmpty()) {
                                    Text(
                                        text = "Eg: 9876543210",
                                        fontSize = 16.sp,
                                        color = Color(0xFFB0BEC5)
                                    )
                                }
                                innerTextField()
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
        }
    }
}
