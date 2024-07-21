package ru.mission.glossary.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


@Immutable
data class Palette(
    val colors: List<Color>
)

//get from https://icolorpalette.com/imagepalette/color-palette-ideas-from-sky-nature-atmosphere-image-52
internal val SkyPalette = Palette(
    colors = listOf(
        Color(0xFF518abc),
        Color(0xFF5a7297),
        Color(0xFF3675a6),
        Color(0xFF2f5476),
        Color(0xFF6c94ac)
    )
)

internal val LocalExtendedPalette = staticCompositionLocalOf {
    Palette(
        listOf()
    )
}
