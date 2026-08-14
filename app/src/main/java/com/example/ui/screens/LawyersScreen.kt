package com.example.ui.screens

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.model.*
import com.example.ui.components.LawyerCard
import com.example.ui.components.LawyerCardSkeleton
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LawyersScreen(
    language: AppLanguage,
    initialCategory: PracticeAreaCategory?,
    onNavigateToPostCase: () -> Unit,
    onViewLawyerProfile: (Lawyer) -> Unit,
    onBookConsultation: (Lawyer) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<PracticeAreaCategory?>(initialCategory) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedGovernorate by remember { mutableStateOf("all") }
    var isLoading by remember { mutableStateOf(true) }

    // Simulate initial network fetch or search debounce fetching state
    LaunchedEffect(Unit) {
        delay(650)
        isLoading = false
    }

    // Refresh loading state when switching categories
    LaunchedEffect(selectedCategory) {
        isLoading = true
        delay(350)
        isLoading = false
    }

    // Filter lawyers
    val filteredLawyers = remember(selectedCategory, searchQuery, selectedGovernorate) {
        MockData.lawyers.filter { lawyer ->
            val matchesCategory = selectedCategory == null || lawyer.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    lawyer.getName(language).contains(searchQuery, ignoreCase = true) ||
                    lawyer.getTitle(language).contains(searchQuery, ignoreCase = true) ||
                    lawyer.getGovernorate(language).contains(searchQuery, ignoreCase = true) ||
                    lawyer.getBio(language).contains(searchQuery, ignoreCase = true)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.ARABIC) "دليل ومطابقة المحامين" else "Lawyer Matching Directory",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = if (language == AppLanguage.ARABIC) "محامون معتمدون أمام محاكم النقض والاستئناف" else "Certified trial & cassation attorneys",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    // Post Case quick button
                    Button(
                        onClick = onNavigateToPostCase,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Navy950
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("lawyers_post_case_top_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ARABIC) "طرح قضية" else "Post Case",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = AppStrings.searchLawyerPlaceholder.get(language),
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
                        .testTag("lawyer_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Filter Chips Row
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
                                    text = AppStrings.allSpecialties.get(language),
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
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedCategory == null,
                                borderColor = BorderSubtle,
                                selectedBorderColor = Navy850
                            ),
                            modifier = Modifier.testTag("chip_all_specialties")
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
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = BorderSubtle,
                                selectedBorderColor = Navy850
                            ),
                            modifier = Modifier.testTag("chip_category_${cat.id}")
                        )
                    }
                }
            }
        },
        modifier = modifier
            .fillMaxSize()
            .testTag("lawyers_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(OffWhite)
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Smart Matching Banner
            item {
                Card(
                    onClick = onNavigateToPostCase,
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Navy850
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("smart_matching_prompt_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == AppLanguage.ARABIC) "هل تحتاج مطابقة دقيقة لقضيتك؟" else "Need precise case matching?",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                color = GoldLight
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "اطرح تفاصيل المشكلة وسنرشح لك أفضل ٣ محامين متخصصين بالميزانية المحددة" else "Post your issue details and we will match 3 top lawyers in budget",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = TextOnNavySecondary
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Smart Match",
                            tint = GoldPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            // Results count
            item {
                Text(
                    text = if (isLoading) {
                        if (language == AppLanguage.ARABIC) "جاري استرجاع المحامين المعتمدين..." else "Fetching certified attorneys..."
                    } else {
                        if (language == AppLanguage.ARABIC) "المحامون المتاحون (${filteredLawyers.size})" else "Available Lawyers (${filteredLawyers.size})"
                    },
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TextSecondary
                )
            }

            // SKELETON LOADING OR REAL DATA ITEMS
            if (isLoading) {
                // Display 4 shimmering skeleton cards while loading
                items(4) {
                    LawyerCardSkeleton()
                }
            } else if (filteredLawyers.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.PersonSearch,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "لم يتم العثور على محامين مطابقين للبحث" else "No matching lawyers found",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                }
            } else {
                items(filteredLawyers) { lawyer ->
                    LawyerCard(
                        lawyer = lawyer,
                        language = language,
                        onViewProfile = onViewLawyerProfile,
                        onBookConsultation = onBookConsultation
                    )
                }
            }
        }
    }
}
