package com.ramesh.roundupsavingapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramesh.roundupsavingapp.data.models.saving_goal_list.SavingsGoal
import com.ramesh.roundupsavingapp.presentation.roundup.home.SavingsViewModel

@Composable
fun NoAccountView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "No accounts found. Please set up an account.",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun NoSavingsGoalView(roundUpAmount: Double, navController: NavController) {
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
                navController.navigate("create_savings_goal_screen/4dad5b84-3da6-4f1f-ae82-7ade18477da7")
            }
        ) {
            Text("Create Savings Goal")
        }
    }
}

@Composable
fun SavingsGoalView(savingsGoal: SavingsGoal, roundUpAmount: Double, viewModel: SavingsViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Round-up available: £%.2f".format(roundUpAmount),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("💰 Savings Goal Account", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(8.dp))

                Text("Name: ${savingsGoal.name}", style = MaterialTheme.typography.bodyLarge)
                Text("Balance: £%.2f".format(savingsGoal.totalSaved), style = MaterialTheme.typography.bodyLarge)
                Text("Target: £%.2f".format(savingsGoal.target), style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { viewModel.roundUpAndSave() }) {
                    Text("Round Up & Save")
                }
            }
        }
    }
}

@Composable
fun SuccessView(message: String, viewModel: SavingsViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Success: $message",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.fetchAccountsAndGoals() }) {
            Text("Refresh")
        }
    }
}

