package com.example.model

data class Lawyer(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val titleAr: String,
    val titleEn: String,
    val category: PracticeAreaCategory,
    val barLevelAr: String,
    val barLevelEn: String,
    val experienceYears: Int,
    val rating: Double,
    val reviewsCount: Int,
    val consultationFeeEgp: Int,
    val governorateAr: String,
    val governorateEn: String,
    val officeAddressAr: String,
    val officeAddressEn: String,
    val bioAr: String,
    val bioEn: String,
    val phone: String,
    val isVerified: Boolean = true,
    val isAvailableOnline: Boolean = true,
    val casesWonCount: Int = 180,
    val avatarColorHex: Long = 0xFF1E3A70,
    val avatarDrawableRes: Int? = null
) {
    fun getName(language: AppLanguage): String = if (language == AppLanguage.ARABIC) nameAr else nameEn
    fun getTitle(language: AppLanguage): String = if (language == AppLanguage.ARABIC) titleAr else titleEn
    fun getBarLevel(language: AppLanguage): String = if (language == AppLanguage.ARABIC) barLevelAr else barLevelEn
    fun getGovernorate(language: AppLanguage): String = if (language == AppLanguage.ARABIC) governorateAr else governorateEn
    fun getOfficeAddress(language: AppLanguage): String = if (language == AppLanguage.ARABIC) officeAddressAr else officeAddressEn
    fun getBio(language: AppLanguage): String = if (language == AppLanguage.ARABIC) bioAr else bioEn
}
