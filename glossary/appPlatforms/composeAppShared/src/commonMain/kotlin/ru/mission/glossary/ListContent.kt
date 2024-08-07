package ru.mission.glossary

import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.items
import com.github.panpf.sketch.AsyncImage
import glossary.ui.compose.kit.generated.resources.Res
import glossary.ui.compose.kit.generated.resources.close
import glossary.ui.compose.kit.generated.resources.ic_done
import org.jetbrains.compose.resources.painterResource
import ru.mission.glossary.components.ListComponent
import ru.mission.glossary.theme.MissionTheme
import ru.mission.glossary.uikit.components.DraggableCard
import ru.mission.glossary.uikit.utils.toPx
import kotlin.math.min

@Composable
fun ListContent(component: ListComponent, modifier: Modifier = Modifier) {
    val cardsStack by component.stack.subscribeAsState()
    val items = cardsStack.items

    var layoutSize by remember { mutableStateOf(IntSize.Zero) }

    var lastItems = items.map {
        Item(it.configuration, it.instance, MutableTransitionState(initialState = true))
    }

    var cardEditImageUrl by remember { mutableStateOf<Long?>(null) }

    var lastColors by remember { mutableStateOf(emptyMap<Long, Color>()) }

    lastColors = lastColors.toMutableMap()
        .filter { items.firstOrNull { v -> it.key == v.instance.model.value.id } != null }
        .toMutableMap()
        .apply {
            items.forEach { (_, v) ->
                val id = v.model.value.id
                if (!containsKey(id)) {
                    put(id, MissionTheme.palette.colors.random())
                }
            }
        }


    var dragTotalOffset by remember { mutableStateOf<Offset>(Offset.Zero) }

    MissionTheme.palette.background?.let {
        Image(
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            painter = painterResource(it),
            contentDescription = null
        )
    }


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
                    .coerceIn(0f, 0.8f),
                animationSpec = tween(easing = LinearEasing)
            )
            Box(
                modifier = modifier.fillMaxSize()
                    .run {
                        val successColor = MissionTheme.palette.success
                        val mistakeColor = MissionTheme.palette.failure
                        var color by remember { mutableStateOf(Color.Unspecified) }
                        if (alphaState > 0.1f && dragTotalOffset.getDistance() > 0) {
                            color = if (dragTotalOffset.x > 0) successColor else mistakeColor
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
                val model by instance.model.subscribeAsState()

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

                        DisposableEffect(Unit) {
                            onDispose {
                                lastItems =
                                    lastItems.filterNot { it.configuration == configuration }
                            }
                        }

                        val colorRGBA = lastColors[instance.model.value.id] ?: Color.Unspecified

                        Box(
                            modifier = modifier
                                .defaultMinSize(minWidth = 256.dp, minHeight = 220.dp)
                                .shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(size = 16.dp),
                                    clip = true
                                )
                                .background(
                                    colorRGBA
                                ),
                        ) {
                            if (!model.blurredSubtitle && model.imageUrl != null) {
                                val imageUri = model.imageUrl
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    uri = imageUri,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Column(
                                modifier = Modifier.fillMaxSize()
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

                                if (model.blurredSubtitle) {
                                    cardEditImageUrl = null
                                    BlurredBox(radius = 5.dp) {
                                        Text(
                                            modifier = Modifier.clickable { instance.clickOnSubtitle() },
                                            text = model.subtitle,
                                            style = MissionTheme.typography.inputText
                                        )
                                    }
                                } else {
                                    cardEditImageUrl = model.id
                                    Text(
                                        modifier = Modifier.clickable { instance.clickOnSubtitle() },
                                        text = model.subtitle,
                                        style = MissionTheme.typography.inputText
                                    )
                                }
                            }
                        }
                    }
                }
            }
            //    }
        }

        Box(
            modifier = Modifier.align(Alignment.TopStart)
                .padding(top = 16.dp, start = 12.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { component.onBack() }
                    .padding(8.dp),
                painter = painterResource(Res.drawable.close),
                contentDescription = "close",
                colorFilter = ColorFilter.tint(color = Color.White)
            )
        }

        cardEditImageUrl?.let { wordId ->
            val cardComponent = component.stack.items.first { it.instance.model.value.id == wordId }.instance
            Box(
                modifier = Modifier.align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 12.dp)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                contentAlignment = Alignment.Center,
            ) {
                val imageUrl = cardComponent.model.value.imageUrl
                UrlEditor(imageUrl.orEmpty()) { url ->
                    cardEditImageUrl = null
                    cardComponent.setImageUrl(url)
                }
            }
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

@Composable
fun UrlEditor(initialUrl: String, onUrlChanged: (String) -> Unit) {
    var imageUrlInput by remember { mutableStateOf(initialUrl) }
    Row(
        modifier = Modifier.alpha(0.5f).windowInsetsPadding(WindowInsets.safeDrawing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            modifier = Modifier.height(50.dp).widthIn(max = 300.dp),
            value = imageUrlInput,
            onValueChange = { imageUrlInput = it },
        )
        Box(
            modifier = Modifier
                .padding(12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onUrlChanged(imageUrlInput) }
                    .padding(8.dp),
                painter = painterResource(Res.drawable.ic_done),
                contentDescription = "done",
                colorFilter = ColorFilter.tint(color = Color.White)
            )
        }
    }
}

private data class Item<out T : Any>(
    val configuration: Any,
    val instance: T,
    val transitionState: MutableTransitionState<Boolean>,
)
