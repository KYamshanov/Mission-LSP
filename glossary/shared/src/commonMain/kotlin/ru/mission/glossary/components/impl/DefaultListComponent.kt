package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.mission.glossary.Dictionary
import ru.mission.glossary.components.ListComponent
import kotlin.coroutines.CoroutineContext
import kotlinx.serialization.Serializable
import ru.mission.glossary.components.CardComponent
import ru.mission.glossary.models.ColorRGBA
import ru.mission.glossary.models.RandomColor
import ru.mission.glossary.models.WordTranslate
import ru.mission.glossary.setFirstItem
import ru.mission.glossary.setLastItem
import kotlin.random.Random

internal class DefaultListComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    private val onItemSelected: (String) -> Unit,
    private val collectionId: Long,
    private val dictionary: Dictionary,
    private val back: () -> Unit,
) : ListComponent, ComponentContext by componentContext {

    private var words = listOf(WordTranslate("WORK", "xyi"))

    private val navigation = StackNavigation<CardConfig>()

    private val _stack: Value<ChildStack<CardConfig, CardComponent>> =
        childStack(
            source = navigation,
            serializer = CardConfig.serializer(),
            initialStack = {
                listOf(
                    CardConfig(
                        title = "Loading...",
                        subtitle = "Загрузка...",
                        isDraggable = false,
                    )
                )
            },
            childFactory = ::card,
        )

    override val stack: Value<ChildStack<*, CardComponent>> = _stack

    override fun onItemClicked(item: String) = onItemSelected(item)
    override fun onCardSwiped(index: Int) {
        navigation.navigate { stack ->
            val newWordConfig = words[random.nextInt(words.size)].toConfig()
            val oldCardConfig = stack[index]
            listOf(newWordConfig) + (stack - oldCardConfig).setLastItem { it.copy(isDraggable = true) }
        }
    }

    override fun onBack() {
        back()
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    private val random = Random(2)

    init {
        scope.launch {
            val dictionary = dictionary.getWords(collectionId)
            words = dictionary
            navigation.navigate {
                val initialCardConfigs = mutableListOf<CardConfig>()
                for (i in 0 until 3) {
                    val word = dictionary[random.nextInt(dictionary.size)]
                    initialCardConfigs.add(word.toConfig())
                }
                initialCardConfigs.setLastItem { it.copy(isDraggable = true) }
            }
        }
    }

    private fun card(config: CardConfig, componentContext: ComponentContext): CardComponent =
        CardComponentImpl(
            componentContext = componentContext,
            title = config.title,
            subtitle = config.subtitle,
            isDraggable = config.isDraggable,
            color = config.color
        )


    @Serializable
    private data class CardConfig(
        val title: String,
        val subtitle: String,
        val isDraggable: Boolean = false,
        val color: ColorRGBA = RandomColor(),
    )

    companion object {

        private fun WordTranslate.toConfig(): CardConfig =
            CardConfig(
                title = word, subtitle = translate
            )
    }
}