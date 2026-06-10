package com.feature.auth.model

data class OtpRequest(
    val channel: OtpChannel,
    val locale: String? = null,
    val extras: Map<String, String> = emptyMap()
)
