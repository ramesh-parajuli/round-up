package com.ramesh.roundupsavingapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramesh.roundupsavingapp.data.models.saving_goal_list.SavingsGoal


@Composable
fun SavingsGoalView(
    savingsGoal: SavingsGoal,
    roundUpAmount: Double,
    onTransfer: () -> Unit,
    onCreateGoal: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Savings Goal Account", style = MaterialTheme.typography.headlineMedium)

        Text("Account Name: ${savingsGoal.name}", style = MaterialTheme.typography.headlineSmall)
        Text("Target: £%.2f".format((savingsGoal.target.minorUnits).toDouble() / 100))
        Text("Balance: £%.2f".format((savingsGoal.totalSaved.minorUnits).toDouble() / 100))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Round-up available: £%.2f".format(roundUpAmount),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Would you like to transfer the available roundup to goal-saver account: ${savingsGoal.name}?",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Button(onClick = onTransfer) {
            Text("Transfer Round-Up")
        }

    }
}