package com.example.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Rule
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Shield
import androidx.compose.ui.graphics.vector.ImageVector

enum class PracticeAreaCategory(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val iconName: String,
    val subcategoriesAr: List<String>,
    val subcategoriesEn: List<String>,
    val activeCasesCount: Int
) {
    CIVIL(
        id = "civil",
        nameAr = "مدني",
        nameEn = "Civil",
        descriptionAr = "عقود البيع والإيجار، الملكية، نزاعات العقارات والشفعة، صحة التوقيع والإشهار العقاري",
        descriptionEn = "Sales & lease contracts, real estate disputes, property registration, title validation",
        iconName = "gavel",
        subcategoriesAr = listOf("عقود وإيجارات", "منازعات عقارية", "صحة توقيع ونفاذ", "شفعة وحيازة"),
        subcategoriesEn = listOf("Lease & Contracts", "Real Estate Disputes", "Signature Validation", "Preemption & Possession"),
        activeCasesCount = 142
    ),
    COMMERCIAL(
        id = "commercial",
        nameAr = "تجاري",
        nameEn = "Commercial",
        descriptionAr = "تأسيس الشركات، نزاعات الشركاء، الشيكات والأوراق التجارية، التحكيم التجاري، والإفلاس",
        descriptionEn = "Company incorporation, partner disputes, commercial papers & checks, arbitration",
        iconName = "business",
        subcategoriesAr = listOf("تأسيس شركات", "شيكات وكمبيالات", "تحكيم تجاري", "علامات تجارية"),
        subcategoriesEn = listOf("Company Setup", "Checks & Promissory Notes", "Commercial Arbitration", "Trademarks"),
        activeCasesCount = 98
    ),
    CRIMINAL(
        id = "criminal",
        nameAr = "جنائي",
        nameEn = "Criminal",
        descriptionAr = "جنح وجنايات، قضايا الأموال العامة، الشيكات بدون رصيد، الدفاع الجنائي، والطعن بالنقض",
        descriptionEn = "Misdemeanors, felonies, public funds, bounced checks, criminal defense & cassation",
        iconName = "shield",
        subcategoriesAr = listOf("جنح وجنايات", "أموال عامة", "نقض جنائي", "جرائم إلكترونية"),
        subcategoriesEn = listOf("Misdemeanors & Felonies", "Public Funds", "Criminal Cassation", "Cybercrimes"),
        activeCasesCount = 85
    ),
    ADMINISTRATIVE(
        id = "administrative",
        nameAr = "إداري",
        nameEn = "Administrative",
        descriptionAr = "قضايا مجلس الدولة، المحاكم التأديبية، تراخيص البناء والأنشطة، طعون الموظفين والقرارات الإدارية",
        descriptionEn = "State Council (Majlis Al-Dawla), disciplinary courts, building licenses, employee appeals",
        iconName = "account_balance",
        subcategoriesAr = listOf("مجلس الدولة", "قرارات إدارية وتراخيص", "محاكم تأديبية", "نزاعات ضرائب ورسوم"),
        subcategoriesEn = listOf("State Council", "Administrative Decrees & Permits", "Disciplinary Courts", "Tax Disputes"),
        activeCasesCount = 64
    ),
    COMPENSATION(
        id = "compensation",
        nameAr = "تعويضات",
        nameEn = "Compensation",
        descriptionAr = "حوادث السير، الأخطاء الطبية، إصابات العمل، تعويضات شركات التأمين، والمسؤولية التقصيرية",
        descriptionEn = "Road accidents, medical malpractice, labor injury claims, insurance liability & torts",
        iconName = "handshake",
        subcategoriesAr = listOf("حوادث سيارات وتأمين", "أخطاء طبية", "إصابات عمل", "تعويض عن ضرر أدبي"),
        subcategoriesEn = listOf("Car Accidents & Insurance", "Medical Malpractice", "Work Injuries", "Moral Damages"),
        activeCasesCount = 115
    );

    fun getDisplayName(language: AppLanguage): String = if (language == AppLanguage.ARABIC) nameAr else nameEn
    fun getDescription(language: AppLanguage): String = if (language == AppLanguage.ARABIC) descriptionAr else descriptionEn
    fun getSubcategories(language: AppLanguage): List<String> = if (language == AppLanguage.ARABIC) subcategoriesAr else subcategoriesEn
}
