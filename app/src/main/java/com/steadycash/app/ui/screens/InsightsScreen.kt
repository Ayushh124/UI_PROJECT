package com.steadycash.app.ui.screens



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.steadycash.app.data.SampleData
import com.steadycash.app.ui.theme.DarkBackground
import com.steadycash.app.ui.theme.PrimaryRed
import com.steadycash.app.ui.theme.SurfaceCard
import com.steadycash.app.ui.theme.TextPrimary
import com.steadycash.app.ui.theme.TextSecondary

private enum class InsightsPeriod { SevenDays, Month, Year }

@Composable
fun InsightsScreen() {
    var selectedPeriod by remember { mutableStateOf(InsightsPeriod.SevenDays) }
    var selectedYear by remember { mutableStateOf(2025) }
    var yearDropdownOpen by remember { mutableStateOf(false) }
    var selectedBarIndex by remember { mutableStateOf<Int?>(null) }

    val showYearSelector = selectedPeriod == InsightsPeriod.Month || selectedPeriod == InsightsPeriod.Year
   val years = (2016..2025).toList()

    // Amount and label to show above chart (total vs selected bar)
    val (displayAmount, displayLabel) = when (selectedPeriod) {
        InsightsPeriod.SevenDays -> {
            val data = SampleData.getDailyFor7Days()
            if (selectedBarIndex != null && selectedBarIndex!! in data.indices) {
                data[selectedBarIndex!!].amount to data[selectedBarIndex!!].day
            } else {
                data.sumOf { it.amount } to "EXPENDITURES"
            }
        }
        InsightsPeriod.Month -> {
            val data = SampleData.getMonthlyForYear(selectedYear)
            if (selectedBarIndex != null && selectedBarIndex!! in data.indices) {
                data[selectedBarIndex!!].second to data[selectedBarIndex!!].first
            } else {
                data.sumOf { it.second } to "EXPENDITURES"
            }
        }
        InsightsPeriod.Year -> {
            val data = SampleData.getLast10Years()
            if (selectedBarIndex != null && selectedBarIndex!! in data.indices) {
                data[selectedBarIndex!!].second to data[selectedBarIndex!!].first
            } else {
                data.sumOf { it.second } to "EXPENDITURES"
            }
        }
    }

    Scaffold(containerColor = DarkBackground) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // 1. Available Balance
            AvailableBalanceSection()
            Spacer(modifier = Modifier.height(24.dp))
            // 2. Period Tabs
            PeriodTabs(
                selectedPeriod = selectedPeriod,
                onPeriodSelected = { selectedPeriod = it; selectedBarIndex = null }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // 3. Year Selector (when Month or Year)
            if (showYearSelector) {
                YearSelector(
                    selectedYear = selectedYear,
                    years = years,
                    expanded = yearDropdownOpen,
                    onExpandClick = { yearDropdownOpen = true },
                    onDismiss = { yearDropdownOpen = false },
                    onYearSelected = { selectedYear = it; yearDropdownOpen = false; selectedBarIndex = null }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            // 4. Expenditures Block (total or selected bar amount + label)
            ExpendituresBlock(amount = displayAmount, label = displayLabel)
            Spacer(modifier = Modifier.height(24.dp))
            // 5. Bar Chart (7-day | 12-month | 10-year; clickable)
            when (selectedPeriod) {
                InsightsPeriod.SevenDays -> SevenDaysBarChart(
                    selectedBarIndex = selectedBarIndex,
                    onBarSelected = { selectedBarIndex = it }
                )
                InsightsPeriod.Month -> TwelveMonthsBarChart(
                    selectedYear = selectedYear,
                    selectedBarIndex = selectedBarIndex,
                    onBarSelected = { selectedBarIndex = it }
                )
                InsightsPeriod.Year -> TenYearsBarChart(
                    selectedBarIndex = selectedBarIndex,
                    onBarSelected = { selectedBarIndex = it }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            // 6. Expense Groups (4 cards; sum of 4 = displayAmount)
            ExpenseGroupsSection(totalExpenditure = displayAmount)
        }
    }
}

/** 1. Available Balance. */
@Composable
private fun AvailableBalanceSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "AVAILABLE BALANCE",
            color = TextSecondary,
            fontSize = 12.sp
        )
        Text(
            text = "$${String.format("%,.2f", SampleData.AVAILABLE_BALANCE)}",
            color = TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun PeriodTabs(
    selectedPeriod: InsightsPeriod,
    onPeriodSelected: (InsightsPeriod) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(
            "7 days" to InsightsPeriod.SevenDays,
            "Month" to InsightsPeriod.Month,
            "Year" to InsightsPeriod.Year
        ).forEach { (label, period) ->
            val isSelected = selectedPeriod == period
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (isSelected) PrimaryRed else SurfaceCard,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 12.dp)
                    .clickable { onPeriodSelected(period) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (isSelected) TextPrimary else TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}


@Composable
private fun YearSelector(
    selectedYear: Int,
    years: List<Int>,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    onDismiss: () -> Unit,
    onYearSelected: (Int) -> Unit
) {
    Box {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Year",
                color = TextSecondary,
                fontSize = 14.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = selectedYear.toString(),
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                IconButton(onClick = onExpandClick) {
                    Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = "Select year",
                        tint = TextPrimary
                    )
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            years.reversed().forEach { year ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = year.toString(),
                            color = TextPrimary
                        )
                    },
                    onClick = { onYearSelected(year) }
                )
            }
        }
    }
}


@Composable
private fun ExpendituresBlock(amount: Double, label: String) {
    Text(
        text = "$${String.format("%,.2f", amount)}",
        color = TextPrimary,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = label,
        color = TextSecondary,
        fontSize = 12.sp
    )
}


@Composable
private fun SevenDaysBarChart(
    selectedBarIndex: Int?,
    onBarSelected: (Int?) -> Unit
) {
    val data = SampleData.getDailyFor7Days()
    val maxAmount = data.maxOfOrNull { it.amount } ?: 1.0
    val maxBarHeightDp = 100

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().height((maxBarHeightDp + 24).dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEachIndexed { index, dayAmount ->
                val heightDp = (dayAmount.amount / maxAmount * maxBarHeightDp).toInt().coerceIn(8, maxBarHeightDp)
                val isHighlight = selectedBarIndex == index || (selectedBarIndex == null && index == data.indexOf(data.maxByOrNull { it.amount }!!))
                BarItem(
                    label = "$${dayAmount.amount.toInt()}",
                    heightDp = heightDp,
                    isRed = isHighlight,
                    onClick = { onBarSelected(if (selectedBarIndex == index) null else index) }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            data.forEach { day ->
                Text(
                    text = day.day,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}


@Composable
private fun TwelveMonthsBarChart(
    selectedYear: Int,
    selectedBarIndex: Int?,
    onBarSelected: (Int?) -> Unit
) {
    val data = SampleData.getMonthlyForYear(selectedYear)
    val maxAmount = data.maxOfOrNull { it.second } ?: 1.0
    val maxBarHeightDp = 100
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .height((maxBarHeightDp + 28).dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEachIndexed { index, (month, amount) ->
                val heightDp = (amount / maxAmount * maxBarHeightDp).toInt().coerceIn(8, maxBarHeightDp)
                val isHighlight = selectedBarIndex == index || (selectedBarIndex == null && index == 5)
                BarItem(
                    label = "$${amount.toInt()}",
                    heightDp = heightDp,
                    isRed = isHighlight,
                    barWidthDp = 28,
                    onClick = { onBarSelected(if (selectedBarIndex == index) null else index) }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            data.forEach { (month, _) ->
                Box(
                    modifier = Modifier.width(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = month,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}


@Composable
private fun TenYearsBarChart(
    selectedBarIndex: Int?,
    onBarSelected: (Int?) -> Unit
) {
    val data = SampleData.getLast10Years()
    val maxAmount = data.maxOfOrNull { it.second } ?: 1.0
    val maxBarHeightDp = 100
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .height((maxBarHeightDp + 28).dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEachIndexed { index, (year, amount) ->
                val heightDp = (amount / maxAmount * maxBarHeightDp).toInt().coerceIn(8, maxBarHeightDp)
                val isHighlight = selectedBarIndex == index || (selectedBarIndex == null && index == data.lastIndex)
                BarItem(
                    label = "$${amount.toInt()}",
                    heightDp = heightDp,
                    isRed = isHighlight,
                    barWidthDp = 36,
                    onClick = { onBarSelected(if (selectedBarIndex == index) null else index) }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.forEach { (year, _) ->
                Box(
                    modifier = Modifier.width(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = year,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BarItem(
    label: String,
    heightDp: Int,
    isRed: Boolean,
    barWidthDp: Int = 32,
    onClick: (() -> Unit)? = null
) {
    val barColor = if (isRed) PrimaryRed else SurfaceCard
    val clickModifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .then(clickModifier)
    ) {
        Text(
            text = label,
            color = if (isRed) PrimaryRed else TextSecondary,
            fontSize = 10.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .width(barWidthDp.dp)
                .height(heightDp.dp)
                .background(barColor, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        )
    }
}

@Composable
private fun ExpenseGroupsSection(totalExpenditure: Double) {
    val groups = SampleData.getExpenseGroupsForTotal(totalExpenditure)
    Text(
        text = "EXPENSE GROUPS",
        color = TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(12.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        groups.take(2).forEach { g ->
            ExpenseCard(
                modifier = Modifier.weight(1f),
                amount = String.format("$%,.2f", g.amount),
                percent = "${g.percentage}%",
                name = g.name,
                icon = g.iconLabel
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        groups.drop(2).forEach { g ->
            ExpenseCard(
                modifier = Modifier.weight(1f),
                amount = String.format("$%,.2f", g.amount),
                percent = "${g.percentage}%",
                name = g.name,
                icon = g.iconLabel
            )
        }
    }
}

@Composable
private fun ExpenseCard(
    modifier: Modifier = Modifier,
    amount: String,
    percent: String,
    name: String,
    icon: String
) {
    Card(
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = amount, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = percent, color = TextSecondary, fontSize = 14.sp)
            }
            Text(text = name, color = TextSecondary, fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(TextSecondary.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 16.sp)
            }
        }
    }
}
