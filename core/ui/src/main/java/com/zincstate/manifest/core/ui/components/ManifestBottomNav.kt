package com.zincstate.manifest.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.People
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.ripple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.zincstate.manifest.core.ui.theme.ManifestThemeTokens

enum class BottomNavTab(val icon: ImageVector, val label: String) {
    TRANSACTIONS(Icons.Default.CalendarMonth, "Transactions"),
    STATS(Icons.Default.BarChart, "Stats"),
    ACCOUNTS(Icons.Default.AccountBalanceWallet, "Accounts"),
    MORE(Icons.Default.MoreHoriz, "More")
}

/**
 * Floating pill-shaped bottom navigation bar with center FAB.
 */
@Composable
fun ManifestBottomNav(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    onFabClick: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it + 200 },
            animationSpec = tween(300)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it + 200 },
            animationSpec = tween(300)
        ),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            
            // Navigation pill
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                        spotColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f)
                ),
                border = BorderStroke(1.dp, ManifestThemeTokens.colors.border),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val tabs = BottomNavTab.entries
                    tabs.forEachIndexed { index, tab ->
                        if (index == 2) {
                            // Spacer for the center FAB
                            Spacer(modifier = Modifier.width(52.dp))
                        }
                        BottomNavItem(
                            tab = tab,
                            isSelected = tab == selectedTab,
                            onClick = { onTabSelected(tab) }
                        )
                    }
                }
            }

            // Center FAB — positioned above the pill
            val fabScale by animateFloatAsState(
                targetValue = if (isPressed) 1.2f else 1f,
                animationSpec = tween(100),
                label = "fabScale"
            )
            Card(
                modifier = Modifier
                    .offset(y = (-24).dp)
                    .size(52.dp)
                    .scale(fabScale)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = ripple(bounded = false, radius = 26.dp),
                        onClick = onFabClick
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(3.dp, MaterialTheme.colorScheme.background),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(
                    modifier = Modifier.size(52.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Transaction",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    tab: BottomNavTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = tween(200),
        label = "navScale"
    )

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(8.dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.label,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    ManifestThemeTokens.colors.textTertiary
                },
                modifier = Modifier.size(22.dp)
            )
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}
