package com.example.model

enum class AppLanguage(val code: String, val displayNameAr: String, val displayNameEn: String) {
    ARABIC("ar", "العربية", "Arabic"),
    ENGLISH("en", "الإنجليزية", "English")
}

data class LocalizedText(
    val ar: String,
    val en: String
) {
    fun get(language: AppLanguage): String = if (language == AppLanguage.ARABIC) ar else en
}
