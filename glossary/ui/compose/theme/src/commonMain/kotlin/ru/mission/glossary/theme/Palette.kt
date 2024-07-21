package ru.mission.glossary.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import glossary.ui.compose.theme.generated.resources.Res
import glossary.ui.compose.theme.generated.resources.sky
import org.jetbrains.compose.resources.DrawableResource


@Immutable
data class Palette(
    val success: Color,
    val failure: Color,
    val colors: List<Color>,
    val background: DrawableResource?
)

//get from https://icolorpalette.com/imagepalette/color-palette-ideas-from-sky-nature-atmosphere-image-52
internal val SkyPalette = Palette(
    success = Color(0xFF236491),
    failure = Color(0xFF24141f),
    colors = listOf(
        Color(0xFF518abc),
        Color(0xFF5a7297),
        Color(0xFF3675a6),
        Color(0xFF2f5476),
        Color(0xFF6c94ac),
    ),
    background = Res.drawable.sky,
)

internal val LocalExtendedPalette = staticCompositionLocalOf {
    Palette(
        success = Color.Unspecified,
        failure = Color.Unspecified,
        colors = listOf(),
        background = null,
    )
}
