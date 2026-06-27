package com.campus.teamup.data

interface DashboardConfigRepository {

    suspend fun getDashboardConfiguration() : List<DashboardComponentConfig>
}