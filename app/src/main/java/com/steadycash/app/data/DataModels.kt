package com.steadycash.app.data



data class Transaction(
    val id: String,
    val merchant: String,
    val description: String,
    val amount: Double,
    val time: String,
    val isIncome: Boolean,
    val iconType: TransactionIconType
)

enum class TransactionIconType {
    CLOCK,
    CAMERA,
    SUBSCRIPTION
}


data class TransactionGroup(
    val dateLabel: String,
    val transactions: List<Transaction>
)



data class ExpenseGroup(
    val name: String,
    val amount: Double,
    val percentage: Int
)


data class ExpenseGroupSummary(
    val name: String,
    val amount: Double,
    val percentage: Int,
    val iconLabel: String
)


data class DayAmount(val day: String, val amount: Double)
