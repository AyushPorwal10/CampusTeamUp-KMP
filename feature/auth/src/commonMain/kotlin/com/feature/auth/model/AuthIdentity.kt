package com.feature.auth.model

data class AuthIdentity(
    val uid: String,
    val userId: String = "",
    val isNewUser: Boolean = false
)
