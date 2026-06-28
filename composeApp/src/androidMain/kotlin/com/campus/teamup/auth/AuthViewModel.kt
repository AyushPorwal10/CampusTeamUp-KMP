package com.campus.teamup.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feature.auth.model.AuthResult
import com.feature.auth.model.OtpChannel
import com.feature.auth.model.OtpRequest
import com.feature.auth.model.OtpVerifyRequest
import com.feature.auth.model.PhoneConfig
import com.feature.auth.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

sealed class AuthEvent {
    object NavigateToOtp : AuthEvent()
    data class ShowError(val message: String) : AuthEvent()
}

class AuthViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>()
    val events = _events.asSharedFlow()

    private val _phone = MutableStateFlow("")
    val phone = _phone.asStateFlow()

    private val _verificationId = MutableStateFlow("")
    val verificationId = _verificationId.asStateFlow()

    fun sendOtp(phone: String) {
        _phone.value = phone
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val request = OtpRequest(
                channel = OtpChannel.Phone(PhoneConfig(number = phone, countryCode = "+91"))
            )
            when (val result = repo.sendOtp(request)) {
                is AuthResult.OtpSent -> {
                    _verificationId.value = result.verificationId
                    _uiState.value = AuthUiState.Idle
                    _events.emit(AuthEvent.NavigateToOtp)
                }
                is AuthResult.Error -> {
                    _uiState.value = AuthUiState.Idle
                    _events.emit(AuthEvent.ShowError(result.message))
                }
                else -> _uiState.value = AuthUiState.Idle
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

    override fun onCleared() {
        super.onCleared()
    }
}
