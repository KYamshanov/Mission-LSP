package ru.mission.glossary.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue

object MissionTheme {

    val colors: MissionColors
        @Composable
        get() = LocalExtendedColors.current

    val typography: MissionTypography
        @Composable
        get() = LocalExtendedTypography.current

    val shapes: MissionShapes
        @Composable
        get() = LocalExtendedShape.current

    val palette: Palette
        @Composable
        get() = LocalExtendedPalette.current
}

expect fun providedValueConfig(): List<ProvidedValue<*>>

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MissionTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    CompositionLocalProvider(
        LocalExtendedColors provides colors,
        LocalExtendedShape provides Shapes,
        LocalExtendedPalette provides SkyPalette,
        LocalTextSelectionColors provides MissionTextSelectionColors,
        LocalMinimumInteractiveComponentEnforcement provides false,
        *providedValueConfig().toTypedArray()
    ) {
        ProvideTypography(content)
    }
}

@Composable
private fun ProvideTypography(context: @Composable () -> Unit) {
    CompositionLocalProvider(LocalExtendedTypography provides typographyComposable()) {
        context.invoke()
    }
}