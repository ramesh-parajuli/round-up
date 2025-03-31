package com.ramesh.roundupsavingapp.presentation.roundup.create_goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramesh.roundupsavingapp.constants.CURRENCY_UNIT
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.CreateSavingsGoalRequest
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.Target
import com.ramesh.roundupsavingapp.domain.usecase.CreateSavingsGoalUseCase
import com.ramesh.roundupsavingapp.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateSavingsGoalViewModel @Inject constructor(
    private val createSavingsGoalUseCase: CreateSavingsGoalUseCase
) : ViewModel() {

    private val _isGoalCreated = MutableStateFlow<Boolean?>(null)
    val isGoalCreated: StateFlow<Boolean?> = _isGoalCreated

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    private var accountUid: String = ""

    fun setAccountId(accountUid: String) {
        this.accountUid = accountUid
    }

    fun createSavingsGoal(goalName: String, targetAmount: Double) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading // Show loading state
            try {
                val targetMinorAmount = (targetAmount * 100).toInt()
                val result = createSavingsGoalUseCase.invoke(
                    accountUid,
                    CreateSavingsGoalRequest(
                        "imageEncoded",
                        CURRENCY_UNIT,
                        goalName,
                        Target(CURRENCY_UNIT, targetMinorAmount)
                    )
                )

                // If success, update UI state
                _isGoalCreated.value = true
                _uiState.value = UiState.Success("Savings goal created successfully!")

            } catch (e: Exception) {
                _isGoalCreated.value = false
                _uiState.value = UiState.Error("Failed to create goal: ${e.message}")
            }
        }
    }
}
