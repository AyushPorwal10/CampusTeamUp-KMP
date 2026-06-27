package com.campus.teamup

import com.campus.teamup.data.DashboardComponentConfig
import com.campus.teamup.data.DashboardConfigRepository
import com.campus.teamup.data.DashboardConfigResponse
import dev.gitlive.firebase.firestore.FirebaseFirestore

class FirebaseDashboardConfigRepository(
    private val firebaseStore: FirebaseFirestore
) : DashboardConfigRepository {
    override suspend fun getDashboardConfiguration(): List<DashboardComponentConfig> {
        val snapshot = firebaseStore
            .collection("dashboard")
            .document("config")
            .get()

        return snapshot.data<DashboardConfigResponse>().components
    }
}