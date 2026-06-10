package com.feature.auth.service

import android.app.Activity
import com.feature.auth.contract.OtpSendResult
import com.feature.auth.contract.OtpService
import com.feature.auth.model.AuthIdentity
import com.feature.auth.model.OtpChannel
import com.feature.auth.model.OtpRequest
import com.feature.auth.model.OtpVerifyRequest
import com.feature.auth.model.PhoneConfig
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

class FirebaseOtpService(
    private val activityProvider: () -> Activity
) : OtpService {

    private val auth = FirebaseAuth.getInstance()

    override suspend fun sendOtp(request: OtpRequest): OtpSendResult {
        val phone = when (request.channel) {
            is OtpChannel.Phone -> buildPhoneNumber(request.channel.config)
            is OtpChannel.Email -> return OtpSendResult.Error("Firebase phone auth only supports phone numbers")
        }

        return suspendCancellableCoroutine { continuation ->
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activityProvider()) // fetched fresh every call, never stored
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        continuation.resume(OtpSendResult.Success(verificationId))
                    }

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // auto-verified on emulator — not handled in two-step flow
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        continuation.resume(OtpSendResult.Error(e.message ?: "Failed to send OTP"))
                    }
                })
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    override suspend fun verifyOtp(request: OtpVerifyRequest): AuthIdentity {
        val credential = PhoneAuthProvider.getCredential(request.verificationId, request.code)
        val result = auth.signInWithCredential(credential).await()
        val uid = result.user?.uid ?: error("User uid missing after OTP verification")
        return AuthIdentity(
            uid = uid,
            isNewUser = result.additionalUserInfo?.isNewUser ?: false
        )
    }

    private fun buildPhoneNumber(config: PhoneConfig): String {
        return buildString {
            append(config.countryCode ?: "")
            append(config.number)
        }
    }
}
