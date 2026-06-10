package com.feature.auth.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.feature.auth.contract.AuthSession
import com.feature.auth.model.AuthIdentity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthSessionImpl(
    private val dataStore: DataStore<Preferences>
) : AuthSession {

    override val currentUser: Flow<AuthIdentity?> = dataStore.data.map { prefs ->
        val uid = prefs[Keys.UID] ?: return@map null
        AuthIdentity(
            uid = uid,
            userId = prefs[Keys.USER_ID] ?: "",
            isNewUser = prefs[Keys.IS_NEW_USER] ?: false
        )
    }

    override suspend fun save(identity: AuthIdentity) {
        dataStore.edit { prefs ->
            prefs[Keys.UID] = identity.uid
            prefs[Keys.USER_ID] = identity.userId
            prefs[Keys.IS_NEW_USER] = identity.isNewUser
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    private object Keys {
        val UID = stringPreferencesKey("auth_uid")
        val USER_ID = stringPreferencesKey("auth_user_id")
        val IS_NEW_USER = booleanPreferencesKey("auth_is_new_user")
    }
}
