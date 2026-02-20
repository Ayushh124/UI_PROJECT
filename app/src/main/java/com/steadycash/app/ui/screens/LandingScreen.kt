package com.steadycash.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.steadycash.app.R
import com.steadycash.app.ui.theme.DarkBackground
import com.steadycash.app.ui.theme.PrimaryRed
import com.steadycash.app.ui.theme.SurfaceCard
import com.steadycash.app.ui.theme.TextPrimary
import com.steadycash.app.ui.theme.TextSecondary

@Composable
fun LandingScreen(
    onBack: () -> Unit,
    onEditKyc: () -> Unit,
    onProceed: () -> Unit
)  {
    Scaffold(

        topBar = { StableTopBar(onBack = onBack) },
        containerColor = DarkBackground
    ) { paddingValues ->
        var contentVisible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { contentVisible = true }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = contentVisible,
                enter = slideInVertically(initialOffsetY = { it / 4 }) + fadeIn()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    LandingCard()
                    Spacer(modifier = Modifier.weight(1f))
                    PageDots()
                    Spacer(modifier = Modifier.height(16.dp))
                    LandingButtons(onEditKyc = onEditKyc, onProceed = onProceed)
                }
            }
        }
    }
}

@Composable
private fun StableTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(DarkBackground)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = TextPrimary
            )
        }
        Text(
            text = "Sign up",
            color = TextSecondary,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "SteadyCash",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

@Composable
private fun LandingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "To confirm your account, we'll need you to give us the following information",
                color = TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.red_cross_icon),
                contentDescription = "Success Illustration",
                modifier = Modifier.size(160.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Thank you for submitting your business information",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your KYC has been verified and approved. You can now proceed with the next steps to start using our services.",
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PageDots() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SingleDot(active = true)
        Spacer(modifier = Modifier.width(8.dp))
        SingleDot(active = false)
        Spacer(modifier = Modifier.width(8.dp))
        SingleDot(active = false)
    }
}

@Composable
private fun SingleDot(active: Boolean) {
    val color = if (active) TextPrimary else TextSecondary.copy(alpha = 0.3f)
    val size = if (active) 10.dp else 8.dp
    Box(
        modifier = Modifier
            .size(size)
            .background(color = color, shape = CircleShape)
    )
}

@Composable
private fun LandingButtons(onEditKyc: () -> Unit, onProceed: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onEditKyc,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, TextSecondary.copy(alpha = 0.2f)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
        ) {
            Text("Edit KYC", fontWeight = FontWeight.Medium)
        }

        Button(
            onClick = onProceed,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryRed,
                contentColor = TextPrimary
            )
        ) {
            Text("Proceed", fontWeight = FontWeight.Bold)
        }
    }
}