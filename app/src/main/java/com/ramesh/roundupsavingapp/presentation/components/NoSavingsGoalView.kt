package com.ramesh.roundupsavingapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NoSavingsGoalView(roundUpAmount: Double, navController: NavController, accountUid: String?) {
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

        accountUid?.let {
            Button(
                onClick = { navController.navigate("create_savings_goal_screen/$it") }
            ) {
                Text("Create Savings Goal")
            }
        }
    }
}
