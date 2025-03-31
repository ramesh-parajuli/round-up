package com.ramesh.roundupsavingapp.presentation.roundup.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramesh.roundupsavingapp.presentation.components.ErrorView
import com.ramesh.roundupsavingapp.presentation.components.NoAccountView
import com.ramesh.roundupsavingapp.presentation.components.NoSavingsGoalView
import com.ramesh.roundupsavingapp.presentation.components.SavingsGoalView
import com.ramesh.roundupsavingapp.presentation.components.SuccessView

@Composable
fun SavingsScreen(viewModel: SavingsViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    val accounts by viewModel.accounts.collectAsState()
// Trigger refresh when screen is recomposed (e.g., after returning from another screen)
    LaunchedEffect(Unit) {
        viewModel.fetchAccountsAndGoals()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.NoAccount -> {
                NoAccountView()
            }

            is UiState.NoSavingsGoal -> {
                NoSavingsGoalView(
                    roundUpAmount = (uiState as UiState.NoSavingsGoal).roundUpAmount,
                    navController = navController,
                    accountUid = accounts.firstOrNull()?.accountUid
                )
            }

            is UiState.SavingsGoalAvailable -> {
                SavingsGoalView(
                    savingsGoal = (uiState as UiState.SavingsGoalAvailable).savingsGoal,
                    roundUpAmount = (uiState as UiState.SavingsGoalAvailable).roundUpAmount,
                    onTransfer = { viewModel.roundUpAndSave() },
                    onCreateGoal = {
                        accounts.firstOrNull()?.accountUid?.let { accountUid ->
                            navController.navigate("create_savings_goal_screen/$accountUid")
                        }
                    }
                )
            }

            is UiState.Success -> {
                SuccessView(
                    message = (uiState as UiState.Success).message,
                    onRefresh = { viewModel.fetchAccountsAndGoals() }
                )
            }

            is UiState.Error -> {
                ErrorView(
                    message = (uiState as UiState.Error).message,
                    viewModel = viewModel
                )
            }
        }
        // Refresh Button
        Button(
            onClick = { viewModel.fetchAccountsAndGoals() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Refresh")
        }
    }
}

