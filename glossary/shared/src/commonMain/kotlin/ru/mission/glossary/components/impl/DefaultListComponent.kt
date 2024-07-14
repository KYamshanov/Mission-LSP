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
import ru.mission.glossary.models.WordTranslate

internal class DefaultListComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    private val onItemSelected: (String) -> Unit,
    private val collectionId: Long,
    private val dictionary: Dictionary,
    private val back: () -> Unit,
) : ListComponent, ComponentContext by componentContext {

    private var words = listOf(WordTranslate("WORK", "xyi"))

    private val navigation = StackNavigation<Config>()

    private val _stack: Value<ChildStack<Config, CardComponent>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialStack = {
                words.map { it.toConfig() }
            },
            childFactory = ::card,
        )

    override val stack: Value<ChildStack<*, CardComponent>> = _stack

    override fun onItemClicked(item: String) = onItemSelected(item)
    override fun onCardSwiped(index: Int) {
        navigation.navigate { stack ->
            wordOffset++
            /*listOf(stack[index]) + words.subList(words.lastIndex - 3 - wordOffset, words.lastIndex - wordOffset)
                .map { it.toConfig() }
            */

            val config = stack[index]
            listOf(config) + (stack - config)
           // listOf(stack[index]) + (stack - stack[index])  //+ words[words.lastIndex - wordOffset].toConfig()
        }
    }

    override fun onBack() {
        back()
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())
    private var wordOffset = 0

    init {
        scope.launch {
            val dictionary = dictionary.getWords(collectionId)
            words = dictionary
            /*navigation.navigate {
                dictionary.takeLast(3)
                    .map { it.toConfig() }
            }*/
            dictionary.takeLast(3)
                .forEach {
                    navigation.push(it.toConfig())
                }
        }
    }

    private fun card(config: Config, componentContext: ComponentContext): CardComponent =
        CardComponentImpl(
            componentContext = componentContext,
            title = config.title,
            subtitle = config.subtitle,
        )

    @Serializable
    private data class Config(
        val title: String,
        val subtitle: String,
    )

    companion object {

        private fun WordTranslate.toConfig(): Config =
            Config(
                title = word, subtitle = translate
            )
    }
}