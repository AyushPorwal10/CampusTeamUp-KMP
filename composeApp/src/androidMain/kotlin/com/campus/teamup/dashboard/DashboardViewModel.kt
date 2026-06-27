package com.campus.teamup.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campus.teamup.domain.DashboardLayout
import com.campus.teamup.domain.GetDashboardLayoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(val layouts: List<DashboardLayout>) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel(
    private val getDashboardLayout: GetDashboardLayoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            runCatching { getDashboardLayout() }
                .onSuccess { _uiState.value = DashboardUiState.Success(it) }
                .onFailure { _uiState.value = DashboardUiState.Error(it.message ?: "Failed to load dashboard") }
        }
    }
}
