package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.ui.theme.*

@Composable
fun AppTopBar(
    currentLanguage: AppLanguage,
    isDarkMode: Boolean = false,
    unreadNotificationsCount: Int = 0,
    onOpenNotifications: () -> Unit = {},
    onToggleLanguage: () -> Unit,
    onToggleTheme: () -> Unit = {},
    onCallHotline: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isDarkMode) Navy950 else Navy900,
        tonalElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("app_top_bar")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Brand Logo & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Navy800, Navy700)
                                )
                            )
                            .border(1.dp, GoldPrimary, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.umbrella_logo),
                            contentDescription = "Umbrella Safe Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "مظـلـة" else "UMBRELLA",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GoldPrimary.copy(alpha = 0.2f))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "حماية" else "SAFE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    ),
                                    color = GoldLight
                                )
                            }
                        }
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "المنصة المعتمدة بمصر" else "Certified Legal Network",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                            color = TextOnNavySecondary,
                            maxLines = 1
                        )
                    }
                }

                // Actions: Notification Bell, Emergency Hotline & Language Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Notification Bell Icon with Badge Count
                    Surface(
                        onClick = onOpenNotifications,
                        shape = CircleShape,
                        color = Navy800,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (unreadNotificationsCount > 0) GoldBorder else BorderSubtle
                        ),
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("top_bar_notifications_btn")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = if (unreadNotificationsCount > 0) GoldPrimary else TextOnNavySecondary,
                                modifier = Modifier.size(18.dp)
                            )

                            // Unread Badge Pill
                            if (unreadNotificationsCount > 0) {
                                Surface(
                                    shape = CircleShape,
                                    color = CrimsonEmergency,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = 2.dp, end = 2.dp)
                                        .size(14.dp)
                                        .testTag("top_bar_notif_badge")
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = if (unreadNotificationsCount > 9) "9+" else "$unreadNotificationsCount",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Emergency Hotline Button
                    Surface(
                        onClick = onCallHotline,
                        shape = RoundedCornerShape(18.dp),
                        color = CrimsonEmergency.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonEmergency.copy(alpha = 0.6f)),
                        modifier = Modifier.testTag("top_bar_hotline_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call Helpline",
                                tint = CrimsonEmergency,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "١٦٩٩٩",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = Color.White
                            )
                        }
                    }

                    // Theme Switcher Toggle
                    Surface(
                        onClick = onToggleTheme,
                        shape = CircleShape,
                        color = Navy800,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("top_bar_theme_toggle_btn")
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Language Switcher Toggle
                    Surface(
                        onClick = onToggleLanguage,
                        shape = RoundedCornerShape(18.dp),
                        color = Navy800,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder.copy(alpha = 0.4f)),
                        modifier = Modifier.testTag("top_bar_lang_toggle")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Toggle Language",
                                tint = GoldPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "EN" else "عربي",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = GoldLight
                            )
                        }
                    }
                }
            }
        }
    }
}
