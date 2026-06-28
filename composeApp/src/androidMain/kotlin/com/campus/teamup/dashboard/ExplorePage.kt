package com.campus.teamup.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.campus.teamup.common.UiState
import com.campus.teamup.dashboard.cards.CPTeamUp
import com.campus.teamup.dashboard.cards.FounderHubCard
import com.campus.teamup.dashboard.cards.ProjectCard
import com.campus.teamup.dashboard.cards.RolesCard
import com.campus.teamup.dashboard.cards.VacancyCard
import com.campus.teamup.domain.DashboardLayout
import com.campus.teamup.ui.components.FloatingBubbles
import com.campus.teamup.ui.theme.BackgroundGradientColor
import com.campus.teamup.ui.theme.IconColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeLandingPage() {
    val viewModel: DashboardViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = BackgroundGradientColor,
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    ) {
        FloatingBubbles()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopGreetingCard(userName = "Ayush")

            ExploreOpportunitiesHeader()

            when (val state = uiState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = IconColor)
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = state.message,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is UiState.Success -> {
                    state.data.forEach { layout ->
                        when (layout) {
                            is DashboardLayout.Role -> RolesCard(onClick = {})
                            is DashboardLayout.Vacancy -> VacancyCard(onClick = {})
                            is DashboardLayout.Project -> ProjectCard(onClick = {})
                            is DashboardLayout.CPTeamUp -> CPTeamUp(onClick = {})
                            is DashboardLayout.FounderHub -> FounderHubCard(onClick = {})
                        }
                    }
                }
                is UiState.Idle -> Unit
            }
        }
    }
}

@Composable
private fun TopGreetingCard(userName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEEFF)),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 28.dp,
            bottomEnd = 28.dp
        ),
        elevation = CardDefaults.cardElevation(16.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Hello 👋",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = userName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun ExploreOpportunitiesHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Explore Opportunities",
            style = MaterialTheme.typography.titleLarge,
            color = IconColor
        )
    }
}
