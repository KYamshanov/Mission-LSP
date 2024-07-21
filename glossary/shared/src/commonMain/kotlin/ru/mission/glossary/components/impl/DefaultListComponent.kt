package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mission.glossary.Dictionary
import ru.mission.glossary.components.ListComponent
import kotlin.coroutines.CoroutineContext
import kotlinx.serialization.Serializable
import ru.mission.glossary.components.CardComponent
import ru.mission.glossary.getRandomWord
import ru.mission.glossary.models.*
import ru.mission.glossary.setLastItem
import kotlin.random.Random

internal class DefaultListComponent(
    componentContext: ComponentContext,
    private val mainContext: CoroutineContext,
    private val defaultContext: CoroutineContext,
    private val onItemSelected: (String) -> Unit,
    private val collectionId: Long,
    private val dictionary: Dictionary,
    private val back: () -> Unit,
) : ListComponent, ComponentContext by componentContext {

    private var words: List<Pair<WordTranslateWithId, TestingModel?>> = emptyList()

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
    override fun onCardSwiped(index: Int, isSuccess: Boolean) {
        navigation.navigate { stack ->
            val oldCardConfig = stack[index]
            val newWordConfig = getRandomWord(words).toConfig()
            updateStatistic(oldCardConfig.title, isSuccess)
            listOf(newWordConfig) + (stack - oldCardConfig).setLastItem { it.copy(isDraggable = true) }
        }
    }

    override fun onBack() {
        back()
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())


    init {
        scope.launch {
            val dictionary = dictionary.getWordsWithTesting(collectionId)
            println("DEBUG:: Words loaded: $dictionary")
            words = dictionary
            navigation.navigate {
                val initialCardConfigs = mutableListOf<CardConfig>()
                for (i in 0 until 3) {
                    val word = getRandomWord(words)
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


    private fun updateStatistic(word: String, isSuccess: Boolean) {
        scope.launch(defaultContext) {
            val wordTranslateIndex = words.indexOfFirst { it.first.word == word }.takeIf { it != -1 } ?: return@launch
            val wordTranslateTestingModelPair = words[wordTranslateIndex]
            val updatedTestingModel = wordTranslateTestingModelPair.second?.let {
                it.copy(
                    checkCount = it.checkCount + 1,
                    successCount = it.successCount + if (isSuccess) 1 else 0
                )
            }
                ?: firstTestingModel(wordTranslateTestingModelPair.first.wordId, isSuccess)
            dictionary.saveTesting(updatedTestingModel)
            println("DEBUG:: Updated testing stat. $updatedTestingModel")
            withContext(mainContext) {
                val newList = words.toMutableList()
                newList[wordTranslateIndex] = wordTranslateTestingModelPair.copy(second = updatedTestingModel)
                words = newList
            }
        }
    }

    companion object {

        private fun WordTranslate.toConfig(): CardConfig =
            CardConfig(
                title = word, subtitle = translate
            )
    }
}