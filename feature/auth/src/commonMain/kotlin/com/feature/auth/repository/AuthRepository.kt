package com.feature.auth.repository

import com.feature.auth.contract.AuthSession
import com.feature.auth.contract.OtpSendResult
import com.feature.auth.contract.OtpService
import com.feature.auth.contract.UserProfileService
import com.feature.auth.model.AuthIdentity
import com.feature.auth.model.AuthResult
import com.feature.auth.model.OtpRequest
import com.feature.auth.model.OtpVerifyRequest
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val otpService: OtpService,
    private val session: AuthSession,
    private val userProfileService: UserProfileService
) {
    val currentUser: Flow<AuthIdentity?> = session.currentUser

    suspend fun sendOtp(request: OtpRequest): AuthResult {
        return when (val result = otpService.sendOtp(request)) {
            is OtpSendResult.Success -> AuthResult.OtpSent(result.verificationId, request.channel)
            is OtpSendResult.Error -> AuthResult.Error(result.message)
        }
    }

    suspend fun verifyOtp(request: OtpVerifyRequest): AuthResult {
        return runCatching {
            val identity = otpService.verifyOtp(request)
            val userId = userProfileService.getOrCreateUserId(identity.uid)
            val fullIdentity = identity.copy(userId = userId)
            session.save(fullIdentity)
            AuthResult.Success(fullIdentity)
        }.getOrElse {
            AuthResult.Error(it.message ?: "OTP verification failed", it)
        }
    }

    suspend fun signOut() {
        session.clear()
    }
}
