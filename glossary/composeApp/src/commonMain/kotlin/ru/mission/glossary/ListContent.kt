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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.mission.glossary.components.ListComponent
import ru.mission.glossary.uikit.components.DraggableCard
import ru.mission.glossary.uikit.components.Surface
import ru.mission.glossary.uikit.components.TopBar
import ru.mission.glossary.uikit.utils.toPx

@Composable
fun ListContent(component: ListComponent, modifier: Modifier = Modifier) {
    val model by component.model.subscribeAsState()
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
    var lastItems = model.items.map {
        Item(it, MutableTransitionState(initialState = true))
    }
    Box(
        modifier = modifier.fillMaxSize().onPlaced { layoutSize = it.size }.padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        lastItems.forEachIndexed { index, (item, transitionState) ->
            val indexFromEnd = lastItems.lastIndex - index

            DraggableCard(
                layoutSize = layoutSize,
                offsetY = indexFromEnd * -16.dp.toPx(),
                scale = 1F - indexFromEnd.toFloat() / 20F,
                onSwiped = { },
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visibleState = transitionState,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                ) {

                    DisposableEffect(Unit) {
                        onDispose {
                            lastItems = lastItems.filterNot { it.instance == item }
                        }
                    }

                    Column(
                        modifier = modifier
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(size = 16.dp), clip = true)
                            .background(Color.Red)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(
                            space = 16.dp,
                            alignment = Alignment.CenterVertically
                        ),
                    ) {
                        Text(
                            text = item,
                            modifier = Modifier.clickable { component.onItemClicked(item = item) },
                        )
                    }
                }
            }
            //    }
        }
    }
}

private data class Item<out T : Any>(
    val instance: T,
    val transitionState: MutableTransitionState<Boolean>,
)
