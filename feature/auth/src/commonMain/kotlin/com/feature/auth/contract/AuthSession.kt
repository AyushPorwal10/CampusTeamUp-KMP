package com.feature.auth.contract

import com.feature.auth.model.AuthIdentity
import kotlinx.coroutines.flow.Flow

interface AuthSession {
    val currentUser: Flow<AuthIdentity?>
    suspend fun save(identity: AuthIdentity)
    suspend fun clear()
}
