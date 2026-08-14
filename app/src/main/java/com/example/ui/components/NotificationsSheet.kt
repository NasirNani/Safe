package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsSheet(
    notifications: List<AppNotification>,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onNotificationClick: (AppNotification) -> Unit,
    onMarkAllAsRead: () -> Unit,
    onOpenLawyerProfile: ((String) -> Unit)? = null,
    onOpenChat: (() -> Unit)? = null
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val unreadCount = remember(notifications) { notifications.count { !it.isRead } }

    val filteredNotifications = remember(notifications, selectedFilter) {
        when (selectedFilter) {
            "UNREAD" -> notifications.filter { !it.isRead }
            "CASES" -> notifications.filter { it.type == NotificationType.CASE_STATUS_CHANGE }
            "CHAT" -> notifications.filter { it.type == NotificationType.CHAT_REPLY }
            else -> notifications
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = SurfaceCard,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = BorderMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        },
        modifier = Modifier.testTag("notifications_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Navy100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = Navy900,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = AppStrings.notifications.get(language),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp
                                    ),
                                    color = Navy950
                                )

                                if (unreadCount > 0) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = CrimsonEmergency,
                                        modifier = Modifier.testTag("notifications_unread_badge")
                                    ) {
                                        Text(
                                            text = "$unreadCount ${AppStrings.newNotificationTag.get(language)}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            ),
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = AppStrings.notificationsSubtitle.get(language),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = TextSecondary
                            )
                        }
                    }

                    // Mark all as read button
                    if (unreadCount > 0) {
                        TextButton(
                            onClick = onMarkAllAsRead,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("mark_all_read_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = null,
                                tint = Gold900,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = AppStrings.markAllRead.get(language),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Gold900
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Filter Tabs
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedFilter == "ALL",
                            onClick = { selectedFilter = "ALL" },
                            label = {
                                Text(
                                    text = "${AppStrings.allNotifications.get(language)} (${notifications.size})",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (selectedFilter == "ALL") FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Navy850,
                                selectedLabelColor = GoldLight,
                                containerColor = OffWhite,
                                labelColor = TextSecondary
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = selectedFilter == "UNREAD",
                            onClick = { selectedFilter = "UNREAD" },
                            label = {
                                Text(
                                    text = "${AppStrings.unreadOnly.get(language)} ($unreadCount)",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (selectedFilter == "UNREAD") FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Navy850,
                                selectedLabelColor = GoldLight,
                                containerColor = OffWhite,
                                labelColor = TextSecondary
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = selectedFilter == "CASES",
                            onClick = { selectedFilter = "CASES" },
                            label = {
                                Text(
                                    text = AppStrings.caseUpdatesTab.get(language),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (selectedFilter == "CASES") FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Navy850,
                                selectedLabelColor = GoldLight,
                                containerColor = OffWhite,
                                labelColor = TextSecondary
                            )
                        )
                    }

                    item {
                        FilterChip(
                            selected = selectedFilter == "CHAT",
                            onClick = { selectedFilter = "CHAT" },
                            label = {
                                Text(
                                    text = AppStrings.chatRepliesTab.get(language),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (selectedFilter == "CHAT") FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Navy850,
                                selectedLabelColor = GoldLight,
                                containerColor = OffWhite,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }
            }

            HorizontalDivider(color = BorderSubtle, thickness = 1.dp)

            // Notifications List
            if (filteredNotifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Navy50),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Text(
                            text = AppStrings.noNotifications.get(language),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = TextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 450.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredNotifications, key = { it.id }) { notif ->
                        NotificationCardItem(
                            notification = notif,
                            language = language,
                            onClick = {
                                onNotificationClick(notif)
                            },
                            onOpenLawyerProfile = onOpenLawyerProfile,
                            onOpenChat = onOpenChat
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCardItem(
    notification: AppNotification,
    language: AppLanguage,
    onClick: () -> Unit,
    onOpenLawyerProfile: ((String) -> Unit)?,
    onOpenChat: (() -> Unit)?
) {
    val isUnread = !notification.isRead

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnread) Navy50.copy(alpha = 0.6f) else SurfaceCard
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isUnread) GoldBorder else BorderSubtle
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("notification_item_${notification.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Type Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        when (notification.type) {
                            NotificationType.CHAT_REPLY -> Navy100
                            NotificationType.CASE_STATUS_CHANGE -> EmeraldSuccess.copy(alpha = 0.15f)
                            NotificationType.CONSULTATION_CONFIRMED -> GoldPale
                            NotificationType.LEGAL_ALERT -> CrimsonEmergency.copy(alpha = 0.12f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (notification.type) {
                        NotificationType.CHAT_REPLY -> Icons.Default.ChatBubble
                        NotificationType.CASE_STATUS_CHANGE -> Icons.Default.Gavel
                        NotificationType.CONSULTATION_CONFIRMED -> Icons.Default.EventAvailable
                        NotificationType.LEGAL_ALERT -> Icons.Default.Campaign
                    },
                    contentDescription = null,
                    tint = when (notification.type) {
                        NotificationType.CHAT_REPLY -> Navy850
                        NotificationType.CASE_STATUS_CHANGE -> EmeraldSuccess
                        NotificationType.CONSULTATION_CONFIRMED -> Gold900
                        NotificationType.LEGAL_ALERT -> CrimsonEmergency
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.getTitle(language),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (isUnread) FontWeight.Bold else FontWeight.SemiBold,
                            fontSize = 13.sp
                        ),
                        color = Navy950,
                        modifier = Modifier.weight(1f)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isUnread) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(GoldPrimary)
                            )
                        }
                        Text(
                            text = notification.getTimeAgo(language),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = TextMuted
                        )
                    }
                }

                Text(
                    text = notification.getMessage(language),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    ),
                    color = if (isUnread) TextPrimary else TextSecondary
                )

                // Optional interactive action footer
                if (notification.type == NotificationType.CHAT_REPLY && onOpenChat != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    TextButton(
                        onClick = onOpenChat,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "متابعة المحادثة القانونية ←" else "Open Chat Conversation →",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = Navy850
                        )
                    }
                } else if (notification.lawyerId != null && onOpenLawyerProfile != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    TextButton(
                        onClick = { onOpenLawyerProfile(notification.lawyerId) },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "عرض ملف المستشار ←" else "View Counsel Profile →",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = Gold900
                        )
                    }
                }
            }
        }
    }
}
