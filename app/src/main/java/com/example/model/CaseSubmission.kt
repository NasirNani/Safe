package com.example.model

data class CaseSubmission(
    val id: String,
    val title: String,
    val category: PracticeAreaCategory,
    val description: String,
    val governorate: String,
    val urgencyLevel: UrgencyLevel,
    val budgetEgp: Int,
    val dateSubmitted: String,
    val statusAr: String,
    val statusEn: String,
    val matchedLawyersCount: Int = 3
) {
    fun getStatus(language: AppLanguage): String = if (language == AppLanguage.ARABIC) statusAr else statusEn
}

enum class UrgencyLevel(val nameAr: String, val nameEn: String) {
    NORMAL("عادي (خلال ٤٨ ساعة)", "Normal (within 48h)"),
    URGENT("عاجل (خلال ٢٤ ساعة)", "Urgent (within 24h)"),
    EMERGENCY("طارئ فوري (خلال ساعتين)", "Emergency (within 2h)")
}
