package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.model.*
import com.example.ui.theme.*

enum class UserProfileTab {
    INQUIRIES, CONTRACTS, PERSONAL_INFO, SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    language: AppLanguage,
    onToggleLanguage: () -> Unit,
    onNavigateToPostCase: () -> Unit,
    onCallHotline: () -> Unit,
    onNavigateToChat: () -> Unit = {},
    onNavigateToFeedback: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(UserProfileTab.INQUIRIES) }
    var showEditProfileModal by remember { mutableStateOf(false) }
    var showLogoutModal by remember { mutableStateOf(false) }
    var selectedContractForPreview by remember { mutableStateOf<UserDownloadedContractItem?>(null) }
    var selectedInquiryForDetail by remember { mutableStateOf<UserInquiryHistoryItem?>(null) }

    // User Profile State
    var userProfile by remember {
        mutableStateOf(
            UserProfileData(
                fullNameAr = "عبد الله الطيبي",
                fullNameEn = "Abdullah El-Tiby",
                phone = "+20 100 123 4567",
                email = "abdullaheltiby@gmail.com",
                governorateAr = "القاهرة - التجمع الخامس",
                governorateEn = "Cairo - 5th Settlement",
                occupationAr = "مهندس برمجيات وريادي أعمال",
                occupationEn = "Software Engineer & Founder",
                nationalIdMasked = "2940815******",
                isNationalIdVerified = true
            )
        )
    }

    // Settings States
    var biometricLockEnabled by remember { mutableStateOf(true) }
    var pushNotificationsEnabled by remember { mutableStateOf(true) }
    var smsAlertsEnabled by remember { mutableStateOf(true) }
    var endToEndEncryptionEnabled by remember { mutableStateOf(true) }

    val inquiryHistory = remember { MockData.userInquiriesHistory }
    val downloadedContracts = remember { MockData.userDownloadedContracts }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(OffWhite)
            .testTag("user_profile_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Header Hero Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_profile_hero_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // User Avatar Monogram
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Navy700, Navy950)
                                        )
                                    )
                                    .border(2.dp, GoldPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                val initials = userProfile.getFullName(language)
                                    .split(" ")
                                    .take(2)
                                    .mapNotNull { it.firstOrNull()?.toString() }
                                    .joinToString("")

                                Text(
                                    text = initials,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    ),
                                    color = GoldLight
                                )
                            }

                            // National ID Verified Badge Icon
                            Surface(
                                shape = CircleShape,
                                color = GoldPrimary,
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Navy900),
                                modifier = Modifier.size(20.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VerifiedUser,
                                        contentDescription = "Verified ID",
                                        tint = Navy950,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        // Name & Contact summary
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userProfile.getFullName(language),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    ),
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = userProfile.phone,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextOnNavySecondary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = EmeraldSuccess.copy(alpha = 0.2f),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, EmeraldSuccess)
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "هوية موثقة بالرقم القومي" else "National ID Verified",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = EmeraldSuccess,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Edit Profile Button
                        IconButton(
                            onClick = { showEditProfileModal = true },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Navy800)
                                .border(1.dp, GoldPrimary.copy(alpha = 0.4f), CircleShape)
                                .testTag("edit_profile_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = GoldLight,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Key stats row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Navy850)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${inquiryHistory.size}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = GoldLight
                            )
                            Text(
                                text = if (language == AppLanguage.ARABIC) "استشارات قانونية" else "Inquiries",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = TextOnNavySecondary
                            )
                        }

                        Divider(
                            modifier = Modifier
                                .height(22.dp)
                                .width(1.dp),
                            color = BorderDark
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${downloadedContracts.size}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = if (language == AppLanguage.ARABIC) "عقود محملة" else "Contracts",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = TextOnNavySecondary
                            )
                        }

                        Divider(
                            modifier = Modifier
                                .height(22.dp)
                                .width(1.dp),
                            color = BorderDark
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = userProfile.memberSinceYear,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = GoldPrimary
                            )
                            Text(
                                text = if (language == AppLanguage.ARABIC) "عضو منذ" else "Member Since",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = TextOnNavySecondary
                            )
                        }
                    }
                }
            }
        }

        // Section Tabs
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val profileTabs = listOf(
                    UserProfileTab.INQUIRIES to (if (language == AppLanguage.ARABIC) "سجل الاستشارات (${inquiryHistory.size})" else "Inquiries (${inquiryHistory.size})"),
                    UserProfileTab.CONTRACTS to (if (language == AppLanguage.ARABIC) "العقود المحملة (${downloadedContracts.size})" else "Downloaded Contracts (${downloadedContracts.size})"),
                    UserProfileTab.PERSONAL_INFO to (if (language == AppLanguage.ARABIC) "البيانات الشخصية" else "Personal Info"),
                    UserProfileTab.SETTINGS to (if (language == AppLanguage.ARABIC) "إعدادات الحساب" else "Settings")
                )

                items(profileTabs) { (tab, label) ->
                    val isSelected = selectedTab == tab
                    Surface(
                        onClick = { selectedTab = tab },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Navy900 else SurfaceCard,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) Navy900 else BorderSubtle
                        ),
                        modifier = Modifier.testTag("profile_tab_${tab.name}")
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) GoldLight else TextSecondary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Tab Content
        when (selectedTab) {
            UserProfileTab.INQUIRIES -> {
                // Inquiries and Chats History
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "استشاراتك ومحادثاتك القضائية السابقة" else "Your Past Legal Inquiries & Chats",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Navy950
                        )
                        TextButton(onClick = onNavigateToPostCase) {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "+ استشارة جديدة" else "+ New Inquiry",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Gold900
                            )
                        }
                    }
                }

                items(inquiryHistory) { inquiry ->
                    Card(
                        onClick = { selectedInquiryForDetail = inquiry },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("inquiry_item_${inquiry.id}")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Navy850),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = GoldLight,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = inquiry.getLawyerName(language),
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Navy950
                                        )
                                        Text(
                                            text = inquiry.getLawyerSpecialty(language),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Navy700
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(inquiry.statusColorHex).copy(alpha = 0.12f),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        Color(inquiry.statusColorHex).copy(alpha = 0.4f)
                                    )
                                ) {
                                    Text(
                                        text = inquiry.getStatus(language),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        ),
                                        color = Color(inquiry.statusColorHex),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Divider(color = BorderSubtle)

                            Text(
                                text = inquiry.getTopic(language),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 20.sp
                                ),
                                color = TextPrimary
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Navy50,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Chat,
                                        contentDescription = null,
                                        tint = Navy700,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = inquiry.getLastMessagePreview(language),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = TextSecondary,
                                        maxLines = 1
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = inquiry.getDate(language),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "عرض التفاصيل والمحادثة" else "View Chat & Details",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Gold900
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = Gold900,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            UserProfileTab.CONTRACTS -> {
                // Downloaded Contract Templates Section
                item {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "مكتبة العقود والنماذج القانونية المحملة" else "Your Downloaded Legal Contracts & Forms",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Navy950
                    )
                }

                items(downloadedContracts) { contractItem ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("downloaded_contract_${contractItem.id}")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(GoldPale)
                                            .border(1.dp, GoldBorder, RoundedCornerShape(10.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Description,
                                            contentDescription = null,
                                            tint = Gold900,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = contractItem.getTitle(language),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                lineHeight = 18.sp
                                            ),
                                            color = Navy950
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${contractItem.clausesCount} ${AppStrings.clausesCount.get(language)} • ${contractItem.fileSize}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Navy50
                                ) {
                                    Text(
                                        text = contractItem.fileFormat,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        ),
                                        color = Navy800,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${if (language == AppLanguage.ARABIC) "تاريخ التحميل: " else "Downloaded: "}${contractItem.getDownloadDate(language)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(
                                        onClick = { selectedContractForPreview = contractItem },
                                        shape = RoundedCornerShape(8.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderMedium),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = Navy800,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "معاينة" else "Preview",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Navy900
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            Toast.makeText(
                                                context,
                                                if (language == AppLanguage.ARABIC) "جارٍ إعادة تحميل ${contractItem.getTitle(language)} بصيغة Word & PDF..." else "Re-downloading ${contractItem.getTitle(language)}...",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = GoldPrimary,
                                            contentColor = Navy950
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FileDownload,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "تحميل" else "Download",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            UserProfileTab.PERSONAL_INFO -> {
                // Personal Info Details View & Edit
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Badge,
                                        contentDescription = null,
                                        tint = Gold800,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "البيانات المسجلة والتحقق القانوني" else "Registered Profile & Verification",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Navy950
                                    )
                                }

                                TextButton(onClick = { showEditProfileModal = true }) {
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "تعديل" else "Edit",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Gold900
                                    )
                                }
                            }

                            // Full Name Item
                            ProfileInfoRow(
                                icon = Icons.Default.Person,
                                label = if (language == AppLanguage.ARABIC) "الاسم الكامل الثلاثي" else "Full Name",
                                value = userProfile.getFullName(language)
                            )

                            // Phone Item
                            ProfileInfoRow(
                                icon = Icons.Default.Phone,
                                label = if (language == AppLanguage.ARABIC) "رقم الهاتف المحمول" else "Mobile Number",
                                value = userProfile.phone
                            )

                            // Email Item
                            ProfileInfoRow(
                                icon = Icons.Default.Email,
                                label = if (language == AppLanguage.ARABIC) "البريد الإلكتروني" else "Email Address",
                                value = userProfile.email
                            )

                            // Location
                            ProfileInfoRow(
                                icon = Icons.Default.LocationOn,
                                label = if (language == AppLanguage.ARABIC) "المحافظة والمنطقة" else "Governorate & District",
                                value = userProfile.getGovernorate(language)
                            )

                            // Occupation
                            ProfileInfoRow(
                                icon = Icons.Default.Work,
                                label = if (language == AppLanguage.ARABIC) "المهنة / النشاط الاقتصادي" else "Occupation / Industry",
                                value = userProfile.getOccupation(language)
                            )

                            // National ID Status
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = GoldPale,
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = Gold900,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "حالة التوثيق القومي: موثق ومعتمد" else "National Verification: Verified & Active",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Gold900
                                        )
                                        Text(
                                            text = "${if (language == AppLanguage.ARABIC) "رقم البطاقة المشفر: " else "Masked National ID: "}${userProfile.nationalIdMasked}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            UserProfileTab.SETTINGS -> {
                // Account Settings & Security
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "إعدادات الأمان والتفضيلات" else "Security & App Preferences",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Navy950
                            )

                            // Language Switch Setting
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onToggleLanguage() }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = null,
                                        tint = Navy700
                                    )
                                    Column {
                                        Text(
                                            text = AppStrings.languageSetting.get(language),
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "اللغة الحالية: العربية (مصر)" else "Current: English",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Navy50,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Navy100)
                                ) {
                                    Text(
                                        text = AppStrings.switchLanguage.get(language),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Navy900,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            Divider(color = BorderSubtle)

                            // Biometrics Toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Fingerprint,
                                        contentDescription = null,
                                        tint = Navy700
                                    )
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "القفل البيومتري (بصمة الإصبع / الوجه)" else "Biometric App Lock (Fingerprint / Face ID)",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "حماية سرية المستندات والاستشارات" else "Protect confidential case files",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Switch(
                                    checked = biometricLockEnabled,
                                    onCheckedChange = { biometricLockEnabled = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = GoldPrimary
                                    )
                                )
                            }

                            Divider(color = BorderSubtle)

                            // Notifications Toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = Navy700
                                    )
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "إشعارات الجلسات ومواعيد الاستشارة" else "Court Hearings & Consultation Alerts",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "تنبيهات فورية عند رد المستشارين" else "Instant push alerts when counsel replies",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Switch(
                                    checked = pushNotificationsEnabled,
                                    onCheckedChange = { pushNotificationsEnabled = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = GoldPrimary
                                    )
                                )
                            }

                            Divider(color = BorderSubtle)

                            // End-to-End Encryption
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = EmeraldSuccess
                                    )
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "تشفير كامل للمستندات والمحادثات" else "End-to-End Data & File Encryption",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "متوافق مع معايير السرية المهنية" else "Compliant with Legal Professional Conduct",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Switch(
                                    checked = endToEndEncryptionEnabled,
                                    onCheckedChange = { endToEndEncryptionEnabled = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = EmeraldSuccess
                                    )
                                )
                            }
                        }
                    }
                }

                // Support & Logout Section
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Hotline Action
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onCallHotline() }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhoneInTalk,
                                    contentDescription = null,
                                    tint = CrimsonEmergency
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = AppStrings.emergencyHotline.get(language),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = CrimsonEmergency
                                    )
                                    Text(
                                        text = AppStrings.hotlineNotice.get(language),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = CrimsonEmergency
                                )
                            }

                            Divider(color = BorderSubtle)

                            // Feedback & Suggestions Action Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToFeedback() }
                                    .padding(vertical = 8.dp)
                                    .testTag("profile_feedback_btn"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RateReview,
                                    contentDescription = null,
                                    tint = Gold900
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = AppStrings.feedbackAndSuggestions.get(language),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Navy950
                                    )
                                    Text(
                                        text = AppStrings.feedbackSubtitle.get(language),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary,
                                        maxLines = 1
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Gold900
                                )
                            }

                            Divider(color = BorderSubtle)

                            // Legal Syndicate Compliance Notice
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Gold900,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = AppStrings.syndicateCompliance.get(language),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        lineHeight = 18.sp,
                                        fontSize = 11.sp
                                    ),
                                    color = TextSecondary
                                )
                            }

                            Divider(color = BorderSubtle)

                            // Logout Button
                            TextButton(
                                onClick = { showLogoutModal = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Logout,
                                    contentDescription = null,
                                    tint = CrimsonEmergency,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "تسجيل الخروج من الحساب" else "Sign Out of Account",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = CrimsonEmergency
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit Profile Modal Dialog
    if (showEditProfileModal) {
        var editNameAr by remember { mutableStateOf(userProfile.fullNameAr) }
        var editNameEn by remember { mutableStateOf(userProfile.fullNameEn) }
        var editPhone by remember { mutableStateOf(userProfile.phone) }
        var editEmail by remember { mutableStateOf(userProfile.email) }
        var editGovernorateAr by remember { mutableStateOf(userProfile.governorateAr) }
        var editGovernorateEn by remember { mutableStateOf(userProfile.governorateEn) }
        var editOccupationAr by remember { mutableStateOf(userProfile.occupationAr) }
        var editOccupationEn by remember { mutableStateOf(userProfile.occupationEn) }

        AlertDialog(
            onDismissRequest = { showEditProfileModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ManageAccounts,
                        contentDescription = null,
                        tint = Gold900,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "تعديل البيانات الشخصية" else "Edit Personal Information",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = if (language == AppLanguage.ARABIC) editNameAr else editNameEn,
                            onValueChange = {
                                if (language == AppLanguage.ARABIC) editNameAr = it else editNameEn = it
                            },
                            label = {
                                Text(
                                    if (language == AppLanguage.ARABIC) "الاسم الكامل" else "Full Name",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = editPhone,
                            onValueChange = { editPhone = it },
                            label = {
                                Text(
                                    if (language == AppLanguage.ARABIC) "رقم الهاتف" else "Phone Number",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = editEmail,
                            onValueChange = { editEmail = it },
                            label = {
                                Text(
                                    if (language == AppLanguage.ARABIC) "البريد الإلكتروني" else "Email Address",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = if (language == AppLanguage.ARABIC) editGovernorateAr else editGovernorateEn,
                            onValueChange = {
                                if (language == AppLanguage.ARABIC) editGovernorateAr = it else editGovernorateEn = it
                            },
                            label = {
                                Text(
                                    if (language == AppLanguage.ARABIC) "المحافظة والحي" else "Governorate & Area",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = if (language == AppLanguage.ARABIC) editOccupationAr else editOccupationEn,
                            onValueChange = {
                                if (language == AppLanguage.ARABIC) editOccupationAr = it else editOccupationEn = it
                            },
                            label = {
                                Text(
                                    if (language == AppLanguage.ARABIC) "المهنة / النشاط التجاري" else "Occupation / Business",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        userProfile = userProfile.copy(
                            fullNameAr = editNameAr,
                            fullNameEn = editNameEn,
                            phone = editPhone,
                            email = editEmail,
                            governorateAr = editGovernorateAr,
                            governorateEn = editGovernorateEn,
                            occupationAr = editOccupationAr,
                            occupationEn = editOccupationEn
                        )
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ARABIC) "تم حفظ التعديلات في ملفك الشخصي بنجاح!" else "Personal information updated successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        showEditProfileModal = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Navy950
                    )
                ) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "حفظ التغييرات" else "Save Changes",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileModal = false }) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إلغاء" else "Cancel",
                        color = TextSecondary
                    )
                }
            }
        )
    }

    // Downloaded Contract Preview Modal
    selectedContractForPreview?.let { contractItem ->
        AlertDialog(
            onDismissRequest = { selectedContractForPreview = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = Gold900,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = contractItem.getTitle(language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GoldPale,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${contractItem.clausesCount} ${AppStrings.clausesCount.get(language)} • ${contractItem.fileFormat}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Gold900
                            )
                            Text(
                                text = contractItem.fileSize,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Text(
                        text = if (language == AppLanguage.ARABIC)
                            "هذا النموذج معتمد وموثق قانونياً طبقاً لأحكام القانون المدني والتشريعات المصرية السارية. يتضمن الشروط النموذجية، التزامات الأطراف، والشرط الجزائي."
                        else
                            "This template is legally certified under the Egyptian Civil Code and applicable statutes. Includes model clauses, obligations, and penalty provisions.",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                        color = TextPrimary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ARABIC) "تم تنزيل النموذج ${contractItem.getTitle(language)} بنجاح!" else "Downloaded ${contractItem.getTitle(language)} successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        selectedContractForPreview = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Navy950
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "تحميل النموذج" else "Download File",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedContractForPreview = null }) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إغلاق" else "Close",
                        color = TextSecondary
                    )
                }
            }
        )
    }

    // Inquiry Detail Dialog
    selectedInquiryForDetail?.let { inquiry ->
        AlertDialog(
            onDismissRequest = { selectedInquiryForDetail = null },
            title = {
                Column {
                    Text(
                        text = inquiry.getTopic(language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Navy950
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${inquiry.getLawyerName(language)} - ${inquiry.getLawyerSpecialty(language)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Gold900
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(inquiry.statusColorHex).copy(alpha = 0.15f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${if (language == AppLanguage.ARABIC) "الحالة القضائية: " else "Status: "}${inquiry.getStatus(language)}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(inquiry.statusColorHex)
                            )
                            Text(
                                text = inquiry.getDate(language),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Text(
                        text = if (language == AppLanguage.ARABIC) "آخر إفادة من المستشار القانوني:" else "Latest Advice from Counsel:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Navy950
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Navy50,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = inquiry.getLastMessagePreview(language),
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                            color = TextPrimary,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedInquiryForDetail = null
                        onNavigateToChat()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Navy900,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "فتح سجل المحادثة" else "Open Chat History",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedInquiryForDetail = null }) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إغلاق" else "Close",
                        color = TextSecondary
                    )
                }
            }
        )
    }

    // Sign Out Confirmation Modal
    if (showLogoutModal) {
        AlertDialog(
            onDismissRequest = { showLogoutModal = false },
            title = {
                Text(
                    text = if (language == AppLanguage.ARABIC) "تأكيد تسجيل الخروج" else "Confirm Sign Out",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = if (language == AppLanguage.ARABIC)
                        "هل أنت متأكد من رغبتك في تسجيل الخروج من منصة 'مظلة'؟ ستحتاج إلى إعادة التحقق بالرقم القومي عند الدخول مجدداً."
                    else
                        "Are you sure you want to sign out of UMBRELLA? You will need to re-verify your credentials on next sign in.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ARABIC) "تم تسجيل الخروج بنجاح" else "Signed out successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        showLogoutModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonEmergency)
                ) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "تسجيل الخروج" else "Sign Out",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutModal = false }) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إلغاء" else "Cancel",
                        color = TextSecondary
                    )
                }
            }
        )
    }
}

@Composable
private fun ProfileInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Navy50)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Navy700,
            modifier = Modifier.size(18.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = TextSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = Navy950
            )
        }
    }
}
