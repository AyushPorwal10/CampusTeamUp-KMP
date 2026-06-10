package com.feature.auth.model

sealed class AuthResult {
    data class Success(val identity: AuthIdentity) : AuthResult()
    data class OtpSent(val verificationId: String, val channel: OtpChannel) : AuthResult()
    data class Error(val message: String, val cause: Throwable? = null) : AuthResult()
}
