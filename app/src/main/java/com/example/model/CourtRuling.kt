package com.example.model

data class CourtRuling(
    val id: String,
    val appealNumber: String,
    val judicialYear: String,
    val sessionDate: String,
    val chamberAr: String,
    val chamberEn: String,
    val category: PracticeAreaCategory,
    val principleAr: String,
    val principleEn: String,
    val fullRulingSummaryAr: String,
    val fullRulingSummaryEn: String,
    val keywordsAr: List<String>,
    val keywordsEn: List<String>
) {
    fun getChamber(language: AppLanguage): String = if (language == AppLanguage.ARABIC) chamberAr else chamberEn
    fun getPrinciple(language: AppLanguage): String = if (language == AppLanguage.ARABIC) principleAr else principleEn
    fun getSummary(language: AppLanguage): String = if (language == AppLanguage.ARABIC) fullRulingSummaryAr else fullRulingSummaryEn
    fun getKeywords(language: AppLanguage): List<String> = if (language == AppLanguage.ARABIC) keywordsAr else keywordsEn
}
