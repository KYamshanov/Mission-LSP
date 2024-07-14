package ru.mission.glossary

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.components.ListComponent
import ru.mission.glossary.uikit.components.DraggableCard
import ru.mission.glossary.uikit.utils.toPx

@Composable
fun ListContent(component: ListComponent, modifier: Modifier = Modifier) {
    val cardsStack by component.stack.subscribeAsState()
    val items = cardsStack.items
    /*  Surface(
          topContent = {
              TopBar(
                  title = "Загрузка...",
                  navigationListener = {
                      component.onBack()
                  }
              )
          }
      ) {*/

    var layoutSize by remember { mutableStateOf(IntSize.Zero) }

    var lastItems = items.map {
        Item(it.configuration, it.instance, MutableTransitionState(initialState = true))
    }

    DisposableEffect(items) {
        lastItems = lastItems.diff(items)
        onDispose {}
    }

    Box(
        modifier = modifier.fillMaxSize().onPlaced { layoutSize = it.size }.padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        lastItems.forEachIndexed { index, (configuration, instance, transitionState) ->
            key(configuration) {
                val indexFromEnd = lastItems.lastIndex - index

                SideEffect {
                    println("XYI $index  ${instance.model.value.title}  ${lastItems.lastIndex}")
                }
                DraggableCard(
                    layoutSize = layoutSize,
                    offsetY = indexFromEnd * -16.dp.toPx(),
                    scale = 1F - indexFromEnd.toFloat() / 20F,
                    isDraggable = index == lastItems.lastIndex,
                    onSwiped = { component.onCardSwiped(index) },
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visibleState = transitionState,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                    ) {

                        DisposableEffect(Unit) {
                            onDispose {
                                lastItems = lastItems.filterNot { it.configuration == configuration }
                            }
                        }

                        Column(
                            modifier = modifier
                                .defaultMinSize(minWidth = 160.dp, minHeight = 220.dp)
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(size = 16.dp), clip = true)
                                .background(Color.Red)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(
                                space = 16.dp,
                                alignment = Alignment.CenterVertically
                            ),
                        ) {
                            val model by instance.model.subscribeAsState()

                            Text(
                                text = model.title,
                                modifier = Modifier.clickable { /*component.onItemClicked(item = )*/ },
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
