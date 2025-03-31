package com.ramesh.roundupsavingapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

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
