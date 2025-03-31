package com.ramesh.roundupsavingapp.presentation.roundup.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramesh.roundupsavingapp.constants.CURRENCY_UNIT
import com.ramesh.roundupsavingapp.data.models.accounts.Account
import com.ramesh.roundupsavingapp.data.models.saving_goal_list.SavingsGoal
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.Amount
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.TransferToGoalAccountRequest
import com.ramesh.roundupsavingapp.domain.usecase.RoundUpAndSaveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class SavingsViewModel @Inject constructor(
    private val roundUpAndSaveUseCase: RoundUpAndSaveUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()

    private val _savingsGoalAccount = MutableStateFlow<SavingsGoal?>(null)
    val savingsGoalAccount: StateFlow<SavingsGoal?> = _savingsGoalAccount.asStateFlow()

    private val _roundUpAmount = MutableStateFlow<Double>(0.0)
    val roundUpAmount: StateFlow<Double> = _roundUpAmount.asStateFlow()

    init {
        fetchAccountsAndGoals()
    }

    fun fetchAccountsAndGoals() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading

                val accountList = roundUpAndSaveUseCase.getAccounts()
                _accounts.value = accountList

                if (accountList.isNotEmpty()) {
                    val primaryAccount = accountList.firstOrNull() ?: return@launch

                    val goalAccount = roundUpAndSaveUseCase.getSavingsGoalsAccount(primaryAccount.accountUid)

                    _savingsGoalAccount.value = goalAccount

                    // Calling the function and passing changeSince for 1 week ago
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_YEAR, -7) // Subtract 7 days to get 1 week ago
                    val oneWeekAgo = calendar.time

                    // Format the date as ISO 8601 string (e.g., "2025-03-24T12:00:00.000Z")
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                    val formattedDate = dateFormat.format(oneWeekAgo)

                    val roundUpValue = roundUpAndSaveUseCase.calculateRoundUp(primaryAccount, changeSince = formattedDate)
                    _roundUpAmount.value = roundUpValue
                    _uiState.value = if (goalAccount == null) {
                        UiState.NoSavingsGoal(roundUpValue)
                    } else {
                        UiState.SavingsGoalAvailable(goalAccount, roundUpValue)
                    }
                } else {
                    _uiState.value = UiState.NoAccount
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }


    /*
    This function call api to add roundup money to savings goal account. This api needs client generated transfer id which is why we used UUID.randomUUID()
    */
    fun roundUpAndSave() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = roundUpAndSaveUseCase.transferToGoal(
                    _accounts.value.first().accountUid, _roundUpAmount.value, _savingsGoalAccount.value!!.savingsGoalUid,
                    "${UUID.randomUUID()}",
                    TransferToGoalAccountRequest(Amount(CURRENCY_UNIT, minorUnits = (_roundUpAmount.value*100).roundToInt()))
                )
                _uiState.value = UiState.Success("$result Successfully transferred the fund")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to transfer.")
            }
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    object NoAccount : UiState()
    data class NoSavingsGoal(val roundUpAmount: Double) : UiState()
    data class SavingsGoalAvailable(val savingsGoal: SavingsGoal, val roundUpAmount: Double) : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}