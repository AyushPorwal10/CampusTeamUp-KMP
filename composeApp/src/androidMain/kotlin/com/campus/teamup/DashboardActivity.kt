package com.campus.teamup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.campus.teamup.common.UiState
import com.campus.teamup.dashboard.DashboardViewModel
import com.campus.teamup.dashboard.cards.RolesCard
import com.campus.teamup.dashboard.cards.VacancyCard
import com.campus.teamup.domain.DashboardLayout
import org.koin.androidx.compose.koinViewModel

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            DashboardScreen()
        }
    }
}

@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Text(state.message)
                is UiState.Success -> DashboardContent(layouts = state.data)
                is UiState.Idle -> Unit
            }
        }
    }
}

@Composable
private fun DashboardContent(layouts: List<DashboardLayout>) {
    LazyColumn {
        items(layouts) { layout ->
            when (layout) {
                is DashboardLayout.Role -> RolesCard(onClick = {})
                is DashboardLayout.Vacancy -> VacancyCard(onClick = {})
                is DashboardLayout.Project -> Unit   // card coming soon
                is DashboardLayout.CPTeamUp -> Unit  // card coming soon
                is DashboardLayout.FounderHub -> Unit // card coming soon
            }
        }
    }
}
