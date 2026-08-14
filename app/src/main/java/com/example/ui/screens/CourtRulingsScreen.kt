package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MockData
import com.example.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtRulingsScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var selectedCategory by remember { mutableStateOf<PracticeAreaCategory?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var expandedRulingId by remember { mutableStateOf<String?>(null) }

    // Initial default recent searches for Egyptian Court of Cassation
    val initialRecentSearches = remember(language) {
        if (language == AppLanguage.ARABIC) {
            listOf(
                "صحة ونفاذ عقد البيع",
                "الشرط الجزائي والتعويض الاتفاقي",
                "بطلان إعلان صحيفة الدعوى",
                "طرد للغصب وإنهاء العلاقة الإيجارية",
                "تزوير صلب السند الإذني",
                "المحررات العرفية وحجيتها"
            )
        } else {
            listOf(
                "Sale Contract Enforcement",
                "Penalty Clause & Agreed Damages",
                "Nullity of Lawsuit Service",
                "Eviction for Unlawful Possession",
                "Promissory Note Forgery",
                "Authenticity of Customary Deeds"
            )
        }
    }

    val recentSearches = remember { mutableStateListOf<String>().apply { addAll(initialRecentSearches) } }

    // Popular trending queries for discovery
    val popularLegalQueries = remember(language) {
        if (language == AppLanguage.ARABIC) {
            listOf(
                "مسؤولية الشريك المتضامن",
                "حضانة الصغير ونفقة الأقارب",
                "شيك بدون رصيد وتنازل الشاكي",
                "الطعن بالنقض للمرة الثانية",
                "سقوط الحق في التقادم"
            )
        } else {
            listOf(
                "Partner Liability in LLC",
                "Child Custody & Alimony",
                "Cheque without Balance",
                "Second Appeal in Cassation",
                "Statute of Limitations"
            )
        }
    }

    fun executeSearch(query: String) {
        val trimmed = query.trim()
        searchQuery = trimmed
        if (trimmed.isNotBlank()) {
            recentSearches.remove(trimmed)
            recentSearches.add(0, trimmed)
            if (recentSearches.size > 10) {
                recentSearches.removeAt(recentSearches.lastIndex)
            }
        }
    }

    fun removeRecentSearch(query: String) {
        recentSearches.remove(query)
    }

    fun clearAllRecentSearches() {
        recentSearches.clear()
        Toast.makeText(
            context,
            if (language == AppLanguage.ARABIC) "تم مسح سجل البحث بالكامل" else "Search history cleared",
            Toast.LENGTH_SHORT
        ).show()
    }

    val filteredRulings = remember(selectedCategory, searchQuery) {
        MockData.courtRulings.filter { ruling ->
            val matchesCategory = selectedCategory == null || ruling.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    ruling.appealNumber.contains(searchQuery, ignoreCase = true) ||
                    ruling.getPrinciple(language).contains(searchQuery, ignoreCase = true) ||
                    ruling.getSummary(language).contains(searchQuery, ignoreCase = true) ||
                    ruling.getKeywords(language).any { it.contains(searchQuery, ignoreCase = true) }
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
                    text = AppStrings.rulingsTitle.get(language),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = AppStrings.rulingsSubtitle.get(language),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar with Keyboard Action
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = AppStrings.searchRulings.get(language),
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
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchQuery.isNotBlank()) {
                                executeSearch(searchQuery)
                            }
                            keyboardController?.hide()
                        }
                    ),
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
                        .testTag("rulings_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Chips
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
                                    text = if (language == AppLanguage.ARABIC) "كافة الدوائر" else "All Chambers",
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
            .testTag("court_rulings_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(OffWhite)
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. RECENT SEARCHES SECTION (Prominently displayed to improve navigation & recurrent queries)
            if (recentSearches.isNotEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("recent_searches_section")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Section Header with History Icon & Clear All
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
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(Navy100),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.History,
                                            contentDescription = null,
                                            tint = Navy900,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    Text(
                                        text = AppStrings.recentSearches.get(language),
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Navy950
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Navy50,
                                        modifier = Modifier.padding(start = 2.dp)
                                    ) {
                                        Text(
                                            text = "${recentSearches.size}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            ),
                                            color = Navy800,
                                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                TextButton(
                                    onClick = { clearAllRecentSearches() },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                    modifier = Modifier.testTag("clear_recent_searches_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = AppStrings.clearAll.get(language),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = TextMuted
                                    )
                                }
                            }

                            // Horizontal scrollable recent search pills with individual delete buttons
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                itemsIndexed(recentSearches) { index, query ->
                                    val isCurrentActive = searchQuery.equals(query, ignoreCase = true)

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isCurrentActive) Navy900 else Navy50,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isCurrentActive) GoldPrimary else BorderSubtle
                                        ),
                                        modifier = Modifier
                                            .testTag("recent_search_chip_$index")
                                            .clickable {
                                                executeSearch(query)
                                                keyboardController?.hide()
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(
                                                start = 10.dp,
                                                end = 4.dp,
                                                top = 6.dp,
                                                bottom = 6.dp
                                            ),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = null,
                                                tint = if (isCurrentActive) GoldLight else Navy700,
                                                modifier = Modifier.size(13.dp)
                                            )

                                            Text(
                                                text = query,
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = if (isCurrentActive) FontWeight.Bold else FontWeight.Medium
                                                ),
                                                color = if (isCurrentActive) Color.White else Navy900
                                            )

                                            // Delete individual search item
                                            IconButton(
                                                onClick = { removeRecentSearch(query) },
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .testTag("remove_recent_search_$index")
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Remove query",
                                                    tint = if (isCurrentActive) GoldLight.copy(alpha = 0.8f) else TextMuted,
                                                    modifier = Modifier.size(13.dp)
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

            // 2. TRENDING LEGAL TOPICS / POPULAR SEARCHES (When user is exploring)
            if (searchQuery.isBlank()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Gold900,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = AppStrings.popularSearches.get(language),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Navy900
                            )
                        }

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(popularLegalQueries) { popularQuery ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = GoldPale,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldBorder),
                                    modifier = Modifier
                                        .clickable {
                                            executeSearch(popularQuery)
                                            keyboardController?.hide()
                                        }
                                        .testTag("popular_query_${popularQuery.hashCode()}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = popularQuery,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.SemiBold,
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
            }

            // 3. SEARCH RESULTS HEADER & ACTIVE FILTERS
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) {
                            "المبادئ القضائية (${filteredRulings.size})"
                        } else {
                            "Judicial Principles (${filteredRulings.size})"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Navy950
                    )

                    if (searchQuery.isNotBlank() || selectedCategory != null) {
                        TextButton(
                            onClick = {
                                searchQuery = ""
                                selectedCategory = null
                            },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = Gold900,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ARABIC) "إعادة ضبط الفلاتر" else "Reset Filters",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Gold900
                            )
                        }
                    }
                }
            }

            // 4. EMPTY STATE WHEN NO RULINGS MATCH
            if (filteredRulings.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(Navy100),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = Navy700,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Text(
                                text = AppStrings.noRulingsFound.get(language),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Navy950
                            )

                            Text(
                                text = AppStrings.tryRecentSearch.get(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Suggestions from recent searches
                            if (recentSearches.isNotEmpty()) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    recentSearches.take(2).forEach { fallbackQuery ->
                                        OutlinedButton(
                                            onClick = { executeSearch(fallbackQuery) },
                                            shape = RoundedCornerShape(8.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderMedium)
                                        ) {
                                            Text(
                                                text = fallbackQuery,
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = Navy900
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. RULINGS LIST ITEMS
            items(filteredRulings) { ruling ->
                val isExpanded = expandedRulingId == ruling.id

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ruling_card_${ruling.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header: Chamber badge & Appeal number
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Navy100)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = ruling.getChamber(language),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = Navy850
                                )
                            }

                            Text(
                                text = "${AppStrings.appealNumber.get(language)} ${ruling.appealNumber} ${AppStrings.judicialYear.get(language)} ${ruling.judicialYear}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                ),
                                color = Gold800
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Principle
                        Text(
                            text = ruling.getPrinciple(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                lineHeight = 22.sp
                            ),
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Session date
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = ruling.sessionDate,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = TextSecondary
                            )
                        }

                        // Expandable Full Reasoning
                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Navy50,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Navy100),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = AppStrings.rulingDetails.get(language),
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Navy900
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = ruling.getSummary(language),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                lineHeight = 20.sp,
                                                color = TextPrimary
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Clickable Keywords (Tapping a keyword searches for it & adds to recent searches)
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ruling.getKeywords(language).forEach { kw ->
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(GoldPale)
                                                .border(0.5.dp, GoldBorder, RoundedCornerShape(4.dp))
                                                .clickable {
                                                    executeSearch(kw)
                                                    keyboardController?.hide()
                                                }
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "#$kw",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Gold900
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Actions Row: Expand/Collapse, Copy, Share
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    expandedRulingId = if (isExpanded) null else ruling.id
                                },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = if (isExpanded) {
                                        if (language == AppLanguage.ARABIC) "إخفاء التفاصيل" else "Hide Details"
                                    } else {
                                        if (language == AppLanguage.ARABIC) "عرض أسباب الحكم كاملة" else "Show Full Reasoning"
                                    },
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Navy800
                                )
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Navy800,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Ruling", "${ruling.getPrinciple(language)}\n\n${ruling.getSummary(language)}")
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(
                                            context,
                                            if (language == AppLanguage.ARABIC) "تم نسخ المبدأ القضائي بنجاح" else "Principle copied to clipboard",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = AppStrings.copyRuling.get(language),
                                        tint = Navy700,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        Toast.makeText(
                                            context,
                                            if (language == AppLanguage.ARABIC) "مشاركة المبدأ القانوني مع الزملاء" else "Sharing principle...",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = AppStrings.shareRuling.get(language),
                                        tint = Navy700,
                                        modifier = Modifier.size(18.dp)
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
