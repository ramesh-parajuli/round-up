package com.ramesh.roundupsavingapp.data.models.accounts

data class Account(
    val accountType: String,
    val accountUid: String,
    val createdAt: String,
    val currency: String,
    val defaultCategory: String,
    val name: String
)