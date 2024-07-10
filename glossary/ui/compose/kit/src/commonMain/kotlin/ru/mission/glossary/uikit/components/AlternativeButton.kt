package ru.mission.glossary.uikit.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.mission.glossary.theme.MissionTheme

@Composable
fun AlternativeButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
) = AlternativeButton(
    modifier = modifier,
    content = {
        Text(
            text = label,
            style = MissionTheme.typography.alternativeButtonStyle
        )
    },
    onClick = onClick,
)

@Composable
fun AlternativeButton(
    modifier: Modifier = Modifier,
    content: @Composable (RowScope.() -> Unit),
    onClick: () -> Unit,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
) =
    Button(
        modifier = modifier,
        onClick = onClick,
        content = content,
        colors = buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = Color.Transparent,
            disabledBackgroundColor = Color.Transparent,
            disabledContentColor = Color.Transparent,
        ),
        elevation = null,
        shape = MissionTheme.shapes.large,
        contentPadding = contentPadding
    )