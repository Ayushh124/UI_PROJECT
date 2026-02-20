package com.steadycash.app.data

object SampleData {

    const val AVAILABLE_BALANCE = 7953.00

    /** 7 days (Sun–Sat) for Insights "7 days" tab. */
    fun getDailyFor7Days(): List<DayAmount> = listOf(
        DayAmount("Sun", 280.0),
        DayAmount("Mon", 180.0),
        DayAmount("Tue", 664.0),
        DayAmount("Wed", 275.0),
        DayAmount("Thu", 80.0),
        DayAmount("Fri", 312.0),
        DayAmount("Sat", 274.0)
    )

    /** 12 months (Jan–Dec) for a given year. */
    fun getMonthlyForYear(year: Int): List<Pair<String, Double>> = listOf(
        "Jan" to 12109.0,
        "Feb" to 7863.0,
        "Mar" to 11323.0,
        "Apr" to 12895.0,
        "May" to 9593.0,
        "Jun" to 14311.0,
        "Jul" to 4717.0,
        "Aug" to 8649.0,
        "Sep" to 13839.0,
        "Oct" to 8178.0,
        "Nov" to 10694.0,
        "Dec" to 15726.0
    )


    fun getLast10Years(): List<Pair<String, Double>> = listOf(
        2016 to 125000.0, 2017 to 142000.0, 2018 to 118000.0, 2019 to 15600.0, 2020 to 13900.0,
        2021 to 16200.0, 2022 to 14800.0, 2023 to 129897.0, 2024 to 115000.0, 2025 to 98500.0
    ).map { (y, a) -> y.toString() to a }


    fun getExpenseGroupsForTotal(total: Double): List<ExpenseGroupSummary> {
        val pcts = listOf(13, 28, 58, 1)
        val names = listOf("Lights", "Car", "Transfers", "Recurring payments")
        val icons = listOf("💡", "📄", "◉", "N")
        val amounts = mutableListOf<Double>()
        var sum = 0.0
        for (i in 0..2) {
            val amt = total * pcts[i] / 100.0
            amounts.add(amt)
            sum += amt
        }
        amounts.add((total - sum).coerceAtLeast(0.0))
        return names.indices.map { i ->
            ExpenseGroupSummary(names[i], amounts[i], pcts[i], icons[i])
        }
    }

    fun getTransactionGroups(): List<TransactionGroup> = listOf(
        TransactionGroup(
            dateLabel = "TODAY",
            transactions = listOf(
                Transaction("1", "ram", "Hill Mall", 432.29, "1:22PM", false, TransactionIconType.CLOCK),
                Transaction("2", "shyam", "Thanks for the dinner!", 128.00, "12:32PM", true, TransactionIconType.CAMERA),
                Transaction("3", "Sam", "Netflix", 18.00, "12:45PM", false, TransactionIconType.SUBSCRIPTION),
                Transaction("4", "Damien ", "You won!", 200.00, "10:22AM", true, TransactionIconType.CAMERA),
                Transaction("5", "ankit", "Present for Kelly", 234.00, "12:22PM", false, TransactionIconType.CLOCK),
                Transaction("6", "Ravi", "Payment received", 200.00, "1:34PM", true, TransactionIconType.CAMERA),
                Transaction("7", "utsav", "Online store", 1080.00, "2:56PM", false, TransactionIconType.CLOCK)
            )
        ),
        TransactionGroup(
            dateLabel = "YESTERDAY",
            transactions = listOf(
                Transaction("8", "Starbucks", "Coffee", 5.50, "9:15AM", false, TransactionIconType.CLOCK),
                Transaction("9", "practo", "Reimbursement", 50.00, "2:00PM", true, TransactionIconType.CAMERA)
            )
        ),
        TransactionGroup(
            dateLabel = "Feb 21",
            transactions = listOf(
                Transaction("10", "Amazon", "Shopping", 89.99, "7:30PM", false, TransactionIconType.CLOCK)
            )
        )
    )
}
