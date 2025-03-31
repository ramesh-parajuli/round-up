package com.ramesh.roundupsavingapp.domain.usecase

import com.ramesh.roundupsavingapp.data.models.savinggoal_request.CreateSavingsGoalRequest
import com.ramesh.roundupsavingapp.domain.repository.SavingsRepository
import javax.inject.Inject

class CreateSavingsGoalUseCase  @Inject constructor(
    private val repository: SavingsRepository
) {
    suspend operator fun invoke(accountId: String ,request: CreateSavingsGoalRequest) {
        repository.createSavingsGoal(accountId, request)
    }
}