package com.feature.auth.model

data class OtpVerifyRequest(
    val verificationId: String,
    val code: String,
    val extras: Map<String, String> = emptyMap()
)
