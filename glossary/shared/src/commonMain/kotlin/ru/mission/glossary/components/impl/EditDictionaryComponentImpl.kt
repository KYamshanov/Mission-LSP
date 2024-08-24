package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import ru.mission.glossary.Dictionary
import ru.mission.glossary.LoadSharedCollection
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.components.*
import ru.mission.glossary.components.EditDictionaryComponent.Child
import ru.mission.glossary.components.RootComponent.Child.DetailsChild
import ru.mission.glossary.components.RootComponent.Child.ListChild
import ru.mission.glossary.models.DictionaryGetResult
import ru.mission.glossary.models.WordsDictionary
import java.io.File
import kotlin.coroutines.CoroutineContext

internal class EditDictionaryComponentImpl(
    componentContext: ComponentContext,
    private val mainContext: CoroutineContext,
    private val defaultContext: CoroutineContext,
    private val collectionId: Long,
    private val dictionary: Dictionary,
    private val exit: () -> Unit,
) : EditDictionaryComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.ViewWords,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            Config.ViewWords -> Child.ViewWordsChild(
                ViewWordsComponentImpl(
                    componentContext = componentContext,
                    mainContext = mainContext,
                    defaultContext = defaultContext,
                    collectionId = collectionId,
                    dictionary = dictionary,
                    navToAddWordScreen = { navigation.push(Config.AddWord) },
                    exit = { exit() },
                    editWord = {
                        navigation.push(
                            Config.EditWord(
                                wordId = it.wordId,
                                contextSentence = it.contextSentence,
                                word = it.word,
                                translate = it.translate,
                                imageUrl = it.imageUrl
                            )
                        )
                    }
                )
            )

            Config.AddWord -> Child.AddWordChild(
                AddWordComponentImpl(
                    componentContext = componentContext,
                    mainContext = mainContext,
                    defaultContext = defaultContext,
                    collectionId = collectionId,
                    dictionary = dictionary,
                    back = { navigation.pop() }
                )
            )

            is Config.EditWord -> Child.EditWordChild(
                EditWordComponentImpl(
                    componentContext = componentContext,
                    mainContext = mainContext,
                    defaultContext = defaultContext,
                    wordId = config.wordId,
                    dictionary = dictionary,
                    back = { navigation.pop() },
                    word = config.word,
                    translate = config.translate,
                    imageUrl = config.imageUrl,
                    contextSentence = config.contextSentence,
                )
            )
        }

    @Serializable
    private sealed interface Config {

        @Serializable
        data object ViewWords : Config

        @Serializable
        data object AddWord : Config

        @Serializable
        data class EditWord(
            val wordId: Long,
            val word: String,
            val translate: String,
            val imageUrl: String?,
            val contextSentence: String?,
        ) : Config

    }

}