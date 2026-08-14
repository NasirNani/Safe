package com.example.model

data class UserProfileData(
    val fullNameAr: String = "عبد الله الطيبي",
    val fullNameEn: String = "Abdullah El-Tiby",
    val phone: String = "+20 100 123 4567",
    val email: String = "abdullaheltiby@gmail.com",
    val governorateAr: String = "القاهرة - التجمع الخامس",
    val governorateEn: String = "Cairo - 5th Settlement",
    val occupationAr: String = "مهندس برمجيات وريادي أعمال",
    val occupationEn: String = "Software Engineer & Entrepreneur",
    val nationalIdMasked: String = "2940815******",
    val isNationalIdVerified: Boolean = true,
    val memberSinceYear: String = "2024"
) {
    fun getFullName(language: AppLanguage): String = if (language == AppLanguage.ARABIC) fullNameAr else fullNameEn
    fun getGovernorate(language: AppLanguage): String = if (language == AppLanguage.ARABIC) governorateAr else governorateEn
    fun getOccupation(language: AppLanguage): String = if (language == AppLanguage.ARABIC) occupationAr else occupationEn
}

data class UserInquiryHistoryItem(
    val id: String,
    val lawyerId: String,
    val lawyerNameAr: String,
    val lawyerNameEn: String,
    val lawyerSpecialtyAr: String,
    val lawyerSpecialtyEn: String,
    val topicAr: String,
    val topicEn: String,
    val dateAr: String,
    val dateEn: String,
    val statusAr: String,
    val statusEn: String,
    val statusColorHex: Long,
    val messageCount: Int,
    val lastMessagePreviewAr: String,
    val lastMessagePreviewEn: String
) {
    fun getLawyerName(language: AppLanguage): String = if (language == AppLanguage.ARABIC) lawyerNameAr else lawyerNameEn
    fun getLawyerSpecialty(language: AppLanguage): String = if (language == AppLanguage.ARABIC) lawyerSpecialtyAr else lawyerSpecialtyEn
    fun getTopic(language: AppLanguage): String = if (language == AppLanguage.ARABIC) topicAr else topicEn
    fun getDate(language: AppLanguage): String = if (language == AppLanguage.ARABIC) dateAr else dateEn
    fun getStatus(language: AppLanguage): String = if (language == AppLanguage.ARABIC) statusAr else statusEn
    fun getLastMessagePreview(language: AppLanguage): String = if (language == AppLanguage.ARABIC) lastMessagePreviewAr else lastMessagePreviewEn
}

data class UserDownloadedContractItem(
    val id: String,
    val templateId: String,
    val titleAr: String,
    val titleEn: String,
    val downloadDateAr: String,
    val downloadDateEn: String,
    val fileSize: String,
    val fileFormat: String = "DOCX / PDF",
    val category: PracticeAreaCategory,
    val clausesCount: Int
) {
    fun getTitle(language: AppLanguage): String = if (language == AppLanguage.ARABIC) titleAr else titleEn
    fun getDownloadDate(language: AppLanguage): String = if (language == AppLanguage.ARABIC) downloadDateAr else downloadDateEn
}
