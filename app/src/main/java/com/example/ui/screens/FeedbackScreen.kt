package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.model.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class FeedbackCategoryType {
    SUGGESTION,
    ISSUE_REPORT,
    LAWYER_EXPERIENCE,
    GENERAL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedType by remember { mutableStateOf(FeedbackCategoryType.SUGGESTION) }
    var rating by remember { mutableIntStateOf(5) }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var emailContactConsent by remember { mutableStateOf(true) }
    var attachedScreenshotName by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }
    var generatedTicketId by remember { mutableStateOf("FB-2026-8912") }

    val isFormValid = subject.isNotBlank() && message.trim().length >= 5

    Scaffold(
        topBar = {
            Surface(
                color = Navy900,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("feedback_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GoldLight
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Column {
                        Text(
                            text = AppStrings.feedbackAndSuggestions.get(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = if (language == AppLanguage.ARABIC) "مظلة • مركز آراء المستخدمين والدعم" else "Umbrella • User Feedback & Support",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextOnNavySecondary
                        )
                    }
                }
            }
        },
        modifier = modifier
            .fillMaxSize()
            .testTag("feedback_screen")
    ) { innerPadding ->
        AnimatedContent(
            targetState = isSubmittedSuccess,
            label = "feedback_state_transition"
        ) { submitted ->
            if (submitted) {
                // Success View State
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(OffWhite)
                        .padding(innerPadding)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("feedback_success_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess.copy(alpha = 0.15f))
                                    .border(2.dp, EmeraldSuccess, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = EmeraldSuccess,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Text(
                                text = AppStrings.feedbackSuccessTitle.get(language),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 19.sp
                                ),
                                color = Navy950
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = GoldPale,
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ConfirmationNumber,
                                        contentDescription = null,
                                        tint = Gold900,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "${if (language == AppLanguage.ARABIC) "رقم التذكرة: " else "Ticket Reference: "}#$generatedTicketId",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Gold900
                                        )
                                    )
                                }
                            }

                            Text(
                                text = AppStrings.feedbackSuccessMessage.get(language),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    lineHeight = 22.sp,
                                    fontSize = 13.sp
                                ),
                                color = TextSecondary,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = onBack,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Navy850,
                                    contentColor = GoldLight
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                                    .testTag("feedback_back_to_profile_btn")
                            ) {
                                Text(
                                    text = AppStrings.backToProfile.get(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            TextButton(
                                onClick = {
                                    isSubmittedSuccess = false
                                    subject = ""
                                    message = ""
                                    attachedScreenshotName = null
                                    rating = 5
                                }
                            ) {
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "إرسال ملاحظة أخرى" else "Submit Another Feedback",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            } else {
                // Form Input Screen
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(OffWhite)
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Subtitle Card
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Navy900),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                listOf(Navy700, Navy850)
                                            )
                                        )
                                        .border(1.dp, GoldPrimary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RateReview,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = AppStrings.feedbackAndSuggestions.get(language),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        ),
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = AppStrings.feedbackSubtitle.get(language),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = TextOnNavySecondary
                                    )
                                }
                            }
                        }
                    }

                    // Feedback Category Selector
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Category,
                                        contentDescription = null,
                                        tint = Gold900,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = AppStrings.feedbackType.get(language),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Navy950
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    FeedbackCategoryOption(
                                        category = FeedbackCategoryType.SUGGESTION,
                                        title = AppStrings.typeSuggestion.get(language),
                                        icon = Icons.Default.Lightbulb,
                                        isSelected = selectedType == FeedbackCategoryType.SUGGESTION,
                                        onSelect = { selectedType = FeedbackCategoryType.SUGGESTION }
                                    )

                                    FeedbackCategoryOption(
                                        category = FeedbackCategoryType.ISSUE_REPORT,
                                        title = AppStrings.typeIssueReport.get(language),
                                        icon = Icons.Default.BugReport,
                                        isSelected = selectedType == FeedbackCategoryType.ISSUE_REPORT,
                                        onSelect = { selectedType = FeedbackCategoryType.ISSUE_REPORT }
                                    )

                                    FeedbackCategoryOption(
                                        category = FeedbackCategoryType.LAWYER_EXPERIENCE,
                                        title = AppStrings.typeLawyerExp.get(language),
                                        icon = Icons.Default.Gavel,
                                        isSelected = selectedType == FeedbackCategoryType.LAWYER_EXPERIENCE,
                                        onSelect = { selectedType = FeedbackCategoryType.LAWYER_EXPERIENCE }
                                    )

                                    FeedbackCategoryOption(
                                        category = FeedbackCategoryType.GENERAL,
                                        title = AppStrings.typeGeneralInquiry.get(language),
                                        icon = Icons.Default.Chat,
                                        isSelected = selectedType == FeedbackCategoryType.GENERAL,
                                        onSelect = { selectedType = FeedbackCategoryType.GENERAL }
                                    )
                                }
                            }
                        }
                    }

                    // Rating / Experience Score
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = AppStrings.feedbackSatisfaction.get(language),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Navy950
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    (1..5).forEach { starIndex ->
                                        val isStarred = starIndex <= rating
                                        IconButton(
                                            onClick = { rating = starIndex },
                                            modifier = Modifier.testTag("feedback_rating_star_$starIndex")
                                        ) {
                                            Icon(
                                                imageVector = if (isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                                contentDescription = "$starIndex Stars",
                                                tint = if (isStarred) GoldPrimary else BorderMedium,
                                                modifier = Modifier.size(34.dp)
                                            )
                                        }
                                    }
                                }

                                val ratingLabel = when (rating) {
                                    5 -> if (language == AppLanguage.ARABIC) "تجربة ممتازة جداً (٥/٥)" else "Excellent Experience (5/5)"
                                    4 -> if (language == AppLanguage.ARABIC) "تجربة جيدة جداً (٤/٥)" else "Very Good Experience (4/5)"
                                    3 -> if (language == AppLanguage.ARABIC) "تجربة مقبولة (٣/٥)" else "Average Experience (3/5)"
                                    2 -> if (language == AppLanguage.ARABIC) "تحتاج لتحسينات (٢/٥)" else "Needs Improvements (2/5)"
                                    else -> if (language == AppLanguage.ARABIC) "تجربة غير مرضية (١/٥)" else "Unsatisfactory (1/5)"
                                }

                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = GoldPale
                                    ) {
                                        Text(
                                            text = ratingLabel,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            ),
                                            color = Gold900,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Form Fields: Subject & Details
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
                                // Subject
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = AppStrings.feedbackSubject.get(language),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Navy950
                                    )
                                    OutlinedTextField(
                                        value = subject,
                                        onValueChange = { subject = it },
                                        placeholder = {
                                            Text(
                                                text = AppStrings.feedbackSubjectPlaceholder.get(language),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = TextMuted
                                            )
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Navy800,
                                            unfocusedBorderColor = BorderSubtle,
                                            focusedContainerColor = OffWhite,
                                            unfocusedContainerColor = OffWhite
                                        ),
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("feedback_subject_input")
                                    )
                                }

                                // Message Details
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = AppStrings.feedbackDetails.get(language),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Navy950
                                    )
                                    OutlinedTextField(
                                        value = message,
                                        onValueChange = { message = it },
                                        placeholder = {
                                            Text(
                                                text = AppStrings.feedbackDetailsPlaceholder.get(language),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = TextMuted
                                            )
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Navy800,
                                            unfocusedBorderColor = BorderSubtle,
                                            focusedContainerColor = OffWhite,
                                            unfocusedContainerColor = OffWhite
                                        ),
                                        minLines = 4,
                                        maxLines = 6,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("feedback_details_input")
                                    )
                                    Text(
                                        text = "${message.length} / 500 ${if (language == AppLanguage.ARABIC) "حرف" else "chars"}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted,
                                        modifier = Modifier.align(Alignment.End)
                                    )
                                }

                                HorizontalDivider(color = BorderSubtle)

                                // Mock Attachment Section
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = AppStrings.feedbackAttachScreenshot.get(language),
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )

                                    if (attachedScreenshotName != null) {
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = Navy50,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderMedium),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Image,
                                                        contentDescription = null,
                                                        tint = Navy850,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    Text(
                                                        text = attachedScreenshotName ?: "",
                                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                                        color = Navy950
                                                    )
                                                }

                                                IconButton(
                                                    onClick = { attachedScreenshotName = null },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Remove",
                                                        tint = CrimsonEmergency,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        OutlinedButton(
                                            onClick = {
                                                attachedScreenshotName = "screenshot_umbrella_${System.currentTimeMillis() % 10000}.png"
                                                Toast.makeText(
                                                    context,
                                                    if (language == AppLanguage.ARABIC) "تم إرفاق لقطة الشاشة بنجاح" else "Screenshot attached",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderMedium),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("feedback_attach_btn")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AddPhotoAlternate,
                                                contentDescription = null,
                                                tint = Navy800,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (language == AppLanguage.ARABIC) "إرفاق صورة أو لقطة شاشة" else "Attach Image / Screenshot",
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                                color = Navy900
                                            )
                                        }
                                    }
                                }

                                HorizontalDivider(color = BorderSubtle)

                                // Consent Switch
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = AppStrings.feedbackContactPreference.get(language),
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "abdullaheltiby@gmail.com",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextMuted
                                        )
                                    }

                                    Switch(
                                        checked = emailContactConsent,
                                        onCheckedChange = { emailContactConsent = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = GoldPrimary
                                        ),
                                        modifier = Modifier.testTag("feedback_contact_switch")
                                    )
                                }
                            }
                        }
                    }

                    // Submit Action Button
                    item {
                        Button(
                            onClick = {
                                if (isFormValid) {
                                    isSubmitting = true
                                    coroutineScope.launch {
                                        delay(800)
                                        generatedTicketId = "FB-2026-${(1000..9999).random()}"
                                        isSubmitting = false
                                        isSubmittedSuccess = true
                                    }
                                }
                            },
                            enabled = isFormValid && !isSubmitting,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Navy950,
                                disabledContainerColor = BorderSubtle,
                                disabledContentColor = TextMuted
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("feedback_submit_btn")
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Navy950,
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "جارٍ الإرسال والتسجيل..." else "Submitting...",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = AppStrings.feedbackSubmit.get(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeedbackCategoryOption(
    category: FeedbackCategoryType,
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Navy850 else OffWhite,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) Navy850 else BorderSubtle
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("feedback_type_option_${category.name}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) GoldLight else Navy800,
                modifier = Modifier.size(18.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected) Color.White else TextPrimary,
                modifier = Modifier.weight(1f)
            )

            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = GoldPrimary,
                    unselectedColor = BorderMedium
                )
            )
        }
    }
}
