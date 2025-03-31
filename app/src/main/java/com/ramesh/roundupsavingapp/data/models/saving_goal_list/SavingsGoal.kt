package com.ramesh.roundupsavingapp.data.models.saving_goal_list

data class SavingsGoal(
    val name: String,
    val savedPercentage: Int,
    val savingsGoalUid: String,
    val state: String,
    val target: Target,
    val totalSaved: TotalSaved
)