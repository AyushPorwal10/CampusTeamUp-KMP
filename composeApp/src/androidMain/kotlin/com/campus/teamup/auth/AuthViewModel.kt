package com.campus.teamup.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.auth.model.AuthResult
import com.feature.auth.model.OtpChannel
import com.feature.auth.model.OtpRequest
import com.feature.auth.model.OtpVerifyRequest
import com.feature.auth.model.PhoneConfig
import com.feature.auth.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class OtpSent(val verificationId: String, val phone: String) : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun sendOtp(phone: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val request = OtpRequest(
                channel = OtpChannel.Phone(PhoneConfig(number = phone, countryCode = "+91"))
            )
            when (val result = repo.sendOtp(request)) {
                is AuthResult.OtpSent -> _uiState.value = AuthUiState.OtpSent(result.verificationId, phone)
                is AuthResult.Error -> _uiState.value = AuthUiState.Error(result.message)
                else -> {}
            }
        }
    }

    fun verifyOtp(verificationId: String, code: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val request = OtpVerifyRequest(verificationId = verificationId, code = code)
            when (val result = repo.verifyOtp(request)) {
                is AuthResult.Success -> _uiState.value = AuthUiState.Success
                is AuthResult.Error -> _uiState.value = AuthUiState.Error(result.message)
                else -> {}
            }
        }
    }

    fun resetError() {
        _uiState.value = AuthUiState.Idle
    }
}
