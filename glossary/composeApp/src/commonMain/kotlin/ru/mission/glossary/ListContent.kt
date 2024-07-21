package ru.mission.glossary

import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.components.ListComponent
import ru.mission.glossary.theme.MissionTheme
import ru.mission.glossary.uikit.components.DraggableCard
import ru.mission.glossary.uikit.utils.toPx
import kotlin.math.abs
import kotlin.math.min

@Composable
fun ListContent(component: ListComponent, modifier: Modifier = Modifier) {
    val cardsStack by component.stack.subscribeAsState()
    val items = cardsStack.items

    var layoutSize by remember { mutableStateOf(IntSize.Zero) }

    var lastItems = items.map {
        Item(it.configuration, it.instance, MutableTransitionState(initialState = true))
    }

    var dragTotalOffset by remember { mutableStateOf<Offset>(Offset.Zero) }



    DisposableEffect(items) {
        lastItems = lastItems.diff(items)
        onDispose {}
    }

    Box(
        modifier = modifier.fillMaxSize().onPlaced { layoutSize = it.size },
        contentAlignment = Alignment.Center,
    ) {
        val dragDistanceThreshold = min(200.dp.toPx(), layoutSize.width / 3f)
        val feedbackBufferThreshold = dragDistanceThreshold / 4

        if (layoutSize.width > 0) {
            val alphaState by animateFloatAsState(
                targetValue = ((dragTotalOffset.getDistance() - feedbackBufferThreshold) / (dragDistanceThreshold))
                    .coerceIn(0f, 1f),
                animationSpec = tween(easing = LinearEasing)
            )
            Box(
                modifier = modifier.fillMaxSize()
                    .run {
                        var color by remember { mutableStateOf(Color.Unspecified) }
                        if (alphaState > 0.1f && dragTotalOffset.getDistance() > 0) {
                            color = if (dragTotalOffset.x > 0) Color.Green else Color.Red
                        }
                        val alpha = alphaState
                        background(color.copy(alpha = alpha))
                    },
                contentAlignment = Alignment.Center,
            ) {}
        }
        lastItems.forEachIndexed { index, (configuration, instance, transitionState) ->
            key(instance) {
                val indexFromEnd = lastItems.lastIndex - index

                DraggableCard(
                    layoutSize = layoutSize,
                    offsetY = indexFromEnd * -16.dp.toPx(),
                    scale = 1F - indexFromEnd.toFloat() / 20F,
                    dragDistanceThreshold = dragDistanceThreshold,
                    isDraggable = instance.model.value.isDraggable,
                    onSwiped = { component.onCardSwiped(index, dragTotalOffset.x > 0) },
                    onDrag = {
                        dragTotalOffset = it
                    }
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visibleState = transitionState,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                    ) {
                        val model by instance.model.subscribeAsState()

                        DisposableEffect(Unit) {
                            onDispose {
                                lastItems = lastItems.filterNot { it.configuration == configuration }
                            }
                        }

                        val colorRGBA = model.colorRGBA

                        Column(
                            modifier = modifier
                                .defaultMinSize(minWidth = 256.dp, minHeight = 220.dp)
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(size = 16.dp), clip = true)
                                .background(
                                    Color(
                                        red = colorRGBA.red,
                                        green = colorRGBA.green,
                                        blue = colorRGBA.blue
                                    )
                                )
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(
                                space = 16.dp,
                                alignment = Alignment.CenterVertically
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = model.title,
                                style = MissionTheme.typography.mainButtonStyle
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.clickable { instance.clickOnSubtitle() }
                                    .run {
                                        if (model.blurredSubtitle) blur(
                                            5.dp,
                                            BlurredEdgeTreatment.Unbounded
                                        ) else this
                                    },
                                text = model.subtitle,
                                style = MissionTheme.typography.inputText
                            )
                        }
                    }
                }
            }
            //    }
        }
    }
}

private fun <T : Any> List<Item<T>>.diff(items: List<Child.Created<*, T>>): List<Item<T>> {
    val configs = items.map(Child.Created<*, *>::configuration)
    val missingItems = filterNot { it.configuration in configs }
    missingItems.forEach { it.transitionState.targetState = false }
    val lastTransitionStates =
        associateBy(keySelector = Item<*>::configuration, valueTransform = Item<*>::transitionState)

    return items.map { (configuration, instance) ->
        Item(
            configuration = configuration,
            instance = instance,
            transitionState = lastTransitionStates[configuration]
                ?: MutableTransitionState(initialState = false).apply { targetState = true },
        )
    } + missingItems
}

private data class Item<out T : Any>(
    val configuration: Any,
    val instance: T,
    val transitionState: MutableTransitionState<Boolean>,
)
