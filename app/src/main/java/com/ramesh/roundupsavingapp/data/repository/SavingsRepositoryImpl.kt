package com.ramesh.roundupsavingapp.data.repository

import com.ramesh.roundupsavingapp.data.models.accounts.Account
import com.ramesh.roundupsavingapp.data.models.create_savings_goals_response.CreateSavingsGoalResponse
import com.ramesh.roundupsavingapp.data.models.feed.FeedItems
import com.ramesh.roundupsavingapp.data.models.saving_goal_list.SavingGoalListResponse
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.CreateSavingsGoalRequest
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.TransferToGoalAccountRequest
import com.ramesh.roundupsavingapp.data.network.ApiService
import com.ramesh.roundupsavingapp.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SavingsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SavingsRepository {
    override suspend fun getPrimaryAccount(): Result<List<Account>> =
        runCatching { apiService.getAccounts().accounts }

    override suspend fun getSavingsGoalsAccount(accountId: String): Result<SavingGoalListResponse> =
        runCatching { apiService.getSavingsGoalsAccount(accountId)}


    override suspend fun getTransactions(accountId: String, categoryId: String, changeSince: String): Flow<FeedItems> {
        return flow {
            try {
                // Fetch transactions from API
                val response = apiService.getTransactions(accountId, categoryId, changeSince)
                emit(response)  // Emit the transactions response wrapped in FeedItems
                println("response $response")
            } catch (e: Exception) {
                // In case of error, emit an empty FeedItems
                println("error $e")
                e.printStackTrace()
//                emit(FeedItems(emptyList()))
            }
        }
    }


    override suspend fun createSavingsGoal(accountId: String, createSavingsRequest: CreateSavingsGoalRequest): Result<CreateSavingsGoalResponse> {
        return try {
            // Assuming apiService.createSavingsGoal() makes an API call
            val response = apiService.createSavingsGoal(accountId,  createSavingsRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    override suspend fun transferToGoal(accountId: String, goalId: String, transferUUid:String, transferToGoalAccountRequest: TransferToGoalAccountRequest): Result<String> =
        runCatching { apiService.transferToGoal(accountId, goalId,transferUUid, transferToGoalAccountRequest) }
}