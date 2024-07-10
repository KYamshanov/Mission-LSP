package ru.mission.glossary.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.runtime.ProvidedValue

@OptIn(ExperimentalFoundationApi::class)
actual fun providedValueConfig(): List<ProvidedValue<*>> = listOf(
    LocalOverscrollConfiguration provides null
)