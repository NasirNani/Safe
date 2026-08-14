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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.model.*
import com.example.ui.components.ContractTemplateSkeleton
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractTemplatesScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf<PracticeAreaCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var previewingContract by remember { mutableStateOf<ContractTemplate?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Simulate initial loading effect for smooth UX
    LaunchedEffect(Unit) {
        delay(600)
        isLoading = false
    }

    // Refresh loading state when switching categories
    LaunchedEffect(selectedCategory) {
        isLoading = true
        delay(350)
        isLoading = false
    }

    val filteredTemplates = remember(selectedCategory, searchQuery) {
        MockData.contractTemplates.filter { template ->
            val matchesCategory = selectedCategory == null || template.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    template.getTitle(language).contains(searchQuery, ignoreCase = true) ||
                    template.getDescription(language).contains(searchQuery, ignoreCase = true) ||
                    template.getLegalBasis(language).contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCard)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = AppStrings.contractsTitle.get(language),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = AppStrings.contractsSubtitle.get(language),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = AppStrings.searchContracts.get(language),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Navy700
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = TextMuted
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Navy800,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = OffWhite,
                        unfocusedContainerColor = OffWhite
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contracts_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = {
                                Text(
                                    text = if (language == AppLanguage.ARABIC) "جميع النماذج" else "All Templates",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (selectedCategory == null) FontWeight.Bold else FontWeight.Normal
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

                    items(PracticeAreaCategory.values()) { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = if (isSelected) null else cat },
                            label = {
                                Text(
                                    text = cat.getDisplayName(language),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
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
        },
        modifier = modifier
            .fillMaxSize()
            .testTag("contracts_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(OffWhite)
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Count Header
            item {
                Text(
                    text = if (isLoading) {
                        if (language == AppLanguage.ARABIC) "جاري مراجعة وتحميل صياغات العقود..." else "Loading legal contract templates..."
                    } else {
                        if (language == AppLanguage.ARABIC) "النماذج القانونية المتاحة (${filteredTemplates.size})" else "Available Legal Templates (${filteredTemplates.size})"
                    },
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TextSecondary
                )
            }

            // SKELETON LOADING OR REAL DATA ITEMS
            if (isLoading) {
                items(3) {
                    ContractTemplateSkeleton()
                }
            } else if (filteredTemplates.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "لم يتم العثور على نماذج عقود مطابقة" else "No matching contract templates found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            } else {
                items(filteredTemplates) { template ->
                    Card(
                        onClick = { previewingContract = template },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contract_card_${template.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
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

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(EmeraldSuccess.copy(alpha = 0.12f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = null,
                                            tint = EmeraldSuccess,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = AppStrings.verifiedCompliance.get(language),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 9.sp
                                            ),
                                            color = EmeraldSuccess
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = template.getTitle(language),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = template.getDescription(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Legal Basis
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Gavel,
                                    contentDescription = null,
                                    tint = Navy600,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = template.getLegalBasis(language),
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = Navy700,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Footer stats & actions
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${template.clausesCount} ${AppStrings.clausesCount.get(language)}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                        color = TextMuted
                                    )
                                    Text(
                                        text = "•",
                                        color = TextMuted
                                    )
                                    Text(
                                        text = "${template.downloadsCount} ${AppStrings.downloads.get(language)}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                        color = TextMuted
                                    )
                                }

                                Button(
                                    onClick = { previewingContract = template },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Navy850,
                                        contentColor = GoldLight
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text(
                                        text = AppStrings.previewContract.get(language),
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Full Contract Preview Dialog
    previewingContract?.let { template ->
        AlertDialog(
            onDismissRequest = { previewingContract = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = GoldPrimary
                    )
                    Text(
                        text = template.getTitle(language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldPale,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "السند القانوني: ${template.getLegalBasis(language)}",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = Gold900,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    item {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "أهم البنود والشروط القانونية المضمنة:" else "Included Legal Clauses:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }

                    items(template.getClauses(language)) { clause ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "✓", color = EmeraldSuccess, fontWeight = FontWeight.Bold)
                            Text(
                                text = clause,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) "نص الديباجة والافتتاحية الرسمية:" else "Official Legal Text Excerpt:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Navy50,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Navy100),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = template.getSampleText(language),
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                color = TextPrimary,
                                modifier = Modifier.padding(10.dp)
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
                            if (language == AppLanguage.ARABIC) "تم بدء تحميل النموذج (DOCX / PDF) بنجاح" else "Downloading template (DOCX / PDF)...",
                            Toast.LENGTH_LONG
                        ).show()
                        previewingContract = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Navy950)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = AppStrings.downloadContract.get(language),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { previewingContract = null }) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "إغلاق" else "Close",
                        color = TextSecondary
                    )
                }
            }
        )
    }
}
