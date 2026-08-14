package com.example.model

data class ContractTemplate(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val category: PracticeAreaCategory,
    val descriptionAr: String,
    val descriptionEn: String,
    val legalBasisAr: String,
    val legalBasisEn: String,
    val clausesCount: Int,
    val downloadsCount: Int,
    val fileSize: String,
    val clausesSummaryAr: List<String>,
    val clausesSummaryEn: List<String>,
    val sampleTextAr: String,
    val sampleTextEn: String
) {
    fun getTitle(language: AppLanguage): String = if (language == AppLanguage.ARABIC) titleAr else titleEn
    fun getDescription(language: AppLanguage): String = if (language == AppLanguage.ARABIC) descriptionAr else descriptionEn
    fun getLegalBasis(language: AppLanguage): String = if (language == AppLanguage.ARABIC) legalBasisAr else legalBasisEn
    fun getClauses(language: AppLanguage): List<String> = if (language == AppLanguage.ARABIC) clausesSummaryAr else clausesSummaryEn
    fun getSampleText(language: AppLanguage): String = if (language == AppLanguage.ARABIC) sampleTextAr else sampleTextEn
}
