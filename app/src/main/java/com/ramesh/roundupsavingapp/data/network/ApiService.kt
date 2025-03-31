package com.ramesh.roundupsavingapp.data.network

import com.ramesh.roundupsavingapp.data.models.accounts.Accounts
import com.ramesh.roundupsavingapp.data.models.create_savings_goals_response.CreateSavingsGoalResponse
import com.ramesh.roundupsavingapp.data.models.feed.FeedItems
import com.ramesh.roundupsavingapp.data.models.saving_goal_list.SavingGoalListResponse
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.CreateSavingsGoalRequest
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.TransferToGoalAccountRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("accounts")
    suspend fun getAccounts(): Accounts

    @GET("feed/account/{accountId}/category/{categoryId}")
    suspend fun getTransactions(
        @Path("accountId") accountId: String,
        @Path("categoryId") categoryId: String,
        @Query("changesSince") changesSince: String
    ): FeedItems

    @GET("account/{accountId}/savings-goals")
    suspend fun getSavingsGoalsAccount(
        @Path("accountId") accountId: String
    ): SavingGoalListResponse

    @PUT("account/{id}/savings-goals")
    suspend fun createSavingsGoal(@Path("id") accountId: String, @Body request: CreateSavingsGoalRequest): CreateSavingsGoalResponse

    @PUT("account/{id}/savings-goals/{goalId}/add-money/{transferUUID}")
    suspend fun transferToGoal(@Path("id") accountId: String, @Path("goalId") goalId: String,@Path("transferUUID") transferUUID: String, @Body request: TransferToGoalAccountRequest): String
}
