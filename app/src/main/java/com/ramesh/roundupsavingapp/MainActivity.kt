package com.ramesh.roundupsavingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ramesh.roundupsavingapp.presentation.roundup.create_goals.CreateSavingsGoalScreen
import com.ramesh.roundupsavingapp.presentation.roundup.create_goals.CreateSavingsGoalViewModel
import com.ramesh.roundupsavingapp.presentation.roundup.home.SavingsScreen
import com.ramesh.roundupsavingapp.presentation.roundup.home.SavingsViewModel
import com.ramesh.roundupsavingapp.ui.theme.RoundUpSavingAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val savingsViewModel: SavingsViewModel by viewModels() // ViewModel injected via Hilt
    private val createSavingsViewModel: CreateSavingsGoalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoundUpSavingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Setup NavController
                    val navController = rememberNavController()

                    // Setup NavHost for navigation
                    NavHost(navController = navController, startDestination = "savings_screen",  modifier = Modifier.padding(innerPadding)) {

                        composable("savings_screen") {
                            SavingsScreen(viewModel = savingsViewModel, navController)
                        }
                        composable(
                            "create_savings_goal_screen/{accountId}",
                            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val accountId = backStackEntry.arguments?.getString("accountId") ?: ""
                            // Pass accountId to CreateSavingsGoalScreen
                            CreateSavingsGoalScreen(createSavingsViewModel, navController, accountId)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RoundUpSavingAppTheme {
        Greeting("Android")
    }
}