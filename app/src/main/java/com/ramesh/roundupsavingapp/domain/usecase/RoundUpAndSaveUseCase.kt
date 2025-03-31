package com.ramesh.roundupsavingapp.domain.usecase

import com.ramesh.roundupsavingapp.data.models.accounts.Account
import com.ramesh.roundupsavingapp.data.models.saving_goal_list.SavingsGoal
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.TransferToGoalAccountRequest
import com.ramesh.roundupsavingapp.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class RoundUpAndSaveUseCase @Inject constructor(
    private val repository: SavingsRepository,

) {

    suspend fun getAccounts(): List<Account> {
        return repository.getPrimaryAccount().getOrThrow()
    }

    suspend fun getSavingsGoalsAccount(accountId: String): SavingsGoal? {
        val savingsGoalsResponse = repository.getSavingsGoalsAccount(accountId)
        return savingsGoalsResponse.getOrThrow().savingsGoalList.firstOrNull()
    }

suspend fun calculateRoundUp(account: Account, changeSince: String): Double {
    val transactions = repository.getTransactions(account.accountUid, account.defaultCategory, changeSince).first()

    // Get the current date (compatible with lower API levels using Calendar)
    val calendar = Calendar.getInstance()
    val today = calendar.time

    // Get the date 7 days ago
    calendar.add(Calendar.DAY_OF_YEAR, -7)
    val weekAgo = calendar.time

    // Create SimpleDateFormat for parsing the date string
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")

    // Filtering the transactions to include only those from the last 7 days
    val recentTransactions = transactions.feedItems.filter {
        // Parse the 'transactionTime' to Date
        val transactionDate = dateFormat.parse(it.transactionTime)

        // Check if the transaction date is within the last 7 days
        val isWithinLastWeek = transactionDate != null && transactionDate.after(weekAgo) && !transactionDate.after(today)
        isWithinLastWeek
    }

    // Calculating the round-up amount for filtered transactions
    val roundUpAmount = recentTransactions.sumOf {
        val amountValue = BigDecimal(it.amount.minorUnits).divide(BigDecimal(100)) // Convert to BigDecimal first
        val ceilValue = amountValue.setScale(0, RoundingMode.CEILING) // Round up to nearest whole number
        val difference = ceilValue.subtract(amountValue).setScale(2, RoundingMode.HALF_UP) // Round-Up calculation
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
