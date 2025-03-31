package com.ramesh.roundupsavingapp.presentation.roundup.create_goals

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ramesh.roundupsavingapp.presentation.state.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSavingsGoalScreen(
    viewModel: CreateSavingsGoalViewModel,
    navController: NavController,
    accountUid: String
) {
    var goalName by remember { mutableStateOf("") }
    var targetAmount by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // Set accountId once
    LaunchedEffect(accountUid) {
        viewModel.setAccountId(accountUid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create a Savings Goal") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), // Use innerPadding to avoid overlapping with the AppBar
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = goalName,
                        onValueChange = { goalName = it },
                        label = { Text("Goal Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = targetAmount,
                        onValueChange = { targetAmount = it },
                        label = { Text("Target Amount") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (goalName.isNotEmpty() && targetAmount.isNotEmpty()) {
                                viewModel.createSavingsGoal(goalName, targetAmount.toDouble())
                            }
                        },
                        enabled = uiState !is UiState.Loading // Disable button during loading
                    ) {
                        Text("Create Goal")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    when (uiState) {
                        is UiState.Loading -> {
                            CircularProgressIndicator() // Show loading spinner
                        }

                        is UiState.Success -> {
                            Text(
                                text = (uiState as UiState.Success).message,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        is UiState.Error -> {
                            Text(
                                text = (uiState as UiState.Error).message,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        else -> {} // Idle state, do nothing
                    }
                }
                // Show loading indicator on top of everything
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator()
                }
            }
        }
    )
}
