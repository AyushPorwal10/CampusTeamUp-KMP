package com.campus.teamup.domain

sealed class DashboardLayout {
    data object Role : DashboardLayout()
    data object Vacancy : DashboardLayout()
    data object Project : DashboardLayout()
    data object CPTeamUp : DashboardLayout()
    object FounderHub : DashboardLayout()
}