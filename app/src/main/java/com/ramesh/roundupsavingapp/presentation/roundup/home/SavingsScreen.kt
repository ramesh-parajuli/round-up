package com.ramesh.roundupsavingapp.presentation.roundup.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramesh.roundupsavingapp.presentation.components.ErrorView

@Composable
fun SavingsScreen(viewModel: SavingsViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()

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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "No accounts found. Please set up an account.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            is UiState.NoSavingsGoal -> {
                val roundUpAmount = (uiState as UiState.NoSavingsGoal).roundUpAmount

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Round-up available: £%.2f".format(roundUpAmount),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "No savings goal found. Create one first.",
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            navController.navigate("create_savings_goal_screen/${viewModel.accounts.value.first().accountUid}")
                        }
                    ) {
                        Text("Create Savings Goal")
                    }
                }
            }

            is UiState.SavingsGoalAvailable -> {
                val savingsGoal = (uiState as UiState.SavingsGoalAvailable).savingsGoal
                val roundUpAmount = (uiState as UiState.SavingsGoalAvailable).roundUpAmount

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        "Savings Goal Account",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text("Account Name: ${savingsGoal.name}",style = MaterialTheme.typography.headlineSmall)
                    Text("Target: £%.2f".format((savingsGoal.target.minorUnits).toDouble()/100))
                    Text("Balance: £%.2f".format((savingsGoal.totalSaved.minorUnits).toDouble()/100))

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Round-up available: £%.2f".format(roundUpAmount),
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Would you like to transfer the available roundup to goal-saver account ${savingsGoal.name}"
                    )
                    Button(onClick = { viewModel.roundUpAndSave() }) {
                        Text("Transfer Round-Up")
                    }

                    Button(
                        onClick = {
                            navController.navigate("create_savings_goal_screen/${viewModel.accounts.value.first().accountUid}")
                        }
                    ) {
                        Text("Create Savings Goal")
                    }
                }
            }

            is UiState.Success -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Success: ${(uiState as UiState.Success).message}",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { viewModel.fetchAccountsAndGoals() }) {
                        Text("Refresh")
                    }
                }
            }

            is UiState.Error -> {
//                ErrorView(message = (uiState as UiState.Error).message, viewModel = viewModel)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Error: ${(uiState as UiState.Error).message}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { viewModel.fetchAccountsAndGoals() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

