package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.model.*
import com.example.ui.components.LawyerCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCaseFlowScreen(
    language: AppLanguage,
    initialCategory: PracticeAreaCategory?,
    onBack: () -> Unit,
    onViewLawyerProfile: (Lawyer) -> Unit,
    onBookConsultation: (Lawyer) -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableStateOf(1) } // 1: Info, 2: Urgency & Budget, 3: Results
    var selectedCategory by remember { mutableStateOf(initialCategory ?: PracticeAreaCategory.CIVIL) }
    var caseTitle by remember { mutableStateOf("") }
    var caseDescription by remember { mutableStateOf("") }
    var selectedGovernorate by remember { mutableStateOf("القاهرة (Cairo)") }
    var selectedUrgency by remember { mutableStateOf(UrgencyLevel.NORMAL) }
    var budgetEgp by remember { mutableStateOf(2500f) }
    var attachedFiles by remember { mutableStateOf(listOf<String>()) }
    var isSubmitting by remember { mutableStateOf(false) }

    val governorates = listOf(
        "القاهرة (Cairo)",
        "الجيزة (Giza)",
        "الإسكندرية (Alexandria)",
        "الدقهلية - المنصورة (Mansoura)",
        "الشرقية - الزقازيق (Zagazig)",
        "القليوبية - بنها (Banha)",
        "البحر الأحمر - الغردقة (Hurghada)"
    )

    // Matched lawyers computation for step 3
    val matchedLawyers = remember(selectedCategory) {
        MockData.lawyers.filter { it.category == selectedCategory }.ifEmpty {
            MockData.lawyers.take(3)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (step == 3) {
                            AppStrings.matchingSuccessTitle.get(language)
                        } else {
                            AppStrings.postCaseTitle.get(language)
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (step > 1 && step < 3) step-- else onBack()
                        },
                        modifier = Modifier.testTag("post_case_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Navy900
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceCard
                )
            )
        },
        modifier = modifier
            .fillMaxSize()
            .testTag("post_case_flow_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(OffWhite)
                .padding(innerPadding)
        ) {
            // Step Progress Indicator (if not on results)
            if (step < 3) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceCard)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (step >= 1) GoldPrimary else BorderSubtle)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (step >= 2) GoldPrimary else BorderSubtle)
                    )
                }
            }

            AnimatedContent(
                targetState = step,
                label = "step_transition"
            ) { currentStep ->
                when (currentStep) {
                    1 -> {
                        // Step 1: Category, Title, Description, Governorate
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Text(
                                    text = AppStrings.postCaseStep1.get(language),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp
                                    ),
                                    color = Navy900
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "حدد التخصص واكتب ملخصاً للمشكلة القانونية بدقة" else "Select category and describe your legal issue",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }

                            // Category Selection Chips
                            item {
                                Text(
                                    text = AppStrings.caseCategoryLabel.get(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    PracticeAreaCategory.values().forEach { cat ->
                                        val isSelected = selectedCategory == cat
                                        Card(
                                            onClick = { selectedCategory = cat },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (isSelected) Navy850 else SurfaceCard
                                            ),
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                if (isSelected) GoldPrimary else BorderSubtle
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("post_case_category_${cat.id}")
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = cat.getDisplayName(language),
                                                        style = MaterialTheme.typography.titleSmall.copy(
                                                            fontWeight = FontWeight.Bold
                                                        ),
                                                        color = if (isSelected) GoldLight else TextPrimary
                                                    )
                                                    Text(
                                                        text = cat.getDescription(language),
                                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                                        color = if (isSelected) TextOnNavySecondary else TextSecondary,
                                                        maxLines = 1
                                                    )
                                                }
                                                RadioButton(
                                                    selected = isSelected,
                                                    onClick = { selectedCategory = cat },
                                                    colors = RadioButtonDefaults.colors(
                                                        selectedColor = GoldPrimary,
                                                        unselectedColor = TextMuted
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Case Title
                            item {
                                Text(
                                    text = AppStrings.caseTitleLabel.get(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = caseTitle,
                                    onValueChange = { caseTitle = it },
                                    placeholder = {
                                        Text(
                                            text = AppStrings.caseTitlePlaceholder.get(language),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextMuted
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = SurfaceCard,
                                        unfocusedContainerColor = SurfaceCard,
                                        focusedBorderColor = Navy850,
                                        unfocusedBorderColor = BorderSubtle
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("post_case_title_input")
                                )
                            }

                            // Case Description
                            item {
                                Text(
                                    text = AppStrings.caseDescLabel.get(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = caseDescription,
                                    onValueChange = { caseDescription = it },
                                    placeholder = {
                                        Text(
                                            text = AppStrings.caseDescPlaceholder.get(language),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextMuted
                                        )
                                    },
                                    minLines = 4,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = SurfaceCard,
                                        unfocusedContainerColor = SurfaceCard,
                                        focusedBorderColor = Navy850,
                                        unfocusedBorderColor = BorderSubtle
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("post_case_desc_input")
                                )
                            }

                            // Governorate Selection
                            item {
                                Text(
                                    text = AppStrings.governorateLabel.get(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    governorates.take(4).forEach { gov ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (selectedGovernorate == gov) Navy100 else SurfaceCard)
                                                .clickable { selectedGovernorate = gov }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            RadioButton(
                                                selected = selectedGovernorate == gov,
                                                onClick = { selectedGovernorate = gov },
                                                colors = RadioButtonDefaults.colors(selectedColor = Navy850)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = gov,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = TextPrimary
                                            )
                                        }
                                    }
                                }
                            }

                            // Next Button
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { step = 2 },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Navy850,
                                        contentColor = GoldLight
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("post_case_next_btn")
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "المتابعة للخطوة التالية" else "Continue to Next Step",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }

                    2 -> {
                        // Step 2: Urgency, Budget & Documents Upload
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Text(
                                    text = AppStrings.postCaseStep2.get(language),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp
                                    ),
                                    color = Navy900
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "حدد درجة الاستعجال والميزانية المقترحة لأتعاب المحاماة" else "Set urgency level and expected legal fees budget",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }

                            // Urgency Selection Cards
                            item {
                                Text(
                                    text = AppStrings.urgencyLabel.get(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    UrgencyLevel.values().forEach { urg ->
                                        val isSelected = selectedUrgency == urg
                                        Card(
                                            onClick = { selectedUrgency = urg },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (isSelected) Navy50 else SurfaceCard
                                            ),
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                if (isSelected) Navy850 else BorderSubtle
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = when (urg) {
                                                        UrgencyLevel.NORMAL -> Icons.Default.Schedule
                                                        UrgencyLevel.URGENT -> Icons.Default.Bolt
                                                        UrgencyLevel.EMERGENCY -> Icons.Default.Emergency
                                                    },
                                                    contentDescription = null,
                                                    tint = when (urg) {
                                                        UrgencyLevel.NORMAL -> Navy700
                                                        UrgencyLevel.URGENT -> AmberRating
                                                        UrgencyLevel.EMERGENCY -> CrimsonEmergency
                                                    },
                                                    modifier = Modifier.size(22.dp)
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Text(
                                                    text = if (language == AppLanguage.ARABIC) urg.nameAr else urg.nameEn,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                    ),
                                                    color = TextPrimary,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                RadioButton(
                                                    selected = isSelected,
                                                    onClick = { selectedUrgency = urg },
                                                    colors = RadioButtonDefaults.colors(selectedColor = Navy850)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Budget Slider
                            item {
                                Text(
                                    text = AppStrings.budgetLabel.get(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${budgetEgp.toInt()} ${AppStrings.egp.get(language)}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Gold800
                                        )
                                    )
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "ميزانية تقريبية للأتعاب" else "Approx. Retainer",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextMuted
                                    )
                                }
                                Slider(
                                    value = budgetEgp,
                                    onValueChange = { budgetEgp = it },
                                    valueRange = 500f..15000f,
                                    steps = 29,
                                    colors = SliderDefaults.colors(
                                        thumbColor = GoldPrimary,
                                        activeTrackColor = Navy850,
                                        inactiveTrackColor = Navy100
                                    ),
                                    modifier = Modifier.testTag("post_case_budget_slider")
                                )
                            }

                            // Attachments simulation
                            item {
                                Text(
                                    text = AppStrings.uploadDocs.get(language),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Card(
                                    onClick = {
                                        attachedFiles = attachedFiles + listOf(
                                            if (attachedFiles.isEmpty()) "صورة_عقد_النزاع_المؤرخ_2024.pdf"
                                            else "محضر_اثبات_حالة_قسم_الشرطة.jpg"
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("post_case_attach_btn")
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AttachFile,
                                            contentDescription = null,
                                            tint = Navy700
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = if (language == AppLanguage.ARABIC) "اضغط لإرفاق عقود، إنذارات، أو محاضر" else "Tap to attach contracts, notices, or reports",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = TextPrimary
                                            )
                                            Text(
                                                text = if (language == AppLanguage.ARABIC) "PDF, JPG, PNG حتى 25 ميجابايت" else "PDF, JPG, PNG up to 25MB",
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                                color = TextMuted
                                            )
                                        }
                                    }
                                }

                                if (attachedFiles.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        attachedFiles.forEach { file ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Navy50)
                                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.CheckCircle,
                                                        contentDescription = null,
                                                        tint = EmeraldSuccess,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Text(
                                                        text = file,
                                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                                        color = TextPrimary
                                                    )
                                                }
                                                IconButton(
                                                    onClick = { attachedFiles = attachedFiles.filter { it != file } },
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Remove",
                                                        tint = TextMuted,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Submit & Match Button
                            item {
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        step = 3
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GoldPrimary,
                                        contentColor = Navy950
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .testTag("post_case_submit_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = AppStrings.submitCaseBtn.get(language),
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }

                    3 -> {
                        // Step 3: Matched Lawyers Results Screen
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Celebration / Match Summary Card
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Navy900, Navy800)
                                            )
                                        )
                                        .padding(18.dp)
                                        .testTag("matching_results_header")
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                                .background(GoldPrimary.copy(alpha = 0.2f))
                                                .border(1.5.dp, GoldPrimary, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Success",
                                                tint = GoldLight,
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = AppStrings.matchingSuccessTitle.get(language),
                                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Color.White
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = AppStrings.matchingSuccessSubtitle.get(language),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextOnNavySecondary,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Navy700
                                            ) {
                                                Text(
                                                    text = if (language == AppLanguage.ARABIC) "التخصص: ${selectedCategory.nameAr}" else "Category: ${selectedCategory.nameEn}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = GoldLight,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Navy700
                                            ) {
                                                Text(
                                                    text = if (language == AppLanguage.ARABIC) "المحافظة: $selectedGovernorate" else "Gov: $selectedGovernorate",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = TextOnNavy,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Matched Lawyers List Header
                            item {
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "المحامون المرشحون لقضيتك (${matchedLawyers.size})" else "Matched Counsel Candidates (${matchedLawyers.size})",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                            }

                            // Matched Lawyer Cards with Match Score
                            items(matchedLawyers) { lawyer ->
                                val matchScore = if (lawyer.category == selectedCategory) "98%" else "94%"
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // Match Score tag
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Bolt,
                                            contentDescription = null,
                                            tint = AmberRating,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = if (language == AppLanguage.ARABIC) "نسبة توافق التخصص والخبرة: $matchScore" else "Expertise & Location Match: $matchScore",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Navy700
                                        )
                                    }

                                    LawyerCard(
                                        lawyer = lawyer,
                                        language = language,
                                        onViewProfile = onViewLawyerProfile,
                                        onBookConsultation = onBookConsultation
                                    )
                                }
                            }

                            // Done action
                            item {
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = onBack,
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Navy850),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("post_case_done_btn")
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ARABIC) "العودة للرئيسية" else "Return to Home",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                                        color = Navy850
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
