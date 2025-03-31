package com.ramesh.roundupsavingapp.domain.usecase

import com.ramesh.roundupsavingapp.data.models.accounts.Account
import com.ramesh.roundupsavingapp.data.models.feed.Amount
import com.ramesh.roundupsavingapp.data.models.feed.FeedItem
import com.ramesh.roundupsavingapp.data.models.feed.FeedItems
import com.ramesh.roundupsavingapp.data.models.feed.SourceAmount
import com.ramesh.roundupsavingapp.data.models.savinggoal_request.TransferToGoalAccountRequest
import com.ramesh.roundupsavingapp.domain.repository.SavingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class RoundUpAndSaveUseCaseTest {

    private lateinit var useCase: RoundUpAndSaveUseCase
    private val repository: SavingsRepository = mockk()

    @Before
    fun setUp() {
        useCase = RoundUpAndSaveUseCase(repository)
    }

    @Test
    fun `test calculateRoundUp returns correct amount for last week`() = runBlocking {
        // Arrange: Prepare mock data
        val account = Account(
            accountType = "Checking",
            accountUid = "123",
            createdAt = "2025-01-01T00:00:00.000Z",
            currency = "GBP",
            defaultCategory = "Default",
            name = "Test Account"
        )

        val transaction1 = FeedItem(
            amount = Amount(currency = "GBP", minorUnits = 210), // $2.00
            batchPaymentDetails = Any(),
            categoryUid = "cat1",
            counterPartyName = "Counterparty 1",
            counterPartySubEntityIdentifier = "sub1",
            counterPartySubEntityName = "Entity 1",
            counterPartySubEntitySubIdentifier = "subId1",
            counterPartySubEntityUid = "subUid1",
            counterPartyType = "Type1",
            counterPartyUid = "counterpartyUid1",
            country = "AU",
            direction = "Debit",
            feedItemUid = "feed1",
            hasAttachment = false,
            hasReceipt = false,
            reference = "Ref1",
            settlementTime = "2025-03-25T12:00:00.000Z",
            source = "App",
            sourceAmount = SourceAmount( "GBP", 210),
            spendingCategory = "Food",
            status = "Completed",
            transactingApplicationUserUid = "user1",
            transactionTime = "2025-03-24T12:00:00.000Z", // $2.00 transaction
            updatedAt = "2025-03-24T12:05:00.000Z"
        )

        val transaction2 = FeedItem(
            amount = Amount(currency = "GBP", minorUnits = 500), // $5.00
            batchPaymentDetails = Any(),
            categoryUid = "cat2",
            counterPartyName = "Counterparty 2",
            counterPartySubEntityIdentifier = "sub2",
            counterPartySubEntityName = "Entity 2",
            counterPartySubEntitySubIdentifier = "subId2",
            counterPartySubEntityUid = "subUid2",
            counterPartyType = "Type2",
            counterPartyUid = "counterpartyUid2",
            country = "AU",
            direction = "Debit",
            feedItemUid = "feed2",
            hasAttachment = false,
            hasReceipt = false,
            reference = "Ref2",
            settlementTime = "2025-03-26T12:00:00.000Z",
            source = "App",
            sourceAmount = SourceAmount( "GBP", 500),
            spendingCategory = "Entertainment",
            status = "Completed",
            transactingApplicationUserUid = "user2",
            transactionTime = "2025-03-25T12:00:00.000Z", // $5.00 transaction
            updatedAt = "2025-03-25T12:05:00.000Z"
        )

        val transactions = FeedItems(feedItems = listOf(transaction1, transaction2))

        // Mock the repository call to return a flow of transactions for the account
        coEvery { repository.getTransactions(any(), any(), any()) } returns flowOf(transactions)

        // Act: Call the use case function with changeSince as 7 days ago
        val result = useCase.calculateRoundUp(account, "2025-03-24T12:00:00.000Z") // 1 week ago

        // Assert: Check the result
        val expectedRoundUpAmount = 0.90 + 0.00 // Calculated round-up based on $5.00 and $2.00
        assertEquals(expectedRoundUpAmount, result, 0.90)
    }

    @Test
    fun `test transferToGoal transfers roundUpAmount correctly`() = runBlocking {
        // Arrange: Prepare mock data
        val accountUId = "123"
        val goalId = "goal123"
        val transferUUid = "transfer123"
        val roundUpAmount = 3.50
        val transferRequest = TransferToGoalAccountRequest(amount = com.ramesh.roundupsavingapp.data.models.savinggoal_request.Amount("GBP", 350))

        // Mock the repository to return a successful transfer
        coEvery { repository.transferToGoal(any(), any(), any(), any()) } returns Result.success("Successfully transferred £3.50 to the savings goal.")

        // Act: Call the use case function to transfer the round-up amount
        val result = useCase.transferToGoal(
            accountUId,
            roundUpAmount,
            goalId,
            transferUUid,
            transferRequest
        )

        // Assert: Verify that the result matches the expected success message
        assertEquals("Successfully transferred £3.50 to the savings goal.", result.getOrNull())

        // Verify that the transfer function was called once
        coVerify { repository.transferToGoal(accountUId, goalId, transferUUid, transferRequest) }
    }

    @Test
    fun `test transferToGoal returns failure when roundUpAmount is zero`() = runBlocking {
        // Arrange: Prepare mock data for zero round-up amount
        val accountUId = "123"
        val goalId = "goal123"
        val transferUUid = "transfer123"
        val roundUpAmount = 0.0
        val transferRequest = TransferToGoalAccountRequest(amount = com.ramesh.roundupsavingapp.data.models.savinggoal_request.Amount("GBP", 0))

        // Act: Call the use case function to transfer the round-up amount
        val result = useCase.transferToGoal(
            accountUId,
            roundUpAmount,
            goalId,
            transferUUid,
            transferRequest
        )

        // Assert: Check that the result is a failure due to no round-up savings
        assert(result.isFailure)
        assertEquals("No round-up savings this week.", result.exceptionOrNull()?.message)
    }

}