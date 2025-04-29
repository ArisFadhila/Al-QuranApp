package com.example.alquranapp

data class SurahListResponse(
    val data: List<Surah>
)

data class Surah(
    val number: Int,
    val name: String,
    val englishName: String,
    val revelationType: String,
    val numberOfAyahs: Int
)

data class SurahDetailsResponse(
    val data: SurahDetails
)

data class SurahDetails(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String,
    val numberOfAyahs: Int,
    val ayahs: List<RawAyah>
)

data class RawAyah(
    val number: Int,
    val numberInSurah: Int,
    val text: String,
    val audio: String
)


data class Ayah(
    val number: Int,
    val numberInSurah: Int,
    val arabicText: String,
    val translationText: String,
    val audioUrl: String
)
