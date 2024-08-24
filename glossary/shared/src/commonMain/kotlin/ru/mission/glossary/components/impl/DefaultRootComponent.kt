package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.subscribe
import kotlinx.serialization.Serializable
import org.koin.core.definition.Callbacks
import ru.mission.glossary.Dictionary
import ru.mission.glossary.LoadSharedCollection
import ru.mission.glossary.ShareCollection
import ru.mission.glossary.components.RootComponent.Child.DetailsChild
import ru.mission.glossary.components.RootComponent.Child.ListChild
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.components.*
import kotlin.coroutines.CoroutineContext

internal class DefaultRootComponent(
    componentContext: ComponentContext,
    private val singleAppParser: SingleAppParser,
    private val mainContext: CoroutineContext,
    private val defaultContext: CoroutineContext,
    private val dictionary: Dictionary,
    private val shareCollection: ShareCollection,
    private val loadSharedCollection: LoadSharedCollection,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Collections, // The initial child component is List
            handleBackButton = true, // Automatically pop from the stack on back button presses
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.List -> ListChild(listComponent(componentContext, config))
            is Config.Details -> DetailsChild(detailsComponent(componentContext, config))

            is Config.LoadDictionary -> {
                RootComponent.Child.LoadDictionaryChild(
                    loadDictionaryComponent(componentContext, config)
                )
            }

            is Config.Collections -> RootComponent.Child.CollectionsChild(
                collectionsComponent(
                    componentContext,
                    config
                )
            )

            is Config.EditDictionary -> {
                RootComponent.Child.EditDictionaryChild(
                    editDictionaryComponent(componentContext, config)
                )
            }
        }

    private fun listComponent(componentContext: ComponentContext, config: Config.List): ListComponent =
        DefaultListComponent(
            componentContext = componentContext,
            onItemSelected = { item: String -> // Supply dependencies and callbacks
                navigation.push(Config.Details(item = item)) // Push the details component
            },
            mainContext = mainContext,
            defaultContext = defaultContext,
            dictionary = dictionary,
            collectionId = config.collectionId,
            back = { navigation.pop() },
            withRefresh = config.withLearn,
        )

    private fun detailsComponent(componentContext: ComponentContext, config: Config.Details): DetailsComponent =
        DefaultDetailsComponent(
            componentContext = componentContext,
            title = config.item, // Supply arguments from the configuration
            onFinished = navigation::pop, // Pop the details component
        )

    private fun loadDictionaryComponent(
        componentContext: ComponentContext,
        config: Config.LoadDictionary,
    ): LoadDictionaryComponent =
        LoadDictionaryComponentImpl(
            componentContext = componentContext,
            initialUrl = config.initialUrl,
            onLoadDictionary = { navigation.replaceCurrent(Config.List(it, true)) },
            dictionary = dictionary,
            mainContext = mainContext,
            defaultContext = defaultContext,
            singleAppParser = singleAppParser,
            back = { navigation.pop() },
            loadSharedCollection = loadSharedCollection
        )

    private fun collectionsComponent(
        componentContext: ComponentContext,
        config: Config.Collections,
    ): CollectionsComponent =
        CollectionsComponentImpl(
            componentContext = componentContext,
            mainContext = mainContext,
            dictionary = dictionary,
            loadCollection = { navigation.push(Config.LoadDictionary()) },
            openCollection = { navigation.push(Config.List(it, false)) },
            shareCollection = shareCollection,
            openCollectionWithRefresh = { navigation.push(Config.List(it, true)) },
            editCollection = { navigation.push(Config.EditDictionary(it)) }
        )

    private fun editDictionaryComponent(
        componentContext: ComponentContext,
        config: Config.EditDictionary,
    ): EditDictionaryComponent =
        EditDictionaryComponentImpl(
            componentContext = componentContext,
            mainContext = mainContext,
            defaultContext = defaultContext,
            dictionary = dictionary,
            collectionId = config.collectionId,
            exit = { navigation.pop() }
        )

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    @Serializable // kotlinx-serialization plugin must be applied
    private sealed interface Config {

        @Serializable
        data class List(val collectionId: Long, val withLearn: Boolean) : Config

        @Serializable
        data class Details(val item: String) : Config

        @Serializable
        data class LoadDictionary(val initialUrl: String = "") : Config

        @Serializable
        data object Collections : Config

        @Serializable
        data class EditDictionary(val collectionId: Long) : Config
    }
}

