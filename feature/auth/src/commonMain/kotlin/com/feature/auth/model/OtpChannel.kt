package com.feature.auth.model

data class PhoneConfig(
    val number: String,
    val countryCode: String? = null
)

data class EmailConfig(
    val address: String
)

sealed class OtpChannel {
    data class Phone(val config: PhoneConfig) : OtpChannel()
    data class Email(val config: EmailConfig) : OtpChannel()
}
