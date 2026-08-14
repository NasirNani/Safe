package com.example.model

enum class NotificationType {
    CHAT_REPLY,
    CASE_STATUS_CHANGE,
    CONSULTATION_CONFIRMED,
    LEGAL_ALERT
}

data class AppNotification(
    val id: String,
    val type: NotificationType,
    val titleAr: String,
    val titleEn: String,
    val messageAr: String,
    val messageEn: String,
    val timeAgoAr: String,
    val timeAgoEn: String,
    val isRead: Boolean = false,
    val lawyerId: String? = null,
    val lawyerNameAr: String? = null,
    val lawyerNameEn: String? = null,
    val caseNumber: String? = null
) {
    fun getTitle(language: AppLanguage): String = if (language == AppLanguage.ARABIC) titleAr else titleEn
    fun getMessage(language: AppLanguage): String = if (language == AppLanguage.ARABIC) messageAr else messageEn
    fun getTimeAgo(language: AppLanguage): String = if (language == AppLanguage.ARABIC) timeAgoAr else timeAgoEn
    fun getLawyerName(language: AppLanguage): String? = if (language == AppLanguage.ARABIC) lawyerNameAr else lawyerNameEn
}
