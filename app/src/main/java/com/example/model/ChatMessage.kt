package com.example.model

enum class ChatSender {
    USER,
    ADVISOR,
    LAWYER,
    SYSTEM
}

data class ChatMessage(
    val id: String,
    val sender: ChatSender,
    val senderNameAr: String,
    val senderNameEn: String,
    val messageAr: String,
    val messageEn: String,
    val timestamp: String,
    val isRead: Boolean = true,
    val hasAttachment: Boolean = false,
    val attachmentName: String? = null
) {
    fun getSenderName(language: AppLanguage): String = if (language == AppLanguage.ARABIC) senderNameAr else senderNameEn
    fun getMessage(language: AppLanguage): String = if (language == AppLanguage.ARABIC) messageAr else messageEn
}
