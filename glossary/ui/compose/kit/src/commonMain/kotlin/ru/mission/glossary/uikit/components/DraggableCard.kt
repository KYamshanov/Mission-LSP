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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun DraggableCard(
    layoutSize: IntSize,
    offsetY: Float,
    scale: Float,
    onSwiped: () -> Unit,
    isDraggable: Boolean = true,
    content: @Composable () -> Unit,
) {
    var cardPosition: Offset by remember { mutableStateOf(Offset.Zero) }
    var cardSize: IntSize by remember { mutableStateOf(IntSize.Zero) }
    val minOffsetX: Float = -cardPosition.x - cardSize.width
    val maxOffsetX: Float = layoutSize.width - cardPosition.x
    val maxOffsetY: Float = -cardPosition.y

    var mode by remember { mutableStateOf(Mode.IDLE) }
    var startTouchPosition: Offset by remember { mutableStateOf(Offset.Zero) }
    var dragTotalOffset: Offset by remember { mutableStateOf(Offset.Zero) }
    var dragLastOffset: Offset by remember { mutableStateOf(Offset.Zero) }
    val dragDistanceThreshold = 3.dp.toPx()

    val animatedOffset by animateOffsetAsState(
        targetValue = when (mode) {
            Mode.DRAG -> {
                println("TEST2 $dragTotalOffset  ${Offset(x = 0F, y = offsetY)}")
                dragTotalOffset + Offset(x = 0F, y = offsetY)
            }

            Mode.UP -> {
                val (x1, y1) = dragTotalOffset
                val x2 = x1 + dragLastOffset.x
                val y2 = y1 + dragLastOffset.y
                println("TEST: $y2")
                val upperOffsetX = ((maxOffsetY - y1) * (x2 - x1) / (y2 - y1) + x1).coerceIn(minOffsetX, maxOffsetX)
                Offset(x = upperOffsetX, y = maxOffsetY)
            }

            Mode.IDLE,
            Mode.DOWN,
            -> Offset(x = 0F, y = offsetY)
        },
        animationSpec = if (mode == Mode.DRAG) snap() else tween()
    )

    val animatedScale by animateFloatAsState(targetValue = scale, animationSpec = tween())

    DisposableEffect(animatedOffset, mode, offsetY) {
        if ((mode == Mode.UP) && (animatedOffset.y == maxOffsetY)) {
            onSwiped()
            mode = Mode.DOWN
        } else if ((mode == Mode.DOWN) && (animatedOffset.y == offsetY)) {
            mode = Mode.IDLE
        }

        onDispose {}
    }

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
                if(isDraggable){
                    detectDragGestures(
                        onDragStart = { position ->
                            startTouchPosition = position
                            dragTotalOffset = Offset.Zero
                            mode = Mode.DRAG
                        },
                        onDragEnd = {
                            mode = if (dragLastOffset.getDistance() > dragDistanceThreshold) Mode.UP else Mode.DOWN
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragTotalOffset += dragAmount
                            dragLastOffset = dragAmount
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
            }
    ) {
        content()
    }
}

private enum class Mode {
    IDLE,
    DRAG,
    UP,
    DOWN
}
