package com.ramesh.roundupsavingapp.domain.usecase

import com.ramesh.roundupsavingapp.data.models.accounts.Account
import com.ramesh.roundupsavingapp.data.models.saving_goal_list.SavingsGoal
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.TransferToGoalAccountRequest
import com.ramesh.roundupsavingapp.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class RoundUpAndSaveUseCase @Inject constructor(
    private val repository: SavingsRepository,

) {

    suspend fun execute(): Result<String> {
        return try {
            // Fetching primary account details
            val account = repository.getPrimaryAccount().getOrThrow().first()

            // Collecting transactions from the repository (Flow to List)
            val transactions = repository.getTransactions(account.accountUid, account.defaultCategory, "2024-02-28T05:33:04.656Z").first()

            println("transactions $transactions")
            // Calculating the round-up amount
            val roundUpAmount = transactions.feedItems.sumOf {
                val amountValue = BigDecimal(it.amount.minorUnits).divide(BigDecimal(100)) // Convert to BigDecimal first
                val ceilValue = amountValue.setScale(0, RoundingMode.CEILING) // Round up to nearest whole number
                val difference = ceilValue.subtract(amountValue).setScale(2, RoundingMode.HALF_UP) // Round-Up calculation

                println("Ceil amt: $ceilValue, Amount: $amountValue, Round-Up: $difference")
                println("orig amt: ${it.amount.minorUnits}, Converted: $amountValue, Ceil: $ceilValue")

                difference.toDouble()
            }

            if (roundUpAmount > 0) {
                Result.success(""+roundUpAmount)
            } else {
                Result.failure(Exception("No round-up savings this week."))
            }
        } catch (e: Exception) {
            // Catch any exceptions and return failure result
            Result.failure(e)
        }
    }


    suspend fun getAccounts(): List<Account> {
        return repository.getPrimaryAccount().getOrThrow()
    }

    suspend fun getSavingsGoalsAccount(accountId: String): SavingsGoal? {
        val savingsGoalsResponse = repository.getSavingsGoalsAccount(accountId)
        return savingsGoalsResponse.getOrThrow().savingsGoalList.firstOrNull()
    }

    suspend fun calculateRoundUp(account: Account): Double {
        val transactions = repository.getTransactions(account.accountUid, account.defaultCategory, "2024-02-28T05:33:04.656Z").first()

        println("transactions $transactions")
        // Calculating the round-up amount
        val roundUpAmount = transactions.feedItems.sumOf {
            val amountValue = BigDecimal(it.amount.minorUnits).divide(BigDecimal(100)) // Convert to BigDecimal first
            val ceilValue = amountValue.setScale(0, RoundingMode.CEILING) // Round up to nearest whole number
            val difference = ceilValue.subtract(amountValue).setScale(2, RoundingMode.HALF_UP) // Round-Up calculation

            println("Ceil amt: $ceilValue, Amount: $amountValue, Round-Up: $difference")
            println("orig amt: ${it.amount.minorUnits}, Converted: $amountValue, Ceil: $ceilValue")

            difference.toDouble()
        }

        return roundUpAmount
    }
    suspend fun transferToGoal(
        accountUId: String,
        roundUpAmount: Double,
        goalId: String,
        transferUUid: String,
        transferToGoalAccountRequest: TransferToGoalAccountRequest
    ): Result<String> {
        return try {
            // If round-up amount is greater than 0, transfer it to the goal account
            if (roundUpAmount > 0) {
                // Call the repository to transfer the amount to the savings goal account
                val transferResult = repository.transferToGoal(
                    accountUId,
                    goalId,
                    transferUUid,
                    transferToGoalAccountRequest
                )
                return Result.success("Successfully transferred £${"%.2f".format(roundUpAmount)} to the savings goal.")
            } else {
                // If no round-up savings available, return failure
                Result.failure(Exception("No round-up savings this week."))
            }
        } catch (e: Exception) {
            // Handle any exceptions that occur during the process
            Result.failure(e)
        }
    }

}
