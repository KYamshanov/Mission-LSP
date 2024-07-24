package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mission.glossary.Dictionary
import ru.mission.glossary.components.ListComponent
import kotlin.coroutines.CoroutineContext
import ru.mission.glossary.components.CardComponent
import ru.mission.glossary.getRandomWord
import ru.mission.glossary.models.*
import ru.mission.glossary.setLastItem
import kotlin.random.Random
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

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

    private var id: Long = stateKeeper.consume(key = "counter", strategy = serializer()) ?: 0L
        set(value) {
            field = if (value > 5_000_000) 0 else value
        }

    init {
        stateKeeper.register(key = "SAVED_STATE", strategy = serializer()) { id }
    }

    private val _stack: Value<ChildStack<CardConfig, CardComponent>> =
        childStack(
            source = navigation,
            serializer = CardConfig.serializer(),
            initialStack = {
                listOf(
                    CardConfig(
                        id = id++,
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
            updateStatistic(oldCardConfig.title, isSuccess)
            val newWordConfig = getRandomWord(words).toConfig(id++)
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
            val mutableWords = words.toMutableList()
            navigation.navigate {
                val initialCardConfigs = mutableListOf<CardConfig>()
                for (i in 0 until 3) {
                    val word = getRandomWord(words)
                    mutableWords.removeIf { it.first == word }
                    initialCardConfigs.add(word.toConfig(id++))
                }
                initialCardConfigs.setLastItem { it.copy(isDraggable = true) }
            }
        }
    }

    private fun card(config: CardConfig, componentContext: ComponentContext): CardComponent =
        CardComponentImpl(
            componentContext = componentContext,
            id = config.id,
            title = config.title,
            subtitle = config.subtitle,
            isDraggable = config.isDraggable,
        )

    @Serializable
    private data class CardConfig(
        val id: Long,
        val title: String,
        val subtitle: String,
        val isDraggable: Boolean = false,
    )

    private fun updateStatistic(word: String, isSuccess: Boolean) {
        val wordTranslateIndex = words.indexOfFirst { it.first.word == word }.takeIf { it != -1 } ?: return
        val wordTranslateTestingModelPair = words[wordTranslateIndex]
        val updatedTestingModel = wordTranslateTestingModelPair.second?.let {
            it.copy(
                checkCount = it.checkCount + 1,
                successCount = it.successCount + if (isSuccess) 1 else 0
            )
        }
            ?: firstTestingModel(wordTranslateTestingModelPair.first.wordId, isSuccess)
        val newList = words.toMutableList()
        newList[wordTranslateIndex] = wordTranslateTestingModelPair.copy(second = updatedTestingModel)
        words = newList
        scope.launch(defaultContext) {
            dictionary.saveTesting(updatedTestingModel)
            println("DEBUG:: Updated testing stat. $updatedTestingModel")
        }
    }

    companion object {

        private fun WordTranslate.toConfig(id: Long): CardConfig =
            CardConfig(
                id = id, title = word, subtitle = translate
            )
    }
}