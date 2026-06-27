package com.campus.teamup.data

import kotlinx.serialization.Serializable

@Serializable
data class DashboardComponentConfig(
    val id: String = "",
    val enabled: Boolean = true,
    val priority: Int = 0
)

@Serializable
data class DashboardConfigResponse(
    val components: List<DashboardComponentConfig> = emptyList()
)