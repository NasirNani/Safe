package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.Lawyer
import com.example.ui.theme.*

@Composable
fun LawyerCard(
    lawyer: Lawyer,
    language: AppLanguage,
    onViewProfile: (Lawyer) -> Unit,
    onBookConsultation: (Lawyer) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Card(
        onClick = { onViewProfile(lawyer) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
        modifier = modifier
            .fillMaxWidth()
            .testTag("lawyer_card_${lawyer.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Top row: Avatar & Core Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(lawyer.avatarColorHex),
                                    Navy900
                                )
                            )
                        )
                        .border(1.5.dp, colors.goldPrimary, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (lawyer.avatarDrawableRes != null) {
                        Image(
                            painter = painterResource(id = lawyer.avatarDrawableRes),
                            contentDescription = lawyer.getName(language),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = lawyer.getName(language).split(" ").take(2).mapNotNull { it.firstOrNull()?.toString() }.joinToString(""),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = colors.goldLight
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name, Title, Bar
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = lawyer.getName(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = colors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (lawyer.isVerified) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Lawyer",
                                tint = colors.goldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = lawyer.getTitle(language),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = if (colors.isDark) colors.goldLight else Navy700,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = colors.textMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = lawyer.getGovernorate(language),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = colors.textSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Specs badges row: Rating, Experience, Category
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.surfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = AmberRating,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "${lawyer.rating}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = colors.textPrimary
                    )
                    Text(
                        text = "(${lawyer.reviewsCount})",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textMuted
                    )
                }

                // Experience
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkHistory,
                        contentDescription = "Experience",
                        tint = if (colors.isDark) colors.goldLight else Navy600,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = if (language == AppLanguage.ARABIC) "${lawyer.experienceYears} سنة خبرة" else "${lawyer.experienceYears} yrs exp.",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = colors.textSecondary
                    )
                }

                // Category pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(colors.goldPale)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = lawyer.category.getDisplayName(language),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        color = colors.goldText
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom row: Consultation Fee & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.consultationFee.get(language),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = colors.textMuted
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "${lawyer.consultationFeeEgp}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (colors.isDark) colors.goldLight else Navy900,
                                fontSize = 17.sp
                            )
                        )
                        Text(
                            text = AppStrings.egp.get(language),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            color = colors.goldPrimary,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { onViewProfile(lawyer) },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (colors.isDark) colors.goldPrimary.copy(alpha = 0.6f) else Navy700),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text(
                            text = AppStrings.viewProfile.get(language),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (colors.isDark) colors.goldLight else Navy850
                        )
                    }

                    Button(
                        onClick = { onBookConsultation(lawyer) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primaryButtonBg,
                            contentColor = colors.primaryButtonText
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text(
                            text = AppStrings.bookConsultation.get(language),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
