package com.steadycash.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.steadycash.app.data.Transaction
import com.steadycash.app.data.TransactionGroup
import com.steadycash.app.data.TransactionIconType
import com.steadycash.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    groups: List<TransactionGroup>,
    onFullRecords: () -> Unit
) {

    var sheetVisible by remember { mutableStateOf(false) }
    var parallaxVisible by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    LaunchedEffect(Unit) {
        delay(80)
        sheetVisible = true
        delay(400)
        parallaxVisible = true
    }

    Scaffold(containerColor = DarkBackground) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val maxHeightPx = constraints.maxHeight.toFloat()
            val defaultParallaxPx = with(density) { (76.dp + 200.dp + 20.dp).roundToPx() }
            val parallaxHeightPx = defaultParallaxPx

            var sheetOffsetPx by remember { mutableStateOf(0f) }
            val progress = if (parallaxHeightPx > 0)
            {
                (sheetOffsetPx / parallaxHeightPx).coerceIn(0f, 1f)
            }
            else 0f


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { parallaxHeightPx.toDp() })
                    .graphicsLayer {
                        translationY = -sheetOffsetPx
                        alpha = 1f - progress
                    }
            ) {
                AnimatedVisibility(
                    visible = parallaxVisible,
                    enter = slideInVertically(
                        initialOffsetY = { -it / 4 },
                        animationSpec = tween(durationMillis = 500)
                    ) + fadeIn(animationSpec = tween(durationMillis = 500))
                ) {
                    Column {
                        WelcomeHeader()
                        RedCardBlock()
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }


            val sheetTopPx = parallaxHeightPx - sheetOffsetPx
            val sheetHeightPx = maxHeightPx - sheetTopPx

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, sheetTopPx.roundToInt()) }
                    .height(with(density) { sheetHeightPx.toDp() })
                    .background(DarkBackground, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            sheetOffsetPx = (sheetOffsetPx - dragAmount).coerceIn(0f, parallaxHeightPx.toFloat())
                        }
                    }
            ) {
                AnimatedVisibility(
                    visible = sheetVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(durationMillis = 500)
                    ) + fadeIn(
                        initialAlpha = 0f,
                        animationSpec = tween(durationMillis = 500)
                    )
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        HomeHeader(onFullRecords = onFullRecords)
                        Spacer(modifier = Modifier.height(8.dp))
                        TransactionListInSheet(
                            groups = groups,
                            sheetOffsetPx = sheetOffsetPx,
                            parallaxHeightPx = parallaxHeightPx,
                            maxHeightPx = maxHeightPx,
                            onSheetOffsetChange = { delta ->
                                sheetOffsetPx = (sheetOffsetPx + delta).coerceIn(0f, parallaxHeightPx.toFloat())
                            }
                        )
                    }
                }
            }
        }
    }
}

// --- Keeping the rest of your UI components identical ---

@Composable
private fun HomeHeader(onFullRecords: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Operations", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onFullRecords) {
            Text("Full records", color = TextSecondary, fontSize = 14.sp)
        }
    }
}

@Composable
private fun TransactionListInSheet(
    groups: List<TransactionGroup>,
    sheetOffsetPx: Float,
    parallaxHeightPx: Int,
    maxHeightPx: Float,
    onSheetOffsetChange: (Float) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(listState, sheetOffsetPx, parallaxHeightPx) {
                detectVerticalDragGestures { _, dragAmount ->
                    val atTop = listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                    if (atTop) { onSheetOffsetChange(-dragAmount) }
                }
            },
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groups.forEach { group ->
            item(key = "label_${group.dateLabel}") {
                Text(group.dateLabel, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(vertical = 4.dp))
            }
            items(items = group.transactions, key = { it.id }) { transaction ->
                TransactionRow(transaction = transaction)
            }
        }
    }
}

@Composable
private fun WelcomeHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(SurfaceCard), contentAlignment = Alignment.Center) {
            Text("?", color = TextSecondary, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.size(12.dp))
        Text("Welcome, Chief!", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { }) {
            Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = TextPrimary)
        }
    }
}

@Composable
private fun RedCardBlock() {
    val cardGradient = Brush.linearGradient(colors = listOf(PrimaryRed, Color(0xFFC62828), Color(0xFFB71C1C)))
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(200.dp).background(brush = cardGradient, shape = RoundedCornerShape(16.dp))) {
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("VISA", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                ShowInfoButton()
            }
            Spacer(modifier = Modifier.height(12.dp)); Box(modifier = Modifier.size(36.dp, 28.dp).background(Color(0xFFD4AF37), RoundedCornerShape(6.dp)))
            Spacer(modifier = Modifier.height(16.dp)); Text("CARD NUMBER", color = TextPrimary.copy(alpha = 0.8f), fontSize = 10.sp)
            Text("••••  ••••  ••••  9891", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Column { Text("EXP.", color = TextPrimary.copy(alpha = 0.8f), fontSize = 10.sp); Text("12/28", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium) }
                Column(horizontalAlignment = Alignment.End) { Text("CVV", color = TextPrimary.copy(alpha = 0.8f), fontSize = 10.sp); Text("123", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium) }
            }
        }
    }
}

@Composable
private fun ShowInfoButton() {
    Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.25f), contentColor = TextPrimary), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp), modifier = Modifier.height(36.dp), shape = RoundedCornerShape(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Visibility, null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.size(6.dp)); Text("Show info", fontSize = 12.sp)
        }
    }
}

@Composable
private fun TransactionRow(transaction: Transaction) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        TransactionIcon(iconType = transaction.iconType)
        Spacer(modifier = Modifier.size(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.merchant, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(transaction.description, color = TextSecondary, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            val amountText = if (transaction.isIncome) "+$${String.format("%.2f", transaction.amount)}" else "$${String.format("%.2f", transaction.amount)}"
            Text(amountText, color = if (transaction.isIncome) IncomeGreen else TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(transaction.time, color = TextSecondary, fontSize = 12.sp)
        }
    }
    Box(modifier = Modifier.fillMaxWidth().padding(start = 52.dp).height(1.dp).background(DividerColor))
}

@Composable
private fun TransactionIcon(iconType: TransactionIconType) {
    val iconChar = when (iconType) {
        TransactionIconType.CLOCK -> "◷"; TransactionIconType.CAMERA -> "◉"; TransactionIconType.SUBSCRIPTION -> "N"
    }
    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(SurfaceCard), contentAlignment = Alignment.Center) {
        Text(iconChar, color = TextSecondary, fontSize = 16.sp)
    }
}