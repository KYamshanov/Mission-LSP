package ru.mission.glossary.uikit.components

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mission.glossary.theme.MissionTheme

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        content = { Text(text = label, style = MissionTheme.typography.secondaryButtonStyle) },
        colors = buttonColors(
            backgroundColor = MissionTheme.colors.gray
        ),
        shape = MissionTheme.shapes.mediumLarge
    )
}