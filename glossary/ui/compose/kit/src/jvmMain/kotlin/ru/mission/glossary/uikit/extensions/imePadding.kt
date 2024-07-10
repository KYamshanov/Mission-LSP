package ru.mission.glossary.uikit.extensions

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewConfiguration

actual fun Modifier.imePadding(): Modifier = this
actual fun Modifier.navigationBarsPadding(): Modifier = this
@Composable
actual fun ViewConfiguration.getOrientation(): Int = 0
@Composable
actual fun WindowInsets.Companion.getStatusBars(): WindowInsets = WindowInsets(0, 0, 0, 0)
actual fun Modifier.systemBarsPadding(): Modifier = this

@Composable
actual fun isSingleLineSupported(): Boolean = false