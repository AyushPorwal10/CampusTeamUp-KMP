package com.feature.auth.contract

interface UserProfileService {
    suspend fun getOrCreateUserId(uid: String): String
}
