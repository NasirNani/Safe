package com.example.model

data class LawyerReview(
    val id: String,
    val reviewerNameAr: String,
    val reviewerNameEn: String,
    val rating: Double,
    val dateAr: String,
    val dateEn: String,
    val caseCategoryAr: String,
    val caseCategoryEn: String,
    val commentAr: String,
    val commentEn: String,
    val isVerifiedClient: Boolean = true
) {
    fun getReviewerName(language: AppLanguage): String = if (language == AppLanguage.ARABIC) reviewerNameAr else reviewerNameEn
    fun getDate(language: AppLanguage): String = if (language == AppLanguage.ARABIC) dateAr else dateEn
    fun getCaseCategory(language: AppLanguage): String = if (language == AppLanguage.ARABIC) caseCategoryAr else caseCategoryEn
    fun getComment(language: AppLanguage): String = if (language == AppLanguage.ARABIC) commentAr else commentEn
}

data class LawyerEducationCredential(
    val degreeAr: String,
    val degreeEn: String,
    val institutionAr: String,
    val institutionEn: String,
    val year: String
) {
    fun getDegree(language: AppLanguage): String = if (language == AppLanguage.ARABIC) degreeAr else degreeEn
    fun getInstitution(language: AppLanguage): String = if (language == AppLanguage.ARABIC) institutionAr else institutionEn
}
