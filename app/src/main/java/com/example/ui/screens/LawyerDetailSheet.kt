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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.Lawyer
import com.example.model.LawyerReview
import com.example.ui.theme.*

enum class ProfileTabSection {
    ABOUT, CREDENTIALS, REVIEWS, LOCATION
}

enum class ConsultationType {
    VIDEO_CALL, OFFICE_VISIT, PHONE_CALL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LawyerDetailSheet(
    lawyer: Lawyer,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onStartChat: (Lawyer) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = OffWhite,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = BorderMedium,
                width = 44.dp,
                height = 4.dp
            )
        },
        tonalElevation = 8.dp,
        modifier = modifier
            .fillMaxHeight(0.94f)
            .testTag("lawyer_profile_detail_sheet")
    ) {
        LawyerProfileDetailContent(
            lawyer = lawyer,
            language = language,
            onClose = onDismiss,
            onStartChat = onStartChat
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LawyerProfileDetailContent(
    lawyer: Lawyer,
    language: AppLanguage,
    onClose: () -> Unit,
    onStartChat: (Lawyer) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isSaved by remember { mutableStateOf(false) }
    var selectedSection by remember { mutableStateOf(ProfileTabSection.ABOUT) }
    var showBookingModal by remember { mutableStateOf(false) }
    var showDirectMessageModal by remember { mutableStateOf(false) }
    var showAddReviewModal by remember { mutableStateOf(false) }

    val reviews = remember(lawyer.id) { MockData.getReviewsForLawyer(lawyer.id) }
    val credentials = remember(lawyer.id) { MockData.getCredentialsForLawyer(lawyer.id) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(OffWhite)
                .testTag("lawyer_profile_lazy_column"),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar Controls inside Sheet
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Navy50)
                            .border(1.dp, BorderSubtle, CircleShape)
                            .testTag("lawyer_profile_close_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Navy900,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                isSaved = !isSaved
                                Toast.makeText(
                                    context,
                                    if (isSaved) {
                                        if (language == AppLanguage.ARABIC) "تم حفظ المستشار في قائمتك المفضلة" else "Lawyer added to saved favorites"
                                    } else {
                                        if (language == AppLanguage.ARABIC) "تمت الإزالة من المفضلة" else "Removed from favorites"
                                    },
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isSaved) GoldPale else Navy50)
                                .border(1.dp, if (isSaved) GoldPrimary else BorderSubtle, CircleShape)
                                .testTag("save_lawyer_button")
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Save Lawyer",
                                tint = if (isSaved) Gold900 else Navy800,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    if (language == AppLanguage.ARABIC) "تم نسخ رابط الملف الشخصي للمستشار" else "Profile link copied to clipboard",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Navy50)
                                .border(1.dp, BorderSubtle, CircleShape)
                                .testTag("share_lawyer_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Profile",
                                tint = Navy800,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Lawyer Hero Profile Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("lawyer_hero_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar Monogram with Status Badge
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Box(
                                modifier = Modifier
                                    .size(92.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(lawyer.avatarColorHex),
                                                Navy950
                                            )
                                        )
                                    )
                                    .border(3.dp, GoldPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                val initials = lawyer.getName(language)
                                    .split(" ")
                                    .take(2)
                                    .mapNotNull { it.firstOrNull()?.toString() }
                                    .joinToString("")

                                Text(
                                    text = initials,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = GoldLight
                                )
                            }

                            // Online Status Indicator
                            Surface(
                                shape = CircleShape,
                                color = EmeraldSuccess,
                                border = androidx.compose.foundation.BorderStroke(2.dp, Color.White),
                                modifier = Modifier.size(22.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Online",
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Full Name + Verification
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = lawyer.getName(language),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 19.sp
                                ),
                                color = Navy950,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Syndicate Verified",
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Detailed Title
                        Text(
                            text = lawyer.getTitle(language),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            ),
                            color = Navy700,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Bar Level Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldPale,
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Gavel,
                                    contentDescription = null,
                                    tint = Gold900,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = lawyer.getBarLevel(language),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    ),
                                    color = Gold900
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Highlights Grid
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Navy50)
                                .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rating
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = AmberRating,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${lawyer.rating}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        ),
                                        color = Navy950
                                    )
                                }
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "(${lawyer.reviewsCount} تقييم)" else "(${lawyer.reviewsCount} revs)",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = TextSecondary
                                )
                            }

                            Divider(
                                modifier = Modifier
                                    .height(28.dp)
                                    .width(1.dp),
                                color = BorderMedium
                            )

                            // Cases Won
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "+${lawyer.casesWonCount}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    color = Navy900
                                )
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "قضية ناجحة" else "Cases Won",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = TextSecondary
                                )
                            }

                            Divider(
                                modifier = Modifier
                                    .height(28.dp)
                                    .width(1.dp),
                                color = BorderMedium
                            )

                            // Years Experience
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${lawyer.experienceYears} ${if (language == AppLanguage.ARABIC) "سنة" else "Yrs"}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    color = Gold800
                                )
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "خبرة قضائية" else "Experience",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = TextSecondary
                                )
                            }

                            Divider(
                                modifier = Modifier
                                    .height(28.dp)
                                    .width(1.dp),
                                color = BorderMedium
                            )

                            // Response Time
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "< 15 د",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    color = EmeraldSuccess
                                )
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "سرعة الرد" else "Response",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Quick Direct Action Buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showDirectMessageModal = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Navy900,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("send_direct_message_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) "رسالة مباشرة" else "Direct Message",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${lawyer.phone}"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, lawyer.phone, Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("call_lawyer_office_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = Navy800,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) "اتصال بالمكتب" else "Call Office",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Navy900
                        )
                    }
                }
            }

            // Tab Selector Chips
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tabs = listOf(
                        ProfileTabSection.ABOUT to (if (language == AppLanguage.ARABIC) "نبذة وخبرات" else "About & Bio"),
                        ProfileTabSection.CREDENTIALS to (if (language == AppLanguage.ARABIC) "المؤهلات والنقابة" else "Credentials"),
                        ProfileTabSection.REVIEWS to (if (language == AppLanguage.ARABIC) "آراء الموكلين (${reviews.size})" else "Reviews (${reviews.size})"),
                        ProfileTabSection.LOCATION to (if (language == AppLanguage.ARABIC) "المقر والمحاكم" else "Office & Court")
                    )

                    items(tabs) { (tab, label) ->
                        val isSelected = selectedSection == tab
                        Surface(
                            onClick = { selectedSection = tab },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Navy900 else SurfaceCard,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) Navy900 else BorderSubtle
                            ),
                            modifier = Modifier.testTag("tab_${tab.name}")
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

            // Section Content depending on selected tab
            when (selectedSection) {
                ProfileTabSection.ABOUT -> {
                    // Bio Card
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = Gold800,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "نبذة تعريفية ومجالات التخصص" else "Professional Bio & Practice Focus",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Navy950
                                    )
                                }

                                Text(
                                    text = lawyer.getBio(language),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        lineHeight = 23.sp,
                                        color = TextPrimary
                                    )
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = if (language == AppLanguage.ARABIC) "مجالات الترافع والاستشارات الرئيسية:" else "Core Litigation & Advisory Areas:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Navy850
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val practiceTags = if (language == AppLanguage.ARABIC) {
                                        listOf("محكمة النقض", "عقود وصياغة", "نزاعات استثمارية", "تسجيل عقاري")
                                    } else {
                                        listOf("Cassation Court", "Contract Drafting", "Commercial Disputes", "Title Deeds")
                                    }
                                    practiceTags.forEach { tag ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Navy50,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Navy100)
                                        ) {
                                            Text(
                                                text = tag,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                                color = Navy700,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                ProfileTabSection.CREDENTIALS -> {
                    // Academic Degrees & Bar Admissions
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = Gold800,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "المؤهلات الأكاديمية والاعتماد النقابي" else "Academic & Bar Qualifications",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Navy950
                                    )
                                }

                                credentials.forEach { cred ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Navy50)
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Navy900),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.WorkspacePremium,
                                                contentDescription = null,
                                                tint = GoldLight,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = cred.getDegree(language),
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                color = Navy950
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = cred.getInstitution(language),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextSecondary
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "${if (language == AppLanguage.ARABIC) "سنة التخرج / القيد: " else "Year: "}${cred.year}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                                color = Gold800
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                ProfileTabSection.REVIEWS -> {
                    // Reviews Breakdown + Testimonials List
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                // Rating Summary Header
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${lawyer.rating} / 5.0",
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.ExtraBold
                                            ),
                                            color = Navy950
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            repeat(5) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = AmberRating,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "بناءً على ${lawyer.reviewsCount} تقييماً موثقاً" else "Based on ${lawyer.reviewsCount} verified reviews",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = { showAddReviewModal = true },
                                        shape = RoundedCornerShape(10.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Gold800),
                                        modifier = Modifier.testTag("write_review_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.RateReview,
                                            contentDescription = null,
                                            tint = Gold900,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "كتابة تقييم" else "Add Review",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Gold900
                                        )
                                    }
                                }

                                Divider(color = BorderSubtle)

                                // Testimonials List
                                reviews.forEach { review ->
                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = OffWhite),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Text(
                                                        text = review.getReviewerName(language),
                                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                                        color = Navy950
                                                    )
                                                    if (review.isVerifiedClient) {
                                                        Surface(
                                                            shape = RoundedCornerShape(4.dp),
                                                            color = EmeraldSuccess.copy(alpha = 0.15f)
                                                        ) {
                                                            Text(
                                                                text = if (language == AppLanguage.ARABIC) "موكل موثق" else "Verified Client",
                                                                style = MaterialTheme.typography.labelSmall.copy(
                                                                    fontSize = 9.sp,
                                                                    fontWeight = FontWeight.Bold
                                                                ),
                                                                color = EmeraldSuccess,
                                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                            )
                                                        }
                                                    }
                                                }

                                                Text(
                                                    text = review.getDate(language),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = TextMuted
                                                )
                                            }

                                            // Rating & Category
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Row {
                                                    repeat(5) { starIndex ->
                                                        Icon(
                                                            imageVector = if (starIndex < review.rating.toInt()) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                                            contentDescription = null,
                                                            tint = AmberRating,
                                                            modifier = Modifier.size(14.dp)
                                                        )
                                                    }
                                                }

                                                Surface(
                                                    shape = RoundedCornerShape(4.dp),
                                                    color = Navy100
                                                ) {
                                                    Text(
                                                        text = review.getCaseCategory(language),
                                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                        color = Navy800,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            Text(
                                                text = review.getComment(language),
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    lineHeight = 19.sp,
                                                    color = TextPrimary
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                ProfileTabSection.LOCATION -> {
                    // Office Address & Court Jurisdictions
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        tint = CrimsonEmergency,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "مقر المكتب والمحاكم المختصة" else "Office Address & Court Venues",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Navy950
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = OffWhite,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "${lawyer.getOfficeAddress(language)}",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                            color = Navy950
                                        )
                                        Text(
                                            text = "${lawyer.getGovernorate(language)} - جمهورية مصر العربية",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AccessTime,
                                                contentDescription = null,
                                                tint = Gold800,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = if (language == AppLanguage.ARABIC) "ساعات العمل بالمكتب: يومياً من ٤:٠٠ م إلى ١٠:٠٠ م (عدا الجمعة)" else "Office Hours: Daily 4:00 PM - 10:00 PM (Excl. Fri)",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextSecondary
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = if (language == AppLanguage.ARABIC) "المحاكم التي يمثل أمامها المستشار:" else "Courts of Appearance:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Navy850
                                )

                                val courts = if (language == AppLanguage.ARABIC) {
                                    listOf("محكمة النقض (دار القضاء العالي)", "المحكمة الدستورية العليا (المعادي)", "محكمة استئناف القاهرة (العباسية)", "المحاكم الاقتصادية ومجلس الدولة")
                                } else {
                                    listOf("Court of Cassation (High Court Palace)", "Supreme Constitutional Court", "Cairo Court of Appeal", "Economic & State Council Courts")
                                }

                                courts.forEach { court ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccountBalance,
                                            contentDescription = null,
                                            tint = Navy700,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = court,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Sticky Action Bar (Schedule Consultation & Fee)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .testTag("lawyer_sticky_action_bar"),
            color = SurfaceCard,
            shadowElevation = 16.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "أتعاب الاستشارة" else "Consultation Fee",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${lawyer.consultationFeeEgp}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp
                            ),
                            color = Gold900
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.egp.get(language),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Navy900,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }

                Button(
                    onClick = { showBookingModal = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Navy950
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("schedule_consultation_cta_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "حجز موعد استشارة" else "Schedule Consultation",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }

    // Schedule Consultation Booking Modal
    if (showBookingModal) {
        var selectedConsultType by remember { mutableStateOf(ConsultationType.VIDEO_CALL) }
        var selectedDateIndex by remember { mutableStateOf(0) }
        var selectedTimeIndex by remember { mutableStateOf(1) }
        var caseBriefNote by remember { mutableStateOf("") }

        val bookingDays = if (language == AppLanguage.ARABIC) {
            listOf("اليوم (الأحد)", "غداً (الإثنين)", "الثلاثاء", "الأربعاء", "الخميس")
        } else {
            listOf("Today (Sun)", "Tomorrow (Mon)", "Tuesday", "Wednesday", "Thursday")
        }

        val timeSlots = listOf("02:00 م", "04:30 م", "06:00 م", "08:00 م", "09:30 م")

        AlertDialog(
            onDismissRequest = { showBookingModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Gold900,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "حجز موعد استشارة قانونية" else "Schedule Legal Consultation",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Consultation Method Selector
                    Text(
                        text = if (language == AppLanguage.ARABIC) "نوع الاستشارة المفضلة:" else "Consultation Method:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Navy950
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val types = listOf(
                            ConsultationType.VIDEO_CALL to (if (language == AppLanguage.ARABIC) "🎥 فيديو أونلاين" else "🎥 Video Call"),
                            ConsultationType.OFFICE_VISIT to (if (language == AppLanguage.ARABIC) "🏢 مقابلة مكتبية" else "🏢 Office Visit"),
                            ConsultationType.PHONE_CALL to (if (language == AppLanguage.ARABIC) "📞 هاتفياً" else "📞 Phone Call")
                        )

                        types.forEach { (type, label) ->
                            val isSelected = selectedConsultType == type
                            Surface(
                                onClick = { selectedConsultType = type },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Navy900 else Navy50,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) GoldPrimary else BorderSubtle
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 10.sp
                                    ),
                                    color = if (isSelected) GoldLight else TextPrimary,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Select Day
                    Text(
                        text = if (language == AppLanguage.ARABIC) "اختر اليوم المناسب:" else "Select Day:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Navy950
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(bookingDays.indices.toList()) { index ->
                            val isSelected = selectedDateIndex == index
                            Surface(
                                onClick = { selectedDateIndex = index },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Navy900 else Navy50,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) GoldPrimary else BorderSubtle
                                )
                            ) {
                                Text(
                                    text = bookingDays[index],
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (isSelected) GoldLight else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Select Time Slot
                    Text(
                        text = if (language == AppLanguage.ARABIC) "التوقيت المتاح للمستشار:" else "Available Time Slot:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Navy950
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(timeSlots.indices.toList()) { index ->
                            val isSelected = selectedTimeIndex == index
                            Surface(
                                onClick = { selectedTimeIndex = index },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Navy900 else Navy50,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) GoldPrimary else BorderSubtle
                                )
                            ) {
                                Text(
                                    text = timeSlots[index],
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp
                                    ),
                                    color = if (isSelected) GoldLight else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Brief Notes
                    OutlinedTextField(
                        value = caseBriefNote,
                        onValueChange = { caseBriefNote = it },
                        label = {
                            Text(
                                if (language == AppLanguage.ARABIC) "ملاحظات أو نبذة عن الاستشارة (اختياري)" else "Brief Notes for Counsel (Optional)",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        placeholder = {
                            Text(
                                if (language == AppLanguage.ARABIC) "مثال: مراجعة عقد إيجار شقة سكني..." else "e.g. Residential lease contract review...",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        maxLines = 2,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Price & Guarantee Card
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GoldPale,
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "أتعاب الاستشارة المقررة:" else "Consultation Fee:",
                                style = MaterialTheme.typography.bodySmall,
                                color = Gold900
                            )
                            Text(
                                text = "${lawyer.consultationFeeEgp} ${AppStrings.egp.get(language)}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Gold900
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ARABIC) {
                                "تم تأكيد حجز موعد الاستشارة مع ${lawyer.getName(language)} يوم ${bookingDays[selectedDateIndex]} في تمام الساعة ${timeSlots[selectedTimeIndex]}!"
                            } else {
                                "Consultation booked with ${lawyer.getName(language)} on ${bookingDays[selectedDateIndex]} at ${timeSlots[selectedTimeIndex]}!"
                            },
                            Toast.LENGTH_LONG
                        ).show()
                        showBookingModal = false
                        onClose()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Navy950
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "تأكيد الحجز والدفع" else "Confirm & Pay",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookingModal = false }) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إلغاء" else "Cancel",
                        color = TextSecondary
                    )
                }
            }
        )
    }

    // Direct Message Dialog
    if (showDirectMessageModal) {
        var directMessageText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDirectMessageModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = null,
                        tint = Navy900,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إرسال رسالة مباشرة إلى المستشار" else "Direct Message to Counsel",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.ARABIC)
                            "تواصل مباشرة مع ${lawyer.getName(language)} لعرض استفسارك المبدئي أو طلب عرض أتعاب مخصص."
                        else
                            "Message ${lawyer.getName(language)} directly to submit your preliminary question or request a fee quote.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    OutlinedTextField(
                        value = directMessageText,
                        onValueChange = { directMessageText = it },
                        placeholder = {
                            Text(
                                if (language == AppLanguage.ARABIC) "اكتب رسالتك للمستشار هنا..." else "Type your direct message here...",
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("direct_message_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ARABIC) "تم إرسال رسالتك إلى المستشار بنجاح!" else "Message sent to counsel successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        showDirectMessageModal = false
                        onClose()
                        onStartChat(lawyer)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Navy900,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إرسال والبدء" else "Send & Chat",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDirectMessageModal = false }) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إلغاء" else "Cancel",
                        color = TextSecondary
                    )
                }
            }
        )
    }

    // Add Review Modal
    if (showAddReviewModal) {
        var userRating by remember { mutableStateOf(5) }
        var reviewComment by remember { mutableStateOf("") }
        var caseCategoryInput by remember { mutableStateOf(if (language == AppLanguage.ARABIC) "استشارة عقارية" else "Real Estate Consultation") }

        AlertDialog(
            onDismissRequest = { showAddReviewModal = false },
            title = {
                Text(
                    text = if (language == AppLanguage.ARABIC) "إضافة تقييم ورأي مهني" else "Add Client Review",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "تقييمك للمستشار:" else "Your Rating:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (i in 1..5) {
                            IconButton(onClick = { userRating = i }) {
                                Icon(
                                    imageVector = if (i <= userRating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                    contentDescription = "$i Stars",
                                    tint = AmberRating,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = caseCategoryInput,
                        onValueChange = { caseCategoryInput = it },
                        label = {
                            Text(
                                if (language == AppLanguage.ARABIC) "نوع القضية أو الاستشارة" else "Case or Consultation Type",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = reviewComment,
                        onValueChange = { reviewComment = it },
                        label = {
                            Text(
                                if (language == AppLanguage.ARABIC) "رأيك وتجربتك المهنية مع المستشار" else "Your Experience with Counsel",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        minLines = 3,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            if (language == AppLanguage.ARABIC) "شكراً لك! تم إرسال تقييمك وسينشر بعد التحقق." else "Thank you! Your review was submitted.",
                            Toast.LENGTH_SHORT
                        ).show()
                        showAddReviewModal = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Navy950
                    )
                ) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إرسال التقييم" else "Submit Review",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddReviewModal = false }) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إلغاء" else "Cancel",
                        color = TextSecondary
                    )
                }
            }
        )
    }
}
