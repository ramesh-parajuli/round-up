package com.ramesh.roundupsavingapp.domain.repository

import com.ramesh.roundupsavingapp.data.models.accounts.Account
import com.ramesh.roundupsavingapp.data.models.create_savings_goals_response.CreateSavingsGoalResponse
import com.ramesh.roundupsavingapp.data.models.feed.FeedItems
import com.ramesh.roundupsavingapp.data.models.saving_goal_list.SavingGoalListResponse
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.CreateSavingsGoalRequest
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.TransferToGoalAccountRequest
import kotlinx.coroutines.flow.Flow

interface SavingsRepository {
    suspend fun getPrimaryAccount(): Result<List<Account>>
    suspend fun getSavingsGoalsAccount(accountId: String): Result<SavingGoalListResponse>
    suspend fun getTransactions(accountId: String,categoryId: String, changeSince: String): Flow<FeedItems>
    suspend fun createSavingsGoal(accountId: String, createSavingsRequest: CreateSavingsGoalRequest): Result<CreateSavingsGoalResponse>
    suspend fun transferToGoal(accountId: String, goalId: String, transferUUid:String, transferToGoalAccountRequest: TransferToGoalAccountRequest): Result<String>
}