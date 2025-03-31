package com.ramesh.roundupsavingapp.data.models.feed

data class FeedItem(
    val amount: Amount,
    val batchPaymentDetails: Any,
    val categoryUid: String,
    val counterPartyName: String,
    val counterPartySubEntityIdentifier: String,
    val counterPartySubEntityName: String,
    val counterPartySubEntitySubIdentifier: String,
    val counterPartySubEntityUid: String,
    val counterPartyType: String,
    val counterPartyUid: String,
    val country: String,
    val direction: String,
    val feedItemUid: String,
    val hasAttachment: Boolean,
    val hasReceipt: Boolean,
    val reference: String,
    val settlementTime: String,
    val source: String,
    val sourceAmount: SourceAmount,
    val spendingCategory: String,
    val status: String,
    val transactingApplicationUserUid: String,
    val transactionTime: String,
    val updatedAt: String
)