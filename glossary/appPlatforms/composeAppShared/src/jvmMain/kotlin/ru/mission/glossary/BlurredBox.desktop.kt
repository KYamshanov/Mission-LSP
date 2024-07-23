package ru.mission.glossary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp

@Composable
actual fun BlurredBox(
    modifier: Modifier,
    radius: Dp,
    content: @Composable BoxScope.() -> Unit
) = Box(modifier = modifier.blur(radius, BlurredEdgeTreatment.Unbounded), content = content)