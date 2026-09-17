package com.zincstate.manifest.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zincstate.manifest.core.ui.components.BottomNavTab
import com.zincstate.manifest.core.ui.components.ManifestBottomNav
import com.zincstate.manifest.feature.accounts.AccountsScreen
import com.zincstate.manifest.feature.accounts.AccountDetailsScreen
import com.zincstate.manifest.feature.addedit.AddEditScreen
import com.zincstate.manifest.feature.budgets.BudgetsScreen
import com.zincstate.manifest.feature.goals.GoalsScreen
import com.zincstate.manifest.feature.settings.MoreScreen
import com.zincstate.manifest.feature.settings.PrivacySecurityScreen
import com.zincstate.manifest.feature.settings.NotificationsScreen
import com.zincstate.manifest.feature.stats.StatsScreen
import com.zincstate.manifest.feature.subscriptions.SubscriptionsScreen
import com.zincstate.manifest.feature.transactions.TransactionsScreen
import com.zincstate.manifest.feature.transactions.DebtsScreen

object Destinations {
    const val TRANSACTIONS = "transactions"
    const val STATS = "stats"
    const val ACCOUNTS = "accounts"
    const val ACCOUNT_DETAILS = "account_details/{accountId}"
    const val MORE = "more"
    const val DEBTS = "debts"
    const val ADD_EDIT = "add_edit?transactionId={transactionId}"
    
    // Sub-screens
    const val BUDGETS = "budgets"
    const val GOALS = "goals"
    const val SUBSCRIPTIONS = "subscriptions"
    const val SMS_IMPORT = "sms-import"

    fun addEditRoute(transactionId: String? = null): String {
        return if (transactionId != null) "add_edit?transactionId=$transactionId" else "add_edit"
    }

    fun accountDetailsRoute(accountId: String): String {
        return "account_details/$accountId"
    }
}

@Composable
fun ManifestNavGraph(
    navController: NavHostController = rememberNavController(),
    isQuickAdd: Boolean = false
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavVisible = currentRoute in listOf(
        Destinations.TRANSACTIONS,
        Destinations.STATS,
        Destinations.ACCOUNTS,
        Destinations.MORE
    )

    val currentTab = when (currentRoute) {
        Destinations.TRANSACTIONS -> BottomNavTab.TRANSACTIONS
        Destinations.STATS -> BottomNavTab.STATS
        Destinations.ACCOUNTS -> BottomNavTab.ACCOUNTS
        Destinations.MORE -> BottomNavTab.MORE
        else -> BottomNavTab.TRANSACTIONS
    }

    Scaffold(
        bottomBar = {
            ManifestBottomNav(
                selectedTab = currentTab,
                isVisible = bottomNavVisible,
                onTabSelected = { tab ->
                    val route = when (tab) {
                        BottomNavTab.TRANSACTIONS -> Destinations.TRANSACTIONS
                        BottomNavTab.STATS -> Destinations.STATS
                        BottomNavTab.ACCOUNTS -> Destinations.ACCOUNTS
                        BottomNavTab.MORE -> Destinations.MORE
                    }
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onFabClick = {
                    navController.navigate(Destinations.addEditRoute())
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                // We handle our own bottom padding for screens with bottom nav because it's a floating pill
                // but Scaffold requires us to use innerPadding or we get a warning.
                // Since our bottom nav is absolute/floating, we don't strictly need Scaffold's padding
                // except maybe for screens WITHOUT bottom nav.
        ) {
            NavHost(
                navController = navController,
                startDestination = if (isQuickAdd) Destinations.ADD_EDIT else Destinations.TRANSACTIONS,
                enterTransition = {
                    fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                        initialOffsetX = { 300 },
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                        targetOffsetX = { -300 },
                        animationSpec = tween(300)
                    )
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                        initialOffsetX = { -300 },
                        animationSpec = tween(300)
                    )
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                        targetOffsetX = { 300 },
                        animationSpec = tween(300)
                    )
                }
            ) {
                composable(Destinations.TRANSACTIONS) {
                    TransactionsScreen(
                        onTransactionClick = { id ->
                            navController.navigate(Destinations.addEditRoute(id))
                        }
                    )
                }
                
                composable(Destinations.STATS) {
                    StatsScreen()
                }
                
                composable(Destinations.ACCOUNTS) {
                    AccountsScreen(
                        onAccountClick = { id ->
                            navController.navigate(Destinations.accountDetailsRoute(id))
                        }
                    )
                }

                composable(
                    route = Destinations.ACCOUNT_DETAILS,
                    arguments = listOf(navArgument("accountId") { type = NavType.StringType })
                ) {
                    AccountDetailsScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
                
                composable(Destinations.MORE) {
                    MoreScreen(
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }

                composable(Destinations.DEBTS) {
                    DebtsScreen(onBackClick = { navController.popBackStack() })
                }

                composable(
                    route = Destinations.ADD_EDIT,
                    arguments = listOf(
                        navArgument("transactionId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        }
                    )
                ) {
                    AddEditScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // Stubs for More screen items
                composable(Destinations.BUDGETS) {
                    BudgetsScreen(onBackClick = { navController.popBackStack() })
                }
                composable(Destinations.GOALS) {
                    GoalsScreen(onBackClick = { navController.popBackStack() })
                }
                composable(Destinations.SUBSCRIPTIONS) {
                    SubscriptionsScreen(onBackClick = { navController.popBackStack() })
                }
                composable("privacy-security") {
                    PrivacySecurityScreen(onBackClick = { navController.popBackStack() })
                }
                composable("notifications") {
                    NotificationsScreen(onBackClick = { navController.popBackStack() })
                }
                composable(Destinations.SMS_IMPORT) {
                    com.zincstate.manifest.feature.sms.SmsImportScreen(onNavigateBack = { navController.popBackStack() })
                }
            }
        }
    }
}
