package com.steadycash.app.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.steadycash.app.data.SampleData
import com.steadycash.app.ui.screens.InsightsScreen
import com.steadycash.app.ui.screens.HomeScreen
import com.steadycash.app.ui.screens.LandingScreen
import com.steadycash.app.ui.theme.DarkBackground
import com.steadycash.app.ui.theme.PrimaryRed
import com.steadycash.app.ui.theme.TextPrimary
import com.steadycash.app.ui.theme.TextSecondary


const val ROUTE_LANDING = "LANDING"
const val ROUTE_HOME = "HOME"
const val ROUTE_INSIGHTS = "insights"
const val ROUTE_ADD = "add"

//

@Composable
fun SteadyCashNavHost(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    val showBottomBar = currentDestination?.route in listOf(ROUTE_HOME, ROUTE_INSIGHTS, ROUTE_ADD)

    androidx.compose.material3.Scaffold(
        containerColor = DarkBackground,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                BottomBar(
                    currentDestination = currentDestination,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(ROUTE_HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_LANDING,
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            composable(ROUTE_LANDING) {
                LandingScreen(
                    onBack = { navController.popBackStack() },
                    onEditKyc = { },
                    onProceed = {
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(ROUTE_LANDING) { inclusive = true }
                        }
                    }
                )
            }
            composable(ROUTE_HOME) {
                HomeScreen(
                    groups = SampleData.getTransactionGroups(),
                    onFullRecords = { }
                )
            }
            composable(ROUTE_INSIGHTS) {
                InsightsScreen()
            }
            composable(ROUTE_ADD) {
                PlaceholderScreen(title = "Add")
            }
        }
    }
}


@Composable
private fun BottomBar(
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
    val barItems = listOf(
        NavItem(ROUTE_HOME, "Home", Icons.Outlined.Home, isRedCenter = false),
        NavItem(ROUTE_ADD, "", Icons.Filled.AddCard, isRedCenter = true),
        NavItem(ROUTE_INSIGHTS, "Insights", Icons.Filled.BarChart, isRedCenter = false)
    )

    NavigationBar(
        containerColor = DarkBackground,
        contentColor = TextPrimary
    ) {
        barItems.forEach { item ->
            val isSelected = (currentDestination?.route == item.route)
            NavigationBarItem(
                icon = {
                    if (item.isRedCenter) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(PrimaryRed, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = "Add",
                                modifier = Modifier.size(24.dp),
                                tint = TextPrimary
                            )
                        }
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp),
                            tint = if (isSelected) PrimaryRed else TextSecondary
                        )
                    }
                },
                label = {
                    if (item.label.isNotEmpty()) {
                        Text(item.label, color = if (isSelected) PrimaryRed else TextSecondary)
                    }
                },
                selected = isSelected,
                onClick = { if (!item.isRedCenter) onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = PrimaryRed.copy(alpha = 0.2f),
                    selectedIconColor = PrimaryRed,
                    selectedTextColor = PrimaryRed,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}

private data class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val isRedCenter: Boolean = false
)

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize().fillMaxWidth().background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(title, color = TextPrimary)
    }
}
