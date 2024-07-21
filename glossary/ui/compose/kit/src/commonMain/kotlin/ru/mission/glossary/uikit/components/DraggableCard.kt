package ru.mission.glossary.uikit.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOut
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import ru.mission.glossary.uikit.utils.toPx

/**
 * copied from https://github.com/arkivanov/Decompose/blob/master/sample/shared/compose/src/commonMain/kotlin/com/arkivanov/sample/shared/cards/CardsContent.kt
 */

sealed class CardSwipeState {

    data object Down : CardSwipeState()

    data class Up(val offsetState: State<Offset>) : CardSwipeState()
}

@Composable
fun DraggableCard(
    layoutSize: IntSize,
    offsetY: Float,
    scale: Float,
    onSwiped: () -> Unit,
    dragDistanceThreshold: Float,
    isDraggable: Boolean = true,
    onDrag: (Offset) -> Unit,
    content: @Composable () -> Unit,
) {
    var cardPosition: Offset by remember { mutableStateOf(Offset.Zero) }
    var cardSize: IntSize by remember { mutableStateOf(IntSize.Zero) }
    val minOffsetX: Float = -cardPosition.x - cardSize.width
    val maxOffsetX: Float = layoutSize.width - cardPosition.x
    val maxOffsetY: Float = -cardPosition.y

    var mode by remember { mutableStateOf(Mode.IDLE) }
    var startTouchPosition: Offset by remember { mutableStateOf(Offset.Zero) }
    val dragTotalOffset = remember { mutableStateOf<Offset>(Offset.Zero) }
    var dragLastOffset: Offset by remember { mutableStateOf(Offset.Zero) }

    val animatedOffset by animateOffsetAsState(
        targetValue = when (mode) {
            Mode.DRAG -> {
                dragTotalOffset.value + Offset(x = 0F, y = offsetY)
            }

            Mode.UP -> {
                val (x1, y1) = dragTotalOffset.value
                val x2 = x1 + dragLastOffset.x
                val y2 = y1 + dragLastOffset.y
                val upperOffsetX = ((maxOffsetY - y1) * (x2 - x1) / (y2 - y1) + x1).coerceIn(minOffsetX, maxOffsetX)
                Offset(x = dragTotalOffset.value.x * 3, y = dragTotalOffset.value.y * 1.1f)
            }

            Mode.IDLE,
            Mode.DOWN,
                -> Offset(x = 0F, y = offsetY)
        },
        animationSpec = if (mode == Mode.DRAG) snap() else tween()
    )

    val animatedScale by animateFloatAsState(targetValue = scale, animationSpec = tween())

    DisposableEffect(animatedOffset, mode, offsetY) {
        if ((mode == Mode.UP) && (animatedOffset.y == dragTotalOffset.value.y * 1.1f)) {
            onSwiped()
            mode = Mode.IDLE
            onDrag(Offset.Zero)
        } else if ((mode == Mode.DOWN) && (animatedOffset.y == offsetY)) {
            mode = Mode.IDLE
        }

        onDispose {}
    }


    LaunchedEffect(animatedOffset) {
        if (mode == Mode.DOWN){
            onDrag(animatedOffset)
        }
    }


    var rememberCardSwipeState by remember { mutableStateOf<CardSwipeState>(CardSwipeState.Down) }

    Box(
        modifier = Modifier
            .onPlaced {
                cardPosition = it.positionInParent()
                cardSize = it.size
            }
            .requiredWidthIn(max = 256.dp)
            .offset { animatedOffset.round() }
            .aspectRatio(ratio = 1.5882353F)
            .pointerInput(Unit) {
                if (isDraggable) {
                    detectDragGestures(
                        onDragStart = { position ->
                            startTouchPosition = position
                            dragTotalOffset.value = Offset.Zero
                            mode = Mode.DRAG
                        },
                        onDragEnd = {
                            mode =
                                if (dragTotalOffset.value.getDistance() > dragDistanceThreshold) Mode.UP else Mode.DOWN
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragTotalOffset.value += dragAmount
                            dragLastOffset = dragAmount
                            onDrag(dragTotalOffset.value)
                        },
                    )
                }
            }
            .scale(animatedScale)
            .graphicsLayer {
                if (mode == Mode.IDLE) {
                    return@graphicsLayer
                }

                transformOrigin =
                    TransformOrigin(
                        pivotFractionX = startTouchPosition.x / size.width,
                        pivotFractionY = startTouchPosition.y / size.height,
                    )

                val verticalFactor =
                    if (maxOffsetY - offsetY == 0f) 0f else (animatedOffset.y - offsetY) / (maxOffsetY - offsetY)
                val horizontalFactor = transformOrigin.pivotFractionX * 2F - 1F
                rotationZ = verticalFactor * horizontalFactor * -30F
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

private enum class Mode {
    /**
     * initial state
     */
    IDLE,

    /**
     * When card is moving
     */
    DRAG,

    /**
     * means cars is ready to swipe
     */
    UP,
    DOWN
}
