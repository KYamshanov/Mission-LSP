package ru.mission.glossary.uikit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import glossary.ui.compose.kit.generated.resources.Res
import glossary.ui.compose.kit.generated.resources.clear
import glossary.ui.compose.kit.generated.resources.close
import glossary.ui.compose.kit.generated.resources.edit
import glossary.ui.compose.kit.generated.resources.square_edit_outline
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.mission.glossary.theme.MissionTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextFieldCompose(
    modifier: Modifier = Modifier,
    text: String,
    label: String? = null,
    maxLines: Int = 1,
    editable: Boolean,
    underlined: Boolean = true,
    suffix: String? = null,
    editableState: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    onValueChange: (String) -> Unit,
) = TextFieldCompose(
    modifier = modifier,
    text = text,
    label = label,
    maxLines = maxLines,
    editable = editable,
    underlined = underlined,
    suffix = suffix, editing = editableState.value,
    onValueChange = onValueChange,
    onEditingStarted = { editableState.value = true }
)

@Composable
fun TextFieldCompose(
    modifier: Modifier = Modifier,
    text: String,
    label: String? = null,
    maxLines: Int = 1,
    editable: Boolean,
    underlined: Boolean = true,
    suffix: String? = null,
    editing: Boolean,
    onValueChange: (String) -> Unit,
    onEditingStarted: () -> Unit
) {


    val rightIconComposable: (@Composable () -> Unit)? = if (editable) {
        {
            if (editing) {
                Image(
                    painter = painterResource(Res.drawable.close),
                    contentDescription = stringResource(Res.string.clear),
                    modifier = Modifier.clickable { onValueChange.invoke("") }.size(24.dp)
                )
            } else {
                Image(
                    painter = painterResource(Res.drawable.square_edit_outline),
                    contentDescription = stringResource(Res.string.edit),
                    modifier = Modifier
                        .clickable { onEditingStarted.invoke() }
                        .size(24.dp)
                )
            }
        }
    } else null

    EditTextField(
        modifier = modifier,
        text = text,
        label = label,
        maxLines = maxLines,
        rightIcon = rightIconComposable,
        onValueChange = onValueChange,
        editable = editing,
        underlined = underlined,
        suffix = suffix
    )
}

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    text: String,
    label: String? = null,
    rightIcon: (@Composable () -> Unit)? = null,
    underlined: Boolean = false,
) = TextField(
    modifier = modifier,
    label = label,
    content = { Text(text = text, style = MissionTheme.typography.inputText) },
    underlined = underlined,
    rightIcon = rightIcon,
)

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    label: String? = null,
    underlined: Boolean = false,
    rightIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) = BoxWithConstraints(modifier = modifier) {
    val localDensity = LocalDensity.current
    val iconSizeState = remember { mutableStateOf(IntSize.Zero) }


    Column(
        modifier = Modifier.align(Alignment.TopStart)
            .width(this.maxWidth - with(localDensity) { iconSizeState.value.width.toDp() })
    ) {
        label?.let { Text(it, style = MissionTheme.typography.inputHint) }
        content.invoke()
    }

    rightIcon?.let {
        Box(
            modifier = Modifier.align(Alignment.CenterEnd).onSizeChanged { iconSizeState.value = it }) { it.invoke() }
    }
    if (underlined) CellLine(modifier = Modifier.align(Alignment.BottomStart))
}