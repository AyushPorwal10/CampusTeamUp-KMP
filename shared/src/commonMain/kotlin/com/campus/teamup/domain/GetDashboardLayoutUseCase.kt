package com.campus.teamup.domain
import com.campus.teamup.data.DashboardConfigRepository

class GetDashboardLayoutUseCase(
    private val repository: DashboardConfigRepository
) {

    suspend operator fun invoke() : List<DashboardLayout> {
        return repository.getDashboardConfiguration()
            .filter { it.enabled }
            .sortedBy { it.priority }
            .mapNotNull { it.id.toDashboardLayout() }
    }

    fun String.toDashboardLayout() : DashboardLayout? {
        return when(lowercase()){
            "role" -> DashboardLayout.Role
            "vacancy" -> DashboardLayout.Vacancy
            "project" -> DashboardLayout.Project
            "cpteamup" -> DashboardLayout.CPTeamUp
            "founderhub" -> DashboardLayout.FounderHub
            else -> null
        }
    }
}

