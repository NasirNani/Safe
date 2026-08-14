package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.model.*
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.AppTab
import com.example.ui.components.AppTopBar
import com.example.ui.components.NotificationsSheet
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UmbrellaSafeApp()
        }
    }
}

@Composable
fun UmbrellaSafeApp() {
    val context = LocalContext.current
    var isDarkMode by remember { mutableStateOf(false) }
    var currentLanguage by remember { mutableStateOf(AppLanguage.ARABIC) }
    var currentTab by remember { mutableStateOf(AppTab.HOME) }
    var selectedCategoryForLawyers by remember { mutableStateOf<PracticeAreaCategory?>(null) }
    var isPostingCase by remember { mutableStateOf(false) }
    var showFeedbackScreen by remember { mutableStateOf(false) }
    var selectedLawyerForDetail by remember { mutableStateOf<Lawyer?>(null) }
    var showHotlineDialog by remember { mutableStateOf(false) }
    var showNotificationsSheet by remember { mutableStateOf(false) }
    var notificationsList by remember { mutableStateOf(MockData.initialNotifications) }

    val unreadNotificationsCount = remember(notificationsList) {
        notificationsList.count { !it.isRead }
    }

    val layoutDirection = if (currentLanguage == AppLanguage.ARABIC) {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        UmbrellaSafeTheme(darkTheme = isDarkMode) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                if (isPostingCase) {
                    PostCaseFlowScreen(
                        language = currentLanguage,
                        initialCategory = selectedCategoryForLawyers,
                        onBack = { isPostingCase = false },
                        onViewLawyerProfile = { lawyer -> selectedLawyerForDetail = lawyer },
                        onBookConsultation = { lawyer -> selectedLawyerForDetail = lawyer }
                    )
                } else if (showFeedbackScreen) {
                    FeedbackScreen(
                        language = currentLanguage,
                        onBack = { showFeedbackScreen = false }
                    )
                } else {
                    Scaffold(
                        topBar = {
                            AppTopBar(
                                currentLanguage = currentLanguage,
                                isDarkMode = isDarkMode,
                                unreadNotificationsCount = unreadNotificationsCount,
                                onOpenNotifications = { showNotificationsSheet = true },
                                onToggleLanguage = {
                                    currentLanguage = if (currentLanguage == AppLanguage.ARABIC) {
                                        AppLanguage.ENGLISH
                                    } else {
                                        AppLanguage.ARABIC
                                    }
                                },
                                onToggleTheme = { isDarkMode = !isDarkMode },
                                onCallHotline = { showHotlineDialog = true }
                            )
                        },
                        bottomBar = {
                            AppBottomNavBar(
                                selectedTab = currentTab,
                                onTabSelected = { tab ->
                                    currentTab = tab
                                    if (tab != AppTab.LAWYERS) {
                                        selectedCategoryForLawyers = null
                                    }
                                },
                                language = currentLanguage
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Crossfade(
                                targetState = currentTab,
                                label = "tab_crossfade"
                            ) { tab ->
                                when (tab) {
                                    AppTab.HOME -> {
                                        HomeScreen(
                                            language = currentLanguage,
                                            onSelectCategory = { category ->
                                                selectedCategoryForLawyers = category
                                                currentTab = AppTab.LAWYERS
                                            },
                                            onNavigateToPostCase = { isPostingCase = true },
                                            onNavigateToChat = { currentTab = AppTab.CHAT },
                                            onNavigateToContracts = { currentTab = AppTab.CONTRACTS },
                                            onNavigateToRulings = { currentTab = AppTab.RULINGS },
                                            onViewLawyerProfile = { lawyer -> selectedLawyerForDetail = lawyer },
                                            onBookConsultation = { lawyer -> selectedLawyerForDetail = lawyer },
                                            onCallHotline = { showHotlineDialog = true },
                                            onOpenNotifications = { showNotificationsSheet = true },
                                            unreadNotificationsCount = unreadNotificationsCount
                                        )
                                    }
                                    AppTab.LAWYERS -> {
                                        LawyersScreen(
                                            language = currentLanguage,
                                            initialCategory = selectedCategoryForLawyers,
                                            onNavigateToPostCase = { isPostingCase = true },
                                            onViewLawyerProfile = { lawyer -> selectedLawyerForDetail = lawyer },
                                            onBookConsultation = { lawyer -> selectedLawyerForDetail = lawyer }
                                        )
                                    }
                                    AppTab.CHAT -> {
                                        InquiriesChatScreen(
                                            language = currentLanguage,
                                            onCallHotline = { showHotlineDialog = true }
                                        )
                                    }
                                    AppTab.CONTRACTS -> {
                                        ContractTemplatesScreen(
                                            language = currentLanguage
                                        )
                                    }
                                    AppTab.RULINGS -> {
                                        CourtRulingsScreen(
                                            language = currentLanguage
                                        )
                                    }
                                    AppTab.PROFILE -> {
                                        ProfileScreen(
                                            language = currentLanguage,
                                            isDarkMode = isDarkMode,
                                            onToggleTheme = { isDarkMode = !isDarkMode },
                                            onToggleLanguage = {
                                                currentLanguage = if (currentLanguage == AppLanguage.ARABIC) {
                                                    AppLanguage.ENGLISH
                                                } else {
                                                    AppLanguage.ARABIC
                                                }
                                            },
                                            onNavigateToPostCase = { isPostingCase = true },
                                            onCallHotline = { showHotlineDialog = true },
                                            onNavigateToChat = { currentTab = AppTab.CHAT },
                                            onNavigateToFeedback = { showFeedbackScreen = true }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Notifications Bottom Sheet
                if (showNotificationsSheet) {
                    NotificationsSheet(
                        notifications = notificationsList,
                        language = currentLanguage,
                        onDismiss = { showNotificationsSheet = false },
                        onNotificationClick = { notif ->
                            notificationsList = notificationsList.map {
                                if (it.id == notif.id) it.copy(isRead = true) else it
                            }
                        },
                        onMarkAllAsRead = {
                            notificationsList = notificationsList.map { it.copy(isRead = true) }
                        },
                        onOpenLawyerProfile = { lawyerId ->
                            showNotificationsSheet = false
                            MockData.lawyers.find { it.id == lawyerId }?.let {
                                selectedLawyerForDetail = it
                            }
                        },
                        onOpenChat = {
                            showNotificationsSheet = false
                            currentTab = AppTab.CHAT
                        }
                    )
                }

                // Lawyer Detail Bottom Sheet
                selectedLawyerForDetail?.let { lawyer ->
                    LawyerDetailSheet(
                        lawyer = lawyer,
                        language = currentLanguage,
                        onDismiss = { selectedLawyerForDetail = null },
                        onStartChat = {
                            selectedLawyerForDetail = null
                            currentTab = AppTab.CHAT
                        }
                    )
                }

                // 24/7 Legal Emergency Hotline Dialog
                if (showHotlineDialog) {
                    AlertDialog(
                        onDismissRequest = { showHotlineDialog = false },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonEmergency),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhoneInTalk,
                                    contentDescription = "Hotline",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        },
                        title = {
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "الخط الساخن للطوارئ القانونية (١٦٩٩٩)" else "24/7 Legal Emergency Hotline (16999)",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC)
                                        "خدمة التدخل والاستجابة السريعة متوفرة على مدار الساعة لطوارئ التحقيقات الجنائية، الضبط والإحضار، وحوادث السير الفورية في كافة محافظات مصر."
                                    else
                                        "Rapid response legal assistance available 24/7 for urgent investigations, custody arrests, and critical accident cases across all Egyptian governorates.",
                                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                    color = TextSecondary
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = CrimsonLight,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = if (currentLanguage == AppLanguage.ARABIC) "رقم الاتصال الموحد: 16999 (مصر)" else "Toll-Free Hotline: 16999 (Egypt)",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = CrimsonEmergency
                                        ),
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:16999"))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "16999", Toast.LENGTH_SHORT).show()
                                    }
                                    showHotlineDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonEmergency)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "اتصال الآن" else "Call Now",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showHotlineDialog = false }) {
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "إلغاء" else "Cancel",
                                    color = TextSecondary
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
