package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.PracticeAreaCategory
import com.example.ui.theme.*

@Composable
fun getCategoryIcon(category: PracticeAreaCategory): ImageVector {
    return when (category) {
        PracticeAreaCategory.CIVIL -> Icons.Default.Gavel
        PracticeAreaCategory.COMMERCIAL -> Icons.Default.Business
        PracticeAreaCategory.CRIMINAL -> Icons.Default.Shield
        PracticeAreaCategory.ADMINISTRATIVE -> Icons.Default.AccountBalance
        PracticeAreaCategory.COMPENSATION -> Icons.Default.Handshake
    }
}

@Composable
fun getCategoryGradient(category: PracticeAreaCategory): Pair<Color, Color> {
    return when (category) {
        PracticeAreaCategory.CIVIL -> Pair(Color(0xFF1E3A8A), Color(0xFF0F172A))
        PracticeAreaCategory.COMMERCIAL -> Pair(Color(0xFF78350F), Color(0xFF451A03))
        PracticeAreaCategory.CRIMINAL -> Pair(Color(0xFF881337), Color(0xFF4C0519))
        PracticeAreaCategory.ADMINISTRATIVE -> Pair(Color(0xFF14532D), Color(0xFF052E16))
        PracticeAreaCategory.COMPENSATION -> Pair(Color(0xFF581C87), Color(0xFF3B0764))
    }
}

@Composable
fun CategoryCard(
    category: PracticeAreaCategory,
    language: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = getCategoryIcon(category)
    val (colorStart, colorEnd) = getCategoryGradient(category)
    val colors = AppTheme.colors

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
        modifier = modifier
            .fillMaxWidth()
            .testTag("category_card_${category.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon container with gradient
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(listOf(colorStart, colorEnd))
                        )
                        .border(1.dp, colors.goldPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = category.getDisplayName(language),
                        tint = colors.goldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Active cases count badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = colors.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
                ) {
                    Text(
                        text = if (language == AppLanguage.ARABIC) "${category.activeCasesCount} محامٍ" else "${category.activeCasesCount} Lawyers",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = if (colors.isDark) colors.goldLight else Navy700,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = category.getDisplayName(language),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                text = category.getDescription(language),
                style = MaterialTheme.typography.bodySmall.copy(
                    lineHeight = 16.sp
                ),
                color = colors.textSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subcategory chips (first 2)
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                category.getSubcategories(language).take(2).forEach { sub ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(colors.goldPale)
                            .border(0.5.dp, colors.goldBorder.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = sub,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = colors.goldText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
