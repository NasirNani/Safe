package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.ui.theme.*

enum class AppTab(
    val id: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
) {
    HOME("home", Icons.Filled.Home, Icons.Outlined.Home),
    LAWYERS("lawyers", Icons.Filled.People, Icons.Outlined.People),
    CHAT("chat", Icons.Filled.Chat, Icons.Outlined.Chat),
    CONTRACTS("contracts", Icons.Filled.Description, Icons.Outlined.Description),
    RULINGS("rulings", Icons.Filled.Balance, Icons.Outlined.Balance),
    PROFILE("profile", Icons.Filled.Person, Icons.Outlined.Person);

    fun getLabel(language: AppLanguage): String {
        return when (this) {
            HOME -> AppStrings.navHome.get(language)
            LAWYERS -> AppStrings.navLawyers.get(language)
            CHAT -> AppStrings.navChat.get(language)
            CONTRACTS -> AppStrings.navContracts.get(language)
            RULINGS -> AppStrings.navRulings.get(language)
            PROFILE -> AppStrings.navProfile.get(language)
        }
    }
}

@Composable
fun AppBottomNavBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = Navy900,
        contentColor = Color.White,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars,
        modifier = modifier.testTag("app_bottom_nav_bar")
    ) {
        AppTab.values().forEach { tab ->
            val isSelected = selectedTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.filledIcon else tab.outlinedIcon,
                        contentDescription = tab.getLabel(language),
                        tint = if (isSelected) GoldLight else TextOnNavySecondary,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = tab.getLabel(language),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.sp
                        ),
                        color = if (isSelected) GoldLight else TextOnNavySecondary,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Navy700,
                    selectedIconColor = GoldLight,
                    selectedTextColor = GoldLight,
                    unselectedIconColor = TextOnNavySecondary,
                    unselectedTextColor = TextOnNavySecondary
                ),
                modifier = Modifier.testTag("nav_tab_${tab.id}")
            )
        }
    }
}
