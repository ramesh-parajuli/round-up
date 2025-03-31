package com.ramesh.roundupsavingapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramesh.roundupsavingapp.presentation.roundup.home.SavingsViewModel

@Composable
fun ErrorView(message: String, viewModel: SavingsViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Error: $message",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.fetchAccountsAndGoals() }) {
            Text("Retry")
        }
    }
}
