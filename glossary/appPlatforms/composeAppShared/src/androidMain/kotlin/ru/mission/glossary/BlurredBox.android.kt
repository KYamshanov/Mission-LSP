package ru.mission.glossary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.skydoves.cloudy.cloudy

@Composable
actual fun BlurredBox(
    modifier: Modifier,
    radius: Dp,
    content: @Composable BoxScope.() -> Unit
) = Box(modifier = modifier.cloudy(radius.value.toInt() * 4), content = content)