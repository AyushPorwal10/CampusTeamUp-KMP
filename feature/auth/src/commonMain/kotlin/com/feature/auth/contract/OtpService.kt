package com.feature.auth.contract

import com.feature.auth.model.AuthIdentity
import com.feature.auth.model.OtpRequest
import com.feature.auth.model.OtpVerifyRequest

interface OtpService {
    suspend fun sendOtp(request: OtpRequest): OtpSendResult
    suspend fun verifyOtp(request: OtpVerifyRequest): AuthIdentity
}

sealed class OtpSendResult {
    data class Success(val verificationId: String) : OtpSendResult()
    data class Error(val message: String) : OtpSendResult()
}
