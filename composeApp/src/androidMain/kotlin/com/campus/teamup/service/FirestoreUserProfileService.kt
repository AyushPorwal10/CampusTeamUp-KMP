package com.campus.teamup.service

import com.feature.auth.contract.UserProfileService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class FirestoreUserProfileService : UserProfileService {

    private val firestore get() = Firebase.firestore

    override suspend fun getOrCreateUserId(uid: String): String {
        val userDoc = firestore.collection("users").document(uid)
        val snapshot = userDoc.get()

        if (snapshot.exists) {
            return snapshot.get("userId")
        }

        val counterDoc = firestore.collection("counters").document("users")
        return firestore.runTransaction {
            val counterSnapshot = get(counterDoc)
            val count = if (counterSnapshot.exists) counterSnapshot.get<Long>("count") else 0L
            val newCount = count + 1
            val userId = "user_%05d".format(newCount)

            set(counterDoc, mapOf("count" to newCount))
            set(userDoc, mapOf("userId" to userId, "uid" to uid))

            userId
        }
    }
}
