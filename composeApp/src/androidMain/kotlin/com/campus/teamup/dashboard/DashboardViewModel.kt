package com.campus.teamup.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campus.teamup.common.UiState
import com.campus.teamup.domain.DashboardLayout
import com.campus.teamup.domain.GetDashboardLayoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getDashboardLayout: GetDashboardLayoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<DashboardLayout>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            runCatching { getDashboardLayout() } // TODO check if Erro handling can be moved to usecase
                .onSuccess { _uiState.value = UiState.Success(it) }
                .onFailure { _uiState.value = UiState.Error(it.message ?: "Failed to load dashboard") }
        }
    }
}
