package com.ramesh.roundupsavingapp.data.models.savinggoal_request

data class CreateSavingsGoalRequest(
    val base64EncodedPhoto: String,
    val currency: String,
    val name: String,
    val target: Target
)